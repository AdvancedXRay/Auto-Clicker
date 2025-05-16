package pro.mikey.autoclicker;

import com.google.common.base.Suppliers;
import com.google.gson.Gson;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.apache.commons.lang3.concurrent.LazyInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

public class AutoClicker {
    public static final String MOD_ID = "autoclicker";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final KeyMapping openConfig =
            new KeyMapping("keybinding.open-gui", GLFW.GLFW_KEY_O, "category.autoclicker-fabric");
    public static final KeyMapping toggleHolding =
            new KeyMapping("keybinding.toggle-hold", GLFW.GLFW_KEY_I, "category.autoclicker-fabric");

    private static final Supplier<Pair<Path, Path>> CONFIG_PATHS = Suppliers.memoize(() -> {
        Path configDir = Paths.get(Minecraft.getInstance().gameDirectory.getPath() + "/config");
        Path configFile = Paths.get(configDir + "/auto-clicker-fabric.json");
        return Pair.of(configDir, configFile);
    });

    public static Holding.AttackHolding leftHolding;
    public static Holding rightHolding;
    public static Holding jumpHolding;
    public static Config.HudConfig hudConfig;
    private static AutoClicker INSTANCE;
    private boolean isActive = false;
    private Config config = new Config(
            new Config.LeftMouseConfig(false, false, 0, false, false, false),
            new Config.RightMouseConfig(false, false, 0),
            new Config.JumpConfig(false, false, 0),
            new Config.HudConfig(true, "top-left")
    );

    public AutoClicker() {
        INSTANCE = this;
    }

    public static AutoClicker getInstance() {
        return INSTANCE;
    }

    public void onInitialize() {
        LOGGER.info("Auto Clicker Initialised");
    }

    public void clientReady(Minecraft client) {
        var configPaths = CONFIG_PATHS.get();
        if (!Files.exists(configPaths.value())) {
            try {
                Files.createDirectories(configPaths.key());
                Files.createFile(configPaths.value());
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.saveConfig();
        } else {
            try {
                FileReader json = new FileReader(configPaths.value().toFile());
                Config config = new Gson().fromJson(json, Config.class);
                json.close();
                if (config != null && config.getHudConfig() != null) {
                    this.config = config;
                }
            } catch (Exception e){
                e.printStackTrace();
                this.saveConfig();
            }
        }

        leftHolding = new Holding.AttackHolding(client.options.keyAttack, this.config.getLeftClick());
        rightHolding = new Holding(client.options.keyUse, this.config.getRightClick());
        jumpHolding = new Holding(client.options.keyJump, this.config.getJump());
        hudConfig = this.config.getHudConfig();
    }

    public void saveConfig() {
        var configPaths = CONFIG_PATHS.get();
        try {
            FileWriter writer = new FileWriter(configPaths.value().toFile());

            new Gson().toJson(this.config, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renderGameOverlayEvent(GuiGraphics context, DeltaTracker delta) {

        if ((!leftHolding.isActive() && !rightHolding.isActive() && !jumpHolding.isActive()) || !this.isActive || !config.getHudConfig().isEnabled()) {
            return;
        }

        Minecraft client = Minecraft.getInstance();

        if (leftHolding.isActive()) {
            Component text = Language.HUD_HOLDING.getText(I18n.get(leftHolding.getKey().getName()));
            int y = getHudY() + (15 * 0);
            int x = getHudX(text);
            context.drawString(client.font, text.getVisualOrderText(), x, y, 0xffffff);
        }

        if (rightHolding.isActive()) {
            Component text = Language.HUD_HOLDING.getText(I18n.get(rightHolding.getKey().getName()));
            int y = getHudY() + (15 * 1);
            int x = getHudX(text);
            context.drawString(client.font, text.getVisualOrderText(), x, y, 0xffffff);
        }

        if (jumpHolding.isActive()) {
            Component text = Language.HUD_HOLDING.getText(I18n.get(jumpHolding.getKey().getName()));
            int y = getHudY() + (15 * 2);
            int x = getHudX(text);
            context.drawString(client.font, text.getVisualOrderText(), x, y, 0xffffff);
        }
    }

    public int getHudX(Component text){
        Minecraft client = Minecraft.getInstance();

        String location = this.config.getHudConfig().getLocation();
        return switch (location) {
            case "top-left", "bottom-left" -> 10;
            case "top-right", "bottom-right" -> (Minecraft.getInstance().getWindow().getGuiScaledWidth()) - 10 - client.font.width(text);
            default -> 10;
        };
    }
    public int getHudY(){
        String location = this.config.getHudConfig().getLocation();
        return switch (location) {
            case "top-left", "top-right" -> 10;
            case "bottom-left", "bottom-right" -> (Minecraft.getInstance().getWindow().getGuiScaledHeight()) - 50;
            default -> 10;
        };
    }

    public void clientTickEvent(Minecraft mc) {
        if (mc.player == null || mc.level == null) {
            return;
        }
        if(!mc.player.isAlive()) this.isActive = false;

        if (this.isActive) {
            if (leftHolding.isActive()) {
                this.handleActiveHolding(mc, leftHolding);
            }

            if (rightHolding.isActive()) {
                this.handleActiveHolding(mc, rightHolding);
            }

            if (jumpHolding.isActive()) {
                this.handleActiveHolding(mc, jumpHolding);
            }
        }

        this.keyInputEvent(mc);
    }

    private void handleActiveHolding(Minecraft mc, Holding key) {
        assert mc.player != null;
        if (!key.isActive()) {
            return;
        }

        if (key.isSpamming()) {
            // How to handle the click if it's done by spamming
            if (key.getSpeed() > 0) {
                if (key.getTimeout() <= 1) {
                    if (key.getTimeout() <= 0) {
                        key.resetTimeout();
                    }

                    // Press the button twice by toggling 1 and 0
                    key.getKey().setDown(key.getTimeout() == 1);

                    if (key.getKey().isDown()) {
                        this.attemptMobAttack(mc, key);
                    }
                }
                key.decreaseTimeout();
            } else {
                // Handle the click if it's done normally
                key.getKey().setDown(!key.getKey().isDown());
                if (key.getKey().isDown()) {
                    this.attemptMobAttack(mc, key);
                }
            }

            return;
        }

        // Normal holding or cool down behaviour
        // respect cool down
        if (key.isRespectCooldown()) {
            // Don't do anything if they're not looking at something
            if (key instanceof Holding.AttackHolding && ((Holding.AttackHolding) key).isMobMode() && !this.isPlayerLookingAtMob(mc)) {
                if (key.getKey().isDown()) {
                    key.getKey().setDown(false);
                }
                return;
            }

            if (mc.player.getAttackStrengthScale(0) == 1.0F) {
                key.getKey().setDown(true);
                this.attemptMobAttack(mc, key);
            } else {
                key.getKey().setDown(false);
            }
        } else {
            // Hold the click
            key.getKey().setDown(true);
        }
    }

    private void attemptMobAttack(Minecraft mc, Holding key) {
        // Don't attack on a right click
        if (key.getKey() != leftHolding.getKey()) {
            return;
        }

        HitResult rayTrace = mc.hitResult;
        if (rayTrace instanceof EntityHitResult && mc.gameMode != null) {
            if(!(config.getLeftClick().isRespectShield() && isShielding(mc.player))) {
                mc.gameMode.attack(mc.player, ((EntityHitResult) rayTrace).getEntity());
                if (mc.player != null) {
                    mc.player.swing(InteractionHand.MAIN_HAND);
                }
            }
        }
    }

    private boolean isShielding(LocalPlayer player) {
        if (player.isUsingItem()) {
            return player.getUseItem().getItem() instanceof ShieldItem;
        }
        return false;
    }

    private boolean isPlayerLookingAtMob(Minecraft mc) {
        HitResult rayTrace = mc.hitResult;
        return rayTrace instanceof EntityHitResult && ((EntityHitResult) rayTrace).getEntity() instanceof LivingEntity livingEntity && livingEntity.isAlive() && livingEntity.isAttackable();
    }

    private void keyInputEvent(Minecraft mc) {
        assert mc.player != null;
        while (toggleHolding.consumeClick()) {
            this.isActive = !this.isActive;
            mc.player.displayClientMessage(
                    (this.isActive ? Language.MSG_HOLDING_KEYS : Language.MSG_RELEASED_KEYS)
                    .getText()
                            .withStyle(this.isActive ? ChatFormatting.GREEN : ChatFormatting.RED),
                    true
                    );

            if (!this.isActive) {
                if(leftHolding.isActive()) leftHolding.getKey().setDown(false);
                if(rightHolding.isActive()) rightHolding.getKey().setDown(false);
                if(jumpHolding.isActive()) jumpHolding.getKey().setDown(false);
            }
        }

        while (openConfig.consumeClick()) {
            mc.setScreen(getConfigScreen());
        }
    }

    public OptionsScreen getConfigScreen(){
        return new OptionsScreen();
    }
}
