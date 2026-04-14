package pro.mikey.autoclicker.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import pro.mikey.autoclicker.AutoClicker;

public final class AutoClickerFabricClient implements ClientModInitializer {
    AutoClicker autoClicker = new AutoClicker();

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(autoClicker::clientTickEvent);

        KeyMappingHelper.registerKeyMapping(AutoClicker.toggleHolding);
        KeyMappingHelper.registerKeyMapping(AutoClicker.openConfig);

        ClientLifecycleEvents.CLIENT_STARTED.register(autoClicker::clientReady);

        HudElementRegistry.addFirst(Identifier.fromNamespaceAndPath(AutoClicker.MOD_ID, "overlay"), new OverlayRenderLayer());
    }

    private class OverlayRenderLayer implements HudElement {
        @Override
        public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
            autoClicker.renderGameOverlayEvent(graphics, deltaTracker);
        }
    }
}
