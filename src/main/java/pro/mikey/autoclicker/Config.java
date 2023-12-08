package pro.mikey.autoclicker;

public class Config {

    private final LeftMouseConfig leftClick;
    private final RightMouseConfig rightClick;
    private final JumpConfig jump;

    public Config(LeftMouseConfig leftClick, RightMouseConfig rightClick, JumpConfig jump) {
        this.leftClick = leftClick;
        this.rightClick = rightClick;
        this.jump = jump;
    }

    public LeftMouseConfig getLeftClick() {
        return this.leftClick;
    }

    public RightMouseConfig getRightClick() {
        return this.rightClick;
    }

    public JumpConfig getJump() {
        return this.jump;
    }

    @Override
    public String toString() {
        return "Config{" +
                "leftClick=" + this.leftClick +
                ", rightClick=" + this.rightClick +
                ", jump=" + this.jump +
                '}';
    }

    public static class LeftMouseConfig extends SharedConfig {
        private boolean respectCooldown;
        private boolean mobMode;

        public LeftMouseConfig(boolean active, boolean spamming, int mincpt, int maxcpt, boolean respectCooldown, boolean mobMode) {
            super(active, spamming, mincpt, maxcpt);

            this.respectCooldown = respectCooldown;
            this.mobMode = mobMode;
        }

        public boolean isRespectCooldown() {
            return this.respectCooldown;
        }

        public void setRespectCooldown(boolean respectCooldown) {
            this.respectCooldown = respectCooldown;
        }

        public boolean isMobMode() {
            return this.mobMode;
        }

        public void setMobMode(boolean mobMode) {
            this.mobMode = mobMode;
        }
    }

    public static class RightMouseConfig extends SharedConfig {
        public RightMouseConfig(boolean active, boolean spamming, int mincpt, int maxcpt) {
            super(active, spamming, mincpt, maxcpt);
        }
    }

    public static class JumpConfig extends SharedConfig {
        public JumpConfig(boolean active, boolean spamming, int mincpt, int maxcpt) {
            super(active, spamming, mincpt, maxcpt);
        }
    }

    public static class SharedConfig {
        private boolean active;
        private boolean spamming;
        private int mincpt;
        private int maxcpt;

        public SharedConfig(boolean active, boolean spamming, int mincpt,int maxcpt) {
            this.active = active;
            this.spamming = spamming;
            this.mincpt = mincpt;
            this.maxcpt = maxcpt;
        }

        public boolean isActive() {
            return this.active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public boolean isSpamming() {
            return this.spamming;
        }

        public void setSpamming(boolean spamming) {
            this.spamming = spamming;
        }

        public int getMaxcpt() {
            return maxcpt;
        }

        public void setMaxcpt(int maxcpt) {
            this.maxcpt = maxcpt;
        }

        public int getMincpt() {
            return mincpt;
        }

        public void setMincpt(int mincpt) {
            this.mincpt = mincpt;
        }

        @Override
        public String toString() {
            return "SharedConfig{" +
                    "active=" + this.active +
                    ", spamming=" + this.spamming +
                    ", maxcpt=" + this.maxcpt +
                    ", mincpt=" + this.mincpt +
                    '}';
        }
    }
}
