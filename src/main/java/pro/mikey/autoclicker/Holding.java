package pro.mikey.autoclicker;

import net.minecraft.client.option.KeyBinding;

public class Holding {
    private final KeyBinding key;
    ModConfig.SharedConfig config;
    private int timeout;

    public Holding(KeyBinding key, ModConfig.SharedConfig config) {
        this.config = config;
        this.key = key;
        this.timeout = config.speed;
    }

    public boolean isRespectCooldown() {
        return false;
    }

    public KeyBinding getKey() {
        return this.key;
    }

    public boolean isActive() {
        return this.config.active;
    }

    public boolean isSpamming() {
        return this.config.spamming;
    }

    public int getSpeed() {
        return this.config.speed;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void resetTimeout() {
        this.timeout = this.config.speed;
    }

    public void decreaseTimeout() {
        if ((this.timeout - 1) < 0) {
            return;
        }

        this.timeout -= 1;
    }

    public static class AttackHolding extends Holding {

        public AttackHolding(KeyBinding key, ModConfig.LeftMouseConfig config) {
            super(key, config.shared);
        }

        public boolean isMobMode() {
            return AutoClicker.CONFIG.leftClick.mobMode;
        }

        public boolean isCropMode() {
            return AutoClicker.CONFIG.leftClick.cropMode;
        }

        public boolean isRespectCooldown() {
            return AutoClicker.CONFIG.leftClick.respectCooldown;
        }
    }
}
