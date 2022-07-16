package pro.mikey.autoclicker;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.StringVisitable;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.gui.widget.TextFieldWidget;

public class OptionsScreen extends Screen {
    private final Text blacklistHelpingText;
    private final Text cropsListHelpingText;
    private final Text spammingHelpingText;
    private final Text inertiaHelpingText;
    private OptionsSliderWidget leftSpeedSlider;
    private OptionsSliderWidget rightSpeedSlider;
    private OptionsSliderWidget cropInertiaSlider;
    private TextFieldWidget cropsList;
    private TextFieldWidget blacklist;

    protected OptionsScreen() {
        super(Text.empty());
        this.blacklistHelpingText = Text.translatable("autoclicker-fabric.gui.help.blacklist");
        this.cropsListHelpingText = Text.translatable("autoclicker-fabric.gui.help.crops-list");
        this.spammingHelpingText  = Text.translatable("autoclicker-fabric.gui.help.spam-speed");
        this.inertiaHelpingText   = Text.translatable("autoclicker-fabric.gui.help.inertia");
    }

    @Override
    protected void init() {
        int x = this.width / 2, y = this.height / 2;

        this.addDrawableChild(
                new TooltipButton(x - 135, y - 44, 130, 20, Language.GUI_ACTIVE.getText(AutoClicker.leftHolding.isActive()), button -> {
                    AutoClicker.leftHolding.setActive(!AutoClicker.leftHolding.isActive());
                    button.setMessage(Language.GUI_ACTIVE.getText(AutoClicker.leftHolding.isActive()));
                    AutoClicker.getInstance().saveConfig();
                }, this::toolTip, "autoclicker-fabric.gui.help.active"));

        this.addDrawableChild(
                new TooltipButton(x + 5, y - 44, 130, 20, Language.GUI_ACTIVE.getText(AutoClicker.rightHolding.isActive()), button -> {
                    AutoClicker.rightHolding.setActive(!AutoClicker.rightHolding.isActive());
                    button.setMessage(Language.GUI_ACTIVE.getText(AutoClicker.rightHolding.isActive()));
                    AutoClicker.getInstance().saveConfig();
                }, this::toolTip, "autoclicker-fabric.gui.help.active"));

        this.addDrawableChild(
                new TooltipButton(x - 135, y - 22, 130, 20, Language.GUI_SPAMMING.getText(AutoClicker.leftHolding.isSpamming()), button -> {
                    AutoClicker.leftHolding.setSpamming(!AutoClicker.leftHolding.isSpamming());
                    button.setMessage(Language.GUI_SPAMMING.getText(AutoClicker.leftHolding.isSpamming()));
                    AutoClicker.getInstance().saveConfig();
                }, this::toolTip, "autoclicker-fabric.gui.help.spamming"));

        this.addDrawableChild(
                new TooltipButton(x + 5, y - 22, 130, 20, Language.GUI_SPAMMING.getText(AutoClicker.rightHolding.isSpamming()), button -> {
                    AutoClicker.rightHolding.setSpamming(!AutoClicker.rightHolding.isSpamming());
                    button.setMessage(Language.GUI_SPAMMING.getText(AutoClicker.rightHolding.isSpamming()));
                    AutoClicker.getInstance().saveConfig();
                }, this::toolTip, "autoclicker-fabric.gui.help.spamming"));

        this.addDrawableChild(this.leftSpeedSlider = new OptionsSliderWidget(x - 135, y, 130, 20, Language.GUI_SPEED.getText(), AutoClicker.leftHolding.getSpeed() / 50f, value -> {
            AutoClicker.leftHolding.setSpeed(value);
            AutoClicker.getInstance().saveConfig();
        }));
        this.addDrawableChild(this.rightSpeedSlider = new OptionsSliderWidget(x + 5, y, 130, 20, Language.GUI_SPEED.getText(), AutoClicker.rightHolding.getSpeed() / 50f, value -> {
            AutoClicker.rightHolding.setSpeed(value);
            AutoClicker.getInstance().saveConfig();
        }));

        this.addDrawableChild(
                new TooltipButton(x - 135, y + 22, 130, 20, Language.GUI_RESPECT_COOLDOWN.getText(AutoClicker.leftHolding.isRespectCooldown()), button -> {
                    AutoClicker.leftHolding.setRespectCooldown(!AutoClicker.leftHolding.isRespectCooldown());
                    button.setMessage(Language.GUI_RESPECT_COOLDOWN.getText(AutoClicker.leftHolding.isRespectCooldown()));
                    AutoClicker.getInstance().saveConfig();
                }, this::toolTip, "autoclicker-fabric.gui.help.cooldown"));

        this.addDrawableChild(
                new TooltipButton(x - 135, y + 44, 130, 20, Language.GUI_MOB_MODE.getText(AutoClicker.leftHolding.isMobMode()), button -> {
                    AutoClicker.leftHolding.setMobMode(!AutoClicker.leftHolding.isMobMode());
                    button.setMessage(Language.GUI_MOB_MODE.getText(AutoClicker.leftHolding.isMobMode()));
                    AutoClicker.getInstance().saveConfig();
                }, this::toolTip, "autoclicker-fabric.gui.help.mob-mode"));

        this.addDrawableChild(
                new TooltipButton(x - 135, y + 66, 130, 20, Language.GUI_CROP_MODE.getText(AutoClicker.leftHolding.isCropMode()), button -> {
                    AutoClicker.leftHolding.setCropMode(!AutoClicker.leftHolding.isCropMode());
                    button.setMessage(Language.GUI_CROP_MODE.getText(AutoClicker.leftHolding.isCropMode()));
                    AutoClicker.getInstance().saveConfig();
                }, this::toolTip, "autoclicker-fabric.gui.help.crop-mode"));

        this.addDrawableChild(this.cropInertiaSlider = new OptionsSliderWidget(x - 135, y + 88, 130, 20, Language.GUI_INERTIA.getText(), AutoClicker.getCropInertia(), value -> {
            AutoClicker.setCropInertia(value);
            AutoClicker.getInstance().saveConfig();
        }));

        this.addDrawableChild(
                blacklist = new OptionsTextFieldWidget(x + 5, y + 44, 130, 20, AutoClicker.getBlacklist())
                );
        blacklist.setEditableColor(0xFF5555); // reddish

        this.addDrawableChild(
                cropsList = new OptionsTextFieldWidget(x + 5, y + 66, 130, 20, AutoClicker.getCropsList())
                );
    }

    private void toolTip(ButtonWidget button, MatrixStack matrixStack, int i, int i1) {
        this.renderHelpingTip(matrixStack, ((TooltipButton) button).tooltipCache);
    }

    private void renderHelpingTip(MatrixStack stack, Text text) {
        int x = this.width / 2, y = this.height / 2;

        this.renderOrderedTooltip(stack,
                MinecraftClient.getInstance().textRenderer.wrapLines(StringVisitable.plain(text.getString()), 270),
                x - 140,
                y + 128);
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

        if (this.blacklist.isMouseOver(mouseX, mouseY)) {
            this.renderHelpingTip(matrices, this.blacklistHelpingText);
        } else if (this.cropsList.isMouseOver(mouseX, mouseY)) {
            this.renderHelpingTip(matrices, this.cropsListHelpingText);
        } else if (this.leftSpeedSlider.isMouseOver(mouseX, mouseY) || this.rightSpeedSlider.isMouseOver(mouseX, mouseY)) {
            this.renderHelpingTip(matrices, this.spammingHelpingText);
        } else if (this.cropInertiaSlider.isMouseOver(mouseX, mouseY)) {
            this.renderHelpingTip(matrices, this.inertiaHelpingText);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (
                !cropsList.isFocused() &&
                !blacklist.isFocused() &&
                keyCode == AutoClicker.rightClickToggle.getDefaultKey().getCode()
           ) {
            onClose();
            this.close();
            return true;
        }

        if ( keyCode == GLFW.GLFW_KEY_ESCAPE ) {
            onClose();
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void onClose() {
        if ( !AutoClicker.getCropsList().equals( cropsList.getText() ) ) {
            AutoClicker.setCropsList( cropsList.getText() );
            AutoClicker.getInstance().saveConfig();
        }
        if ( !AutoClicker.getBlacklist().equals( blacklist.getText() ) ) {
            AutoClicker.setBlacklist( blacklist.getText() );
            AutoClicker.getInstance().saveConfig();
        }
    }
}
