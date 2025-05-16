package pro.mikey.autoclicker.neoforge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;

import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import pro.mikey.autoclicker.AutoClicker;

@Mod(AutoClicker.MOD_ID)
@EventBusSubscriber(value = Dist.CLIENT, modid = AutoClicker.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class AutoClickerNeoForge {
    private static final AutoClicker autoClicker = new AutoClicker();
    public AutoClickerNeoForge() {
        autoClicker.onInitialize();
        NeoForge.EVENT_BUS.addListener(this::onClientTick);
        NeoForge.EVENT_BUS.addListener(this::onHudRender);
    }

    @SubscribeEvent
    public static void onClientStarted(FMLLoadCompleteEvent event){
        autoClicker.clientReady(Minecraft.getInstance());
    }

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event){
        event.register(AutoClicker.toggleHolding);
        event.register(AutoClicker.openConfig);
    }

    public void onClientTick(ClientTickEvent.Post event){
        autoClicker.clientTickEvent(Minecraft.getInstance());
    }

    public void onHudRender(RenderGuiEvent.Pre event){
        autoClicker.renderGameOverlayEvent(event.getGuiGraphics(), event.getPartialTick());
    }

    @SubscribeEvent
    public static void constructMod(FMLConstructModEvent event){
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> new IConfigScreenFactory() {
            @Override
            public @NotNull Screen createScreen(@NotNull ModContainer arg, @NotNull Screen arg2) {
                return (autoClicker.getConfigScreen());
            }
        });
    }
}
