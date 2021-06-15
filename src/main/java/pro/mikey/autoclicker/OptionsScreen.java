package pro.mikey.autoclicker;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class OptionsScreen extends Screen {
    protected OptionsScreen() {
        super(LiteralText.EMPTY);
    }

    @Override
    protected void init() {
        int x = this.width / 2, y = this.height / 2;

        this.addButton(
                new ButtonWidget(
                        x - 135,
                        y - 44,
                        130,
                        20,
                        Language.GUI_ACTIVE.getText(AutoClicker.leftHolding.isActive()),
                        button -> {
                            AutoClicker.leftHolding.setActive(!AutoClicker.leftHolding.isActive());
                            button.setMessage(Language.GUI_ACTIVE.getText(AutoClicker.leftHolding.isActive()));
                        }));
        this.addButton(
                new ButtonWidget(
                        x + 5,
                        y - 44,
                        130,
                        20,
                        Language.GUI_ACTIVE.getText(AutoClicker.rightHolding.isActive()),
                        button -> {
                            AutoClicker.rightHolding.setActive(!AutoClicker.rightHolding.isActive());
                            button.setMessage(Language.GUI_ACTIVE.getText(AutoClicker.rightHolding.isActive()));
                        }));
        this.addButton(
                new ButtonWidget(
                        x - 135,
                        y - 22,
                        130,
                        20,
                        Language.GUI_SPAMMING.getText(AutoClicker.leftHolding.isSpamming()),
                        button -> {
                            AutoClicker.leftHolding.setSpamming(!AutoClicker.leftHolding.isSpamming());
                            button.setMessage(
                                    Language.GUI_SPAMMING.getText(AutoClicker.leftHolding.isSpamming()));
                        }));
        this.addButton(
                new ButtonWidget(
                        x + 5,
                        y - 22,
                        130,
                        20,
                        Language.GUI_SPAMMING.getText(AutoClicker.rightHolding.isSpamming()),
                        button -> {
                            AutoClicker.rightHolding.setSpamming(!AutoClicker.rightHolding.isSpamming());
                            button.setMessage(
                                    Language.GUI_SPAMMING.getText(AutoClicker.rightHolding.isSpamming()));
                        }));
        this.addButton(
                new OptionsSliderWidget(
                        x - 135,
                        y,
                        130,
                        20,
                        Language.GUI_SPEED.getText(),
                        AutoClicker.leftHolding.getSpeed() / 50f,
                        value -> AutoClicker.leftHolding.setSpeed(value)));
        this.addButton(
                new OptionsSliderWidget(
                        x + 5,
                        y,
                        130,
                        20,
                        Language.GUI_SPEED.getText(),
                        AutoClicker.rightHolding.getSpeed() / 50f,
                        value -> AutoClicker.rightHolding.setSpeed(value)));
        this.addButton(
                new ButtonWidget(
                        x - 135,
                        y + 22,
                        130,
                        20,
                        Language.GUI_RESPECT_COOLDOWN.getText(AutoClicker.leftHolding.isRespectCooldown()),
                        button -> {
                            AutoClicker.leftHolding.setRespectCooldown(
                                    !AutoClicker.leftHolding.isRespectCooldown());
                            button.setMessage(
                                    Language.GUI_RESPECT_COOLDOWN.getText(
                                            AutoClicker.leftHolding.isRespectCooldown()));
                        }));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);

        this.textRenderer.drawWithShadow(
                matrices,
                Language.GUI_ATTACK.getText(),
                this.width / 2f - 135,
                this.height / 2f - 56,
                0xFFFFFF);

        this.textRenderer.drawWithShadow(
                matrices, Language.GUI_USE.getText(), this.width / 2f + 5, this.height / 2f - 56, 0xFFFFFF);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == AutoClicker.rightClickToggle.getDefaultKey().getCode()) {
            this.onClose();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
