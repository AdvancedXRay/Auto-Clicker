package pro.mikey.autoclicker;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;

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
    private static AutoClicker INSTANCE;
    private boolean isActive = false;
    private boolean autoClicked = false;
    private HashSet<String> parsedCropsSet = new HashSet<String>();
    private HashSet<String> parsedBlacklist = new HashSet<String>();
    private static int inertia = 0;
    private Config config = new Config(
            new Config.LeftMouseConfig(false, false, 0, false, false, false),
            new Config.RightMouseConfig(false, false, 0),
            DEFAULT_CROPS_LIST,
            DEFAULT_BLACKLIST,
            DEFAULT_CROP_INERTIA
    );

    public static final String DEFAULT_CROPS_LIST = "bamboo, beetroot, brown_mushroom, cactus, carrots, carved_pumpkin, cocoa, gravel, melon, nether_wart, potatoes, red_mushroom, sand, sugar_cane, sweet_berry_bush, wheat";
    public static final String DEFAULT_BLACKLIST  = "armor_stand, player, villager";
    public static final int DEFAULT_CROP_INERTIA = 500; // milliseconds
    
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
                if (config != null) {
                    this.config = config;
                }
            } catch (JsonIOException | IOException e) {
                e.printStackTrace();
            }
        }

        leftHolding = new Holding.AttackHolding(client.options.attackKey, this.config.getLeftClick());
        rightHolding = new Holding(client.options.useKey, this.config.getRightClick());
        parsedCropsSet = parseList(getCropsList());
        parsedBlacklist = parseList(getBlacklist());
    }

    public static String getCropsList() {
        String cropsList = getInstance().config.getCropsList();
        if ( cropsList == null || cropsList.trim() == "" )
            return DEFAULT_CROPS_LIST;
        else
            return cropsList.trim();
    }

    public static void setCropsList(String value) {
        getInstance().config.setCropsList(value);
        getInstance().parsedCropsSet = parseList(getCropsList());
    }

    public static String getBlacklist() {
        String blacklist = getInstance().config.getBlacklist();
        if ( blacklist == null || blacklist.trim() == "" )
            return DEFAULT_BLACKLIST;
        else
            return blacklist.trim();
    }

    public static void setBlacklist(String value) {
        getInstance().config.setBlacklist(value);
        getInstance().parsedBlacklist = parseList(getBlacklist());
    }

    private static HashSet<String> parseList(String list) {
        String[] strParts = list.split("[\\s,]+");
        return new HashSet<String>( Arrays.asList(strParts) );
    }

    public static void setCropInertia(int value) {
        getInstance().config.setCropInertia(value);
    }

    public static int getCropInertia() {
        return getInstance().config.getCropInertia();
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

    private void RenderGameOverlayEvent(MatrixStack matrixStack, float delta) {
        if ((!leftHolding.isActive() && !rightHolding.isActive()) || !this.isActive) {
            return;
        }

        int y = 10;
        if (leftHolding.isActive()) {
            MinecraftClient.getInstance().textRenderer.drawWithShadow(matrixStack, Language.HUD_HOLDING.getText(I18n.translate(leftHolding.getKey().getTranslationKey())), 10, y, 0xffffff);
            y += 15;
        }

        if (rightHolding.isActive()) {
            MinecraftClient.getInstance().textRenderer.drawWithShadow(matrixStack, Language.HUD_HOLDING.getText(I18n.translate(rightHolding.getKey().getTranslationKey())), 10, y, 0xffffff);
        }
    }

    private void clientTickEvent(MinecraftClient mc) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        if (this.isActive) {
            if (leftHolding.isActive()) {
                this.handleActiveHolding(mc, leftHolding);
            }

            if (rightHolding.isActive()) {
                this.handleActiveHolding(mc, rightHolding);
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

        if ( inertia > 0 ) {
            // Hold the click and decrease inertia counter
            key.getKey().setPressed(true);
            inertia--;
        } else if ( key.isRespectCooldown() ) {
            boolean shouldRelease = true;

            if (((Holding.AttackHolding) key).isMobMode()) {
                String mob_key = this.getMobPlayerLookingAt(mc);
                if ( mob_key != null && !isBlacklisted(mob_key) )
                    shouldRelease = false;
            }

            if (((Holding.AttackHolding) key).isCropMode()) {
                String crop_key = this.getCropPlayerLookingAt(mc);
                if ( crop_key != null ) {
                    boolean cropInSight = parsedCropsSet.contains(crop_key) && !isBlacklisted(crop_key);
                    if ( cropInSight ) {
                        shouldRelease = false;
                        inertia = getCropInertia();
                    }
                }
            }

            // Don't do anything if they're not looking at something
            if (key instanceof Holding.AttackHolding && shouldRelease) {
                // Release the key only if it was pressed by us
                if (autoClicked && key.getKey().isPressed()) {
                    autoClicked = false;
                    key.getKey().setPressed(false);
                }
                return;
            }

            if (mc.player.getAttackCooldownProgress(0) == 1.0F) {
                autoClicked = true;
                key.getKey().setPressed(true);
                this.attemptMobAttack(mc, key);
            } else {
                autoClicked = false;
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
            mc.interactionManager.attackEntity(mc.player, ((EntityHitResult) rayTrace).getEntity());
        }
    }

    private String getMobPlayerLookingAt(MinecraftClient mc) {
        HitResult rayTrace = mc.crosshairTarget;
        if ( rayTrace instanceof EntityHitResult ) {
            Entity entity = ((EntityHitResult) rayTrace).getEntity();
            if ( entity instanceof LivingEntity ) {
                Identifier id = EntityType.getId(entity.getType());
                if ( id != null ) {
                    return ( id.getNamespace() == "minecraft" ) ?
                        id.getPath() : // no 'minecraft:' prefix on standard blocks
                        id.toString(); // full id on nonstandard
                }
            }
        }
        return null;
    }

    private boolean isBlacklisted(String key) {
        return parsedBlacklist.contains(key);
    }

    private String getCropPlayerLookingAt(MinecraftClient mc) {
        HitResult rayTrace = mc.crosshairTarget;
        if(rayTrace instanceof BlockHitResult){
            BlockPos pos = ((BlockHitResult) rayTrace).getBlockPos();
            BlockState state = mc.world.getBlockState(pos);
            Block block = state.getBlock();
            Identifier id = Registry.BLOCK.getId(block);
            if(id != null) {
                return ( id.getNamespace() == "minecraft" ) ?
                    id.getPath() : // no 'minecraft:' prefix on standard blocks
                    id.toString(); // full id on nonstandard
            }
        }
        return null;
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
                leftHolding.getKey().setPressed(false);
                rightHolding.getKey().setPressed(false);
            }
        }

        while (rightClickToggle.wasPressed()) {
            mc.setScreen(new OptionsScreen());
        }
    }
}
