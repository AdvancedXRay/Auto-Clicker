package pro.mikey.autoclicker;

import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AutoClicker implements ModInitializer {
    public static final String MOD_ID = "autoclicker-fabric";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final KeyBinding rightClickToggle =
            new KeyBinding("keybinding.open-gui", GLFW.GLFW_KEY_O, "category.autoclicker-fabric");
    private static final KeyBinding toggleHolding =
            new KeyBinding("keybinding.toggle-hold", GLFW.GLFW_KEY_I, "category.autoclicker-fabric");
    private static final Path CONFIG_DIR = Paths.get(MinecraftClient.getInstance().runDirectory.getPath() + "/config");
    private static final Path CONFIG_FILE = Paths.get(CONFIG_DIR + "/auto-clicker-fabric.json");
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

    @Override
    public void onInitialize() {
        LOGGER.info("Auto Clicker Initialised");

        ClientTickEvents.END_CLIENT_TICK.register(this::clientTickEvent);

        KeyBindingHelper.registerKeyBinding(toggleHolding);
        KeyBindingHelper.registerKeyBinding(rightClickToggle);

        ClientLifecycleEvents.CLIENT_STARTED.register(this::clientReady);
        HudRenderCallback.EVENT.register(this::RenderGameOverlayEvent);
    }

    private void clientReady(MinecraftClient client) {
        if (!Files.exists(CONFIG_FILE)) {
            try {
                Files.createDirectories(CONFIG_DIR);
                Files.createFile(CONFIG_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.saveConfig();
        } else {
            try {
                FileReader json = new FileReader(CONFIG_FILE.toFile());
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

        leftHolding = new Holding.AttackHolding(client.options.attackKey, this.config.getLeftClick());
        rightHolding = new Holding(client.options.useKey, this.config.getRightClick());
        jumpHolding = new Holding(client.options.jumpKey, this.config.getJump());
        hudConfig = this.config.getHudConfig();
    }

    public void saveConfig() {
        try {
            FileWriter writer = new FileWriter(CONFIG_FILE.toFile());

            new Gson().toJson(this.config, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void RenderGameOverlayEvent(DrawContext context, RenderTickCounter delta) {

        if ((!leftHolding.isActive() && !rightHolding.isActive() && !jumpHolding.isActive()) || !this.isActive || !config.getHudConfig().isEnabled()) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();

        if (leftHolding.isActive()) {
            Text text = Language.HUD_HOLDING.getText(I18n.translate(leftHolding.getKey().getTranslationKey()));
            int y = getHudY() + (15 * 0);
            int x = getHudX(text);
            context.drawTextWithShadow(client.textRenderer, text.asOrderedText(), x, y, 0xffffff);
        }

        if (rightHolding.isActive()) {
            Text text = Language.HUD_HOLDING.getText(I18n.translate(rightHolding.getKey().getTranslationKey()));
            int y = getHudY() + (15 * 1);
            int x = getHudX(text);
            context.drawTextWithShadow(client.textRenderer, text.asOrderedText(), x, y, 0xffffff);
        }

        if (jumpHolding.isActive()) {
            Text text = Language.HUD_HOLDING.getText(I18n.translate(jumpHolding.getKey().getTranslationKey()));
            int y = getHudY() + (15 * 2);
            int x = getHudX(text);
            context.drawTextWithShadow(client.textRenderer, text.asOrderedText(), x, y, 0xffffff);
        }
    }

    public int getHudX(Text text){
        MinecraftClient client = MinecraftClient.getInstance();

        String location = this.config.getHudConfig().getLocation();
        return switch (location) {
            case "top-left", "bottom-left" -> 10;
            case "top-right", "bottom-right" -> (MinecraftClient.getInstance().getWindow().getScaledWidth()) - 10 - client.textRenderer.getWidth(text);
            default -> 10;
        };
    }
    public int getHudY(){
        String location = this.config.getHudConfig().getLocation();
        return switch (location) {
            case "top-left", "top-right" -> 10;
            case "bottom-left", "bottom-right" -> (MinecraftClient.getInstance().getWindow().getScaledHeight()) - 50;
            default -> 10;
        };
    }

    private void clientTickEvent(MinecraftClient mc) {
        if (mc.player == null || mc.world == null) {
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

    private void handleActiveHolding(MinecraftClient mc, Holding key) {
        assert mc.player != null;
        if (!key.isActive()) {
            return;
        }

        if (key.isSpamming()) {
            // How to handle the click if it's done by spamming
            if (key.getSpeed() > 0) {
                if (key.getTimeout() <= 1) {
                    // Press the button twice by toggling 1 and 0
                    key.getKey().setPressed(key.getTimeout() == 1);

                    if (key.getKey().isPressed()) {
                        this.attemptMobAttack(mc, key);
                    }

                    if (key.getTimeout() <= 0) {
                        key.resetTimeout();
                    }
                }
                key.decreaseTimeout();
            } else {
                // Handle the click if it's done normally
                key.getKey().setPressed(!key.getKey().isPressed());
                if (key.getKey().isPressed()) {
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
                if (key.getKey().isPressed()) {
                    key.getKey().setPressed(false);
                }
                return;
            }

            if (mc.player.getAttackCooldownProgress(0) == 1.0F) {
                key.getKey().setPressed(true);
                this.attemptMobAttack(mc, key);
            } else {
                key.getKey().setPressed(false);
            }
        } else {
            // Hold the click
            key.getKey().setPressed(true);
        }
    }

    private void attemptMobAttack(MinecraftClient mc, Holding key) {
        // Don't attack on a right click
        if (key.getKey() != leftHolding.getKey()) {
            return;
        }

        HitResult rayTrace = mc.crosshairTarget;
        if (rayTrace instanceof EntityHitResult && mc.interactionManager != null) {
            if(!(config.getLeftClick().isRespectShield() && isShielding(mc.player))) {
                mc.interactionManager.attackEntity(mc.player, ((EntityHitResult) rayTrace).getEntity());
                if (mc.player != null) {
                    mc.player.swingHand(Hand.MAIN_HAND);
                }
            }
        }
    }

    private boolean isShielding(ClientPlayerEntity player) {
        if (player.isUsingItem()) {
            return player.getActiveItem().getItem() instanceof ShieldItem;
        }
        return false;
    }

    private boolean isPlayerLookingAtMob(MinecraftClient mc) {
        HitResult rayTrace = mc.crosshairTarget;
        return rayTrace instanceof EntityHitResult && ((EntityHitResult) rayTrace).getEntity() instanceof LivingEntity livingEntity && livingEntity.isAlive() && livingEntity.isAttackable();
    }

    private void keyInputEvent(MinecraftClient mc) {
        assert mc.player != null;

        while (toggleHolding.wasPressed()) {
            this.isActive = !this.isActive;
            mc.player.sendMessage(
                    (this.isActive ? Language.MSG_HOLDING_KEYS : Language.MSG_RELEASED_KEYS)
                    .getText()
                    .formatted(this.isActive ? Formatting.GREEN : Formatting.RED),
                    true
                    );

            if (!this.isActive) {
                if(leftHolding.isActive()) leftHolding.getKey().setPressed(false);
                if(rightHolding.isActive()) rightHolding.getKey().setPressed(false);
                if(jumpHolding.isActive()) jumpHolding.getKey().setPressed(false);
            }
        }

        while (rightClickToggle.wasPressed()) {
            mc.setScreen(new OptionsScreen());
        }
    }
}
