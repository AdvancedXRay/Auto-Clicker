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
    return respectCooldown;
  }

  public void setRespectCooldown(boolean respectCooldown) {
    this.respectCooldown = respectCooldown;
  }

  public KeyBinding getKey() {
    return key;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  public boolean isSpamming() {
    return isSpamming;
  }

  public void setSpamming(boolean spamming) {
    isSpamming = spamming;
  }

  public int getSpeed() {
    return speed;
  }

  public void setSpeed(int speed) {
    this.speed = speed;
  }

  public int getTimeout() {
    return timeout;
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
}
