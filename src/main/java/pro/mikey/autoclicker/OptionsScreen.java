package pro.mikey.autoclicker;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.TranslatableText;

public class OptionsScreen extends Screen {
    private final TranslatableText spammingHelpingText;
    private OptionsSliderWidget leftSpeedSlider;
    private OptionsSliderWidget rightSpeedSlider;

    protected OptionsScreen() {
        super(LiteralText.EMPTY);
        this.spammingHelpingText = new TranslatableText("autoclicker-fabric.gui.help.spam-speed");
    }

    @Override
    protected void init() {
        int x = this.width / 2, y = this.height / 2;

        this.addButton(
                new TooltipButton(x - 135, y - 44, 130, 20, Language.GUI_ACTIVE.getText(AutoClicker.leftHolding.isActive()), button -> {
                    AutoClicker.leftHolding.setActive(!AutoClicker.leftHolding.isActive());
                    button.setMessage(Language.GUI_ACTIVE.getText(AutoClicker.leftHolding.isActive()));
                    AutoClicker.getInstance().saveConfig();
                }, this::toolTip, "autoclicker-fabric.gui.help.active"));

        this.addButton(
                new TooltipButton(x + 5, y - 44, 130, 20, Language.GUI_ACTIVE.getText(AutoClicker.rightHolding.isActive()), button -> {
                    AutoClicker.rightHolding.setActive(!AutoClicker.rightHolding.isActive());
                    button.setMessage(Language.GUI_ACTIVE.getText(AutoClicker.rightHolding.isActive()));
                    AutoClicker.getInstance().saveConfig();
                }, this::toolTip, "autoclicker-fabric.gui.help.active"));

        this.addButton(
                new TooltipButton(x - 135, y - 22, 130, 20, Language.GUI_SPAMMING.getText(AutoClicker.leftHolding.isSpamming()), button -> {
                    AutoClicker.leftHolding.setSpamming(!AutoClicker.leftHolding.isSpamming());
                    button.setMessage(Language.GUI_SPAMMING.getText(AutoClicker.leftHolding.isSpamming()));
                    AutoClicker.getInstance().saveConfig();
                }, this::toolTip, "autoclicker-fabric.gui.help.spamming"));

        this.addButton(
                new TooltipButton(x + 5, y - 22, 130, 20, Language.GUI_SPAMMING.getText(AutoClicker.rightHolding.isSpamming()), button -> {
                    AutoClicker.rightHolding.setSpamming(!AutoClicker.rightHolding.isSpamming());
                    button.setMessage(Language.GUI_SPAMMING.getText(AutoClicker.rightHolding.isSpamming()));
                    AutoClicker.getInstance().saveConfig();
                }, this::toolTip, "autoclicker-fabric.gui.help.spamming"));

        this.addButton(this.leftSpeedSlider = new OptionsSliderWidget(x - 135, y, 130, 20, Language.GUI_SPEED.getText(), AutoClicker.leftHolding.getSpeed() / 50f, value -> {
            AutoClicker.leftHolding.setSpeed(value);
            AutoClicker.getInstance().saveConfig();
        }));
        this.addButton(this.rightSpeedSlider = new OptionsSliderWidget(x + 5, y, 130, 20, Language.GUI_SPEED.getText(), AutoClicker.rightHolding.getSpeed() / 50f, value -> {
            AutoClicker.rightHolding.setSpeed(value);
            AutoClicker.getInstance().saveConfig();
        }));

        this.addButton(
                new TooltipButton(x - 135, y + 22, 130, 20, Language.GUI_RESPECT_COOLDOWN.getText(AutoClicker.leftHolding.isRespectCooldown()), button -> {
                    AutoClicker.leftHolding.setRespectCooldown(!AutoClicker.leftHolding.isRespectCooldown());
                    button.setMessage(Language.GUI_RESPECT_COOLDOWN.getText(AutoClicker.leftHolding.isRespectCooldown()));
                    AutoClicker.getInstance().saveConfig();
                }, this::toolTip, "autoclicker-fabric.gui.help.cooldown"));

        this.addButton(
                new TooltipButton(x - 135, y + 44, 130, 20, Language.GUI_MOB_MODE.getText(AutoClicker.leftHolding.isMobMode()), button -> {
                    AutoClicker.leftHolding.setMobMode(!AutoClicker.leftHolding.isMobMode());
                    button.setMessage(Language.GUI_MOB_MODE.getText(AutoClicker.leftHolding.isMobMode()));
                    AutoClicker.getInstance().saveConfig();
                }, this::toolTip, "autoclicker-fabric.gui.help.mob-mode"));
    }

    private void toolTip(ButtonWidget button, MatrixStack matrixStack, int i, int i1) {
        this.renderHelpingTip(matrixStack, ((TooltipButton) button).tooltipCache);
    }

    private void renderHelpingTip(MatrixStack stack, TranslatableText text) {
        int x = this.width / 2, y = this.height / 2;

        this.renderOrderedTooltip(stack,
                MinecraftClient.getInstance().textRenderer.wrapLines(StringVisitable.plain(text.getString()), 270),
                x - 140,
                y + 100);
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

        if (this.leftSpeedSlider.isMouseOver(mouseX, mouseY) || this.rightSpeedSlider.isMouseOver(mouseX, mouseY)) {
            this.renderHelpingTip(matrices, this.spammingHelpingText);
        }
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
