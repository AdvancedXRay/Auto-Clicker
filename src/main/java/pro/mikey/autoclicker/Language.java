package pro.mikey.autoclicker;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public enum Language {
    HUD_HOLDING("autoclicker-fabric.hud.holding"),
    MSG_HOLDING_KEYS("autoclicker-fabric.msg.holding-keys"),
    MSG_RELEASED_KEYS("autoclicker-fabric.msg.released-keys"),
    GUI_SPEED("autoclicker-fabric.gui.speed"),
    GUI_ACTIVE("autoclicker-fabric.gui.active"),
    GUI_SPAMMING("autoclicker-fabric.gui.spamming"),
    GUI_ATTACK("autoclicker-fabric.gui.attack"),
    GUI_USE("autoclicker-fabric.gui.use"),
    GUI_RESPECT_COOLDOWN("autoclicker-fabric.gui.respect"),
    GUI_MOB_MODE("autoclicker-fabric.gui.mob-mode"),
    GUI_CROP_MODE("autoclicker-fabric.gui.crop-mode"),
    GUI_INERTIA("autoclicker-fabric.gui.inertia");

    private final String key;
    MutableText text;

    Language(String langKey) {
        this.text = Text.translatable(langKey);
        this.key = langKey;
    }

    public MutableText getText() {
        return this.text;
    }

    public Text getText(Object... args) {
        return Text.translatable(this.key, args);
    }
}
