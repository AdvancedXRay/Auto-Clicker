package pro.mikey.autoclicker;

import net.minecraft.client.option.KeyBinding;

import java.security.PublicKey;
import java.util.Random;

public class Holding {
    private final KeyBinding key;
    Config.SharedConfig config;
    private int timeout;

    public static final Random rng = new Random();

    public Holding(KeyBinding key, Config.SharedConfig config) {
        this.config = config;
        this.key = key;
        this.timeout = createRandomTimeout();
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

    public int getMinSpeed(){
        return this.config.getMincpt();
    }

    public int getMaxSpeed(){
        return this.config.getMaxcpt();
    }

    public void setMinSpeed(int speed) {
        this.config.setMincpt(speed);
    }
    public void setMaxSpeed(int speed) {
        this.config.setMaxcpt(speed);
    }


    public int getTimeout() {
        return this.timeout;
    }

    public void resetTimeout() {
        this.timeout =  createRandomTimeout();
    }

    public int createRandomTimeout() {
        return this.config.getMincpt() + (int)((float)(this.config.getMaxcpt()-this.config.getMincpt())*rng.nextFloat());
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

        public void setMobMode(boolean mobMode) {
            ((Config.LeftMouseConfig) this.config).setMobMode(mobMode);
        }
    }
}
