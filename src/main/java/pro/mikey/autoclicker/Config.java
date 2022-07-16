package pro.mikey.autoclicker;

public class Config {
    private final LeftMouseConfig leftClick;
    private final RightMouseConfig rightClick;
    private String cropsList;
    private String blacklist;
    private int cropInertia;

    public Config(LeftMouseConfig leftClick, RightMouseConfig rightClick, String cropsList, String blacklist, int cropInertia) {
        this.leftClick = leftClick;
        this.rightClick = rightClick;
        this.cropsList = cropsList;
        this.blacklist = blacklist;
        this.cropInertia = cropInertia;
    }

    public LeftMouseConfig getLeftClick() {
        return this.leftClick;
    }

    public RightMouseConfig getRightClick() {
        return this.rightClick;
    }

    public String getCropsList() {
        return this.cropsList;
    }

    public void setCropsList(String value) {
        cropsList = value;
    }

    public String getBlacklist() {
        return this.blacklist;
    }

    public void setBlacklist(String value) {
        blacklist = value;
    }

    public int getCropInertia() {
        return this.cropInertia;
    }

    public void setCropInertia(int value) {
        cropInertia = value;
    }

    @Override
    public String toString() {
        return "Config{" +
                "leftClick=" + this.leftClick +
                ", rightClick=" + this.rightClick +
                ", blacklist=\"" + this.blacklist + "\"" +
                ", cropsList=\"" + this.cropsList + "\"" +
                ", cropInertia=" + this.cropInertia +
                '}';
    }

    public static class LeftMouseConfig extends SharedConfig {
        private boolean respectCooldown;
        private boolean mobMode;
        private boolean cropMode;

        public LeftMouseConfig(boolean active, boolean spamming, int cpt, boolean respectCooldown, boolean mobMode, boolean cropMode) {
            super(active, spamming, cpt);

            this.respectCooldown = respectCooldown;
            this.mobMode = mobMode;
            this.cropMode = cropMode;
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

        public boolean isCropMode() {
            return this.cropMode;
        }

        public void setMobMode(boolean mobMode) {
            this.mobMode = mobMode;
        }

        public void setCropMode(boolean cropMode) {
            this.cropMode = cropMode;
        }
    }

    public static class RightMouseConfig extends SharedConfig {
        public RightMouseConfig(boolean active, boolean spamming, int cpt) {
            super(active, spamming, cpt);
        }
    }

    public static class SharedConfig {
        private boolean active;
        private boolean spamming;
        private int cpt;

        public SharedConfig(boolean active, boolean spamming, int cpt) {
            this.active = active;
            this.spamming = spamming;
            this.cpt = cpt;
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

        public int getCpt() {
            return this.cpt;
        }

        public void setCpt(int cpt) {
            this.cpt = cpt;
        }

        @Override
        public String toString() {
            return "SharedConfig{" +
                    "active=" + this.active +
                    ", spamming=" + this.spamming +
                    ", cpt=" + this.cpt +
                    '}';
        }
    }
}
