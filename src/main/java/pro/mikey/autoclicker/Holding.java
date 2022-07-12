package pro.mikey.autoclicker;

import net.minecraft.client.option.KeyBinding;

public class Holding {
    private final KeyBinding key;
    Config.SharedConfig config;
    private int timeout;

    public Holding(KeyBinding key, Config.SharedConfig config) {
        this.config = config;
        this.key = key;
        this.timeout = config.getCpt();
    }

    public boolean isRespectCooldown() {
        return this.config instanceof Config.LeftMouseConfig && ((Config.LeftMouseConfig) this.config).isRespectCooldown();
    }

    public void setRespectCooldown(boolean respectCooldown) {
        if (!(this.config instanceof Config.LeftMouseConfig)) {
            return;
        }

        ((Config.LeftMouseConfig) this.config).setRespectCooldown(respectCooldown);
    }

    public KeyBinding getKey() {
        return this.key;
    }

    public boolean isActive() {
        return this.config.isActive();
    }

    public void setActive(boolean active) {
        this.config.setActive(active);
    }

    public boolean isSpamming() {
        return this.config.isSpamming();
    }

    public void setSpamming(boolean spamming) {
        this.config.setSpamming(spamming);
    }

    public int getSpeed() {
        return this.config.getCpt();
    }

    public void setSpeed(int speed) {
        this.config.setCpt(speed);
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void resetTimeout() {
        this.timeout = this.config.getCpt();
    }

    public void decreaseTimeout() {
        if ((this.timeout - 1) < 0) {
            return;
        }

        this.timeout -= 1;
    }

    public static class AttackHolding extends Holding {

        public AttackHolding(KeyBinding key, Config.LeftMouseConfig config) {
            super(key, config);
        }

        public boolean isMobMode() {
            return ((Config.LeftMouseConfig) this.config).isMobMode();
        }

        public boolean isCropMode() {
            return ((Config.LeftMouseConfig) this.config).isCropMode();
        }

        public void setMobMode(boolean mobMode) {
            ((Config.LeftMouseConfig) this.config).setMobMode(mobMode);
        }

        public void setCropMode(boolean mobMode) {
            ((Config.LeftMouseConfig) this.config).setCropMode(mobMode);
        }
    }
}
