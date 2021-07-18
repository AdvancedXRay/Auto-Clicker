package pro.mikey.autoclicker;

import net.minecraft.text.TranslatableText;

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
    GUI_MOB_MODE("autoclicker-fabric.gui.mob-mode");

    private final String key;
    TranslatableText text;

    Language(String langKey) {
        this.text = new TranslatableText(langKey);
        this.key = langKey;
    }

    public TranslatableText getText() {
        return this.text;
    }

    public TranslatableText getText(Object... args) {
        return new TranslatableText(this.key, args);
    }
}
