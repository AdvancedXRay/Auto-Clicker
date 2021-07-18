package pro.mikey.autoclicker;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class AutoClicker implements ModInitializer {
    public static final String MOD_ID = "autoclicker-fabric";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final KeyBinding rightClickToggle =
            new KeyBinding("keybinding.open-gui", GLFW.GLFW_KEY_O, "category.autoclicker-fabric");
    private static final KeyBinding toggleHolding =
            new KeyBinding("keybinding.toggle-hold", GLFW.GLFW_KEY_I, "category.autoclicker-fabric");
    public static Holding leftHolding;
    public static Holding rightHolding;
    private boolean isActive = false;

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
        leftHolding = new Holding(client.options.keyAttack, false, false, 0, false);
        rightHolding = new Holding(client.options.keyUse, false, false, 0, false);
    }

    private void RenderGameOverlayEvent(MatrixStack matrixStack, float delta) {
        if ((!leftHolding.isActive() && !rightHolding.isActive()) || !this.isActive) {
            return;
        }

        int y = 10;
        if (leftHolding.isActive()) {
            MinecraftClient.getInstance()
                    .textRenderer
                    .drawWithShadow(
                            matrixStack,
                            Language.HUD_HOLDING.getText(
                                    I18n.translate(leftHolding.getKey().getTranslationKey())),
                            10,
                            y,
                            0xffffff);

            y += 15;
        }

        if (rightHolding.isActive()) {
            MinecraftClient.getInstance()
                    .textRenderer
                    .drawWithShadow(
                            matrixStack,
                            Language.HUD_HOLDING.getText(
                                    I18n.translate(rightHolding.getKey().getTranslationKey())),
                            10,
                            y,
                            0xffffff);
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
                    System.out.println("Act");
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
            mc.interactionManager.attackEntity(mc.player, ((EntityHitResult) rayTrace).getEntity());
        }
    }

    private void keyInputEvent(MinecraftClient mc) {
        assert mc.player != null;

        while (toggleHolding.wasPressed()) {
            this.isActive = !this.isActive;
            mc.player.sendMessage(
                    (this.isActive ? Language.MSG_HOLDING_KEYS : Language.MSG_RELEASED_KEYS)
                            .getText()
                            .formatted(this.isActive ? Formatting.GREEN : Formatting.RED),
                    true);

            if (!this.isActive) {
                leftHolding.getKey().setPressed(false);
                rightHolding.getKey().setPressed(false);
            }
        }

        while (rightClickToggle.wasPressed()) {
            mc.openScreen(new OptionsScreen());
        }
    }
}
