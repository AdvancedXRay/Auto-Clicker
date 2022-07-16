package pro.mikey.autoclicker;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public enum Language {
    HUD_HOLDING("autoclicker-fabric.hud.holding"),
    MSG_HOLDING_KEYS("autoclicker-fabric.msg.holding-keys"),
    MSG_RELEASED_KEYS("autoclicker-fabric.msg.released-keys");

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
