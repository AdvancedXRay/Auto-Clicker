package pro.mikey.autoclicker;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum Language {
    HUD_HOLDING("autoclicker-fabric.hud.holding"),
    MSG_HOLDING_KEYS("autoclicker-fabric.msg.holding-keys"),
    MSG_RELEASED_KEYS("autoclicker-fabric.msg.released-keys"),
    GUI_SPEED("autoclicker-fabric.gui.speed"),
    GUI_ACTIVE("autoclicker-fabric.gui.active"),
    GUI_SPAMMING("autoclicker-fabric.gui.spamming"),
    GUI_ATTACK("autoclicker-fabric.gui.attack"),
    GUI_USE("autoclicker-fabric.gui.use"),
    GUI_JUMP("autoclicker-fabric.gui.jump"),
    GUI_RESPECT_COOLDOWN("autoclicker-fabric.gui.respect"),
    GUI_RESPECT_SHIELD("autoclicker-fabric.gui.shield"),
    GUI_MOB_MODE("autoclicker-fabric.gui.mob-mode"),
    GUI_HUD_ENABLED("autoclicker-fabric.gui.hud-enabled"),
    GUI_HUD_LOCATION("autoclicker-fabric.gui.hud-location");

    private final String key;
    MutableComponent text;

    Language(String langKey) {
        this.text = Component.translatable(langKey);
        this.key = langKey;
    }

    public MutableComponent getText() {
        return this.text;
    }

    public Component getText(Object... args) {
        return Component.translatable(this.key, args);
    }
}
