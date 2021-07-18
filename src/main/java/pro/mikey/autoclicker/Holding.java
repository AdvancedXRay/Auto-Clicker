package pro.mikey.autoclicker;


import net.minecraft.client.option.KeyBinding;

public class Holding {
    private final KeyBinding key;
    private boolean respectCooldown;
    private boolean isActive;
    private boolean isSpamming;
    private int speed;
    private int timeout;

    public Holding(
            KeyBinding key, boolean isActive, boolean isSpamming, int speed, boolean respectCooldown) {
        this.key = key;
        this.isActive = isActive;
        this.isSpamming = isSpamming;
        this.speed = speed;
        this.timeout = speed;
        this.respectCooldown = respectCooldown;
    }

    public boolean isRespectCooldown() {
        return this.respectCooldown;
    }

    public void setRespectCooldown(boolean respectCooldown) {
        this.respectCooldown = respectCooldown;
    }

    public KeyBinding getKey() {
        return this.key;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean isSpamming() {
        return this.isSpamming;
    }

    public void setSpamming(boolean spamming) {
        this.isSpamming = spamming;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void resetTimeout() {
        this.timeout = this.speed;
    }

    public void decreaseTimeout() {
        if ((this.timeout - 1) < 0) {
            return;
        }

        this.timeout -= 1;
    }

    public static class AttachHolding extends Holding {
        private boolean mobMode;

        public AttachHolding(KeyBinding key, boolean isActive, boolean isSpamming, int speed, boolean respectCooldown, boolean mobMode) {
            super(key, isActive, isSpamming, speed, respectCooldown);
            this.mobMode = mobMode;
        }

        public boolean isMobMode() {
            return this.mobMode;
        }

        public void setMobMode(boolean mobMode) {
            this.mobMode = mobMode;
        }
    }
}
