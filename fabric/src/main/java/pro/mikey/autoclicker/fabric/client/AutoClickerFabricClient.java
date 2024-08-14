package pro.mikey.autoclicker.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import pro.mikey.autoclicker.AutoClicker;

public final class AutoClickerFabricClient implements ClientModInitializer {
    AutoClicker autoClicker = new AutoClicker();
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(autoClicker::clientTickEvent);

        KeyBindingHelper.registerKeyBinding(AutoClicker.toggleHolding);
        KeyBindingHelper.registerKeyBinding(AutoClicker.openConfig);

        ClientLifecycleEvents.CLIENT_STARTED.register(autoClicker::clientReady);
        HudRenderCallback.EVENT.register(autoClicker::RenderGameOverlayEvent);
    }
}
