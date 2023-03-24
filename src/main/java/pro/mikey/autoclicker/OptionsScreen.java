package pro.mikey.autoclicker;

import java.util.HashMap;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.StringVisitable;

public class OptionsScreen extends Screen {
    private final HashMap<ButtonWidget, String> buttonTooltips = new HashMap<>();
    private final HashMap<OptionsSliderWidget, String> sliderTooltips = new HashMap<>();

    protected OptionsScreen() {
        super(Text.empty());
    }

    @Override
    protected void init() {
        int x = this.width / 2, y = this.height / 2;
        
        this.buttonTooltips.put(this.addDrawableChild(
            ButtonWidget.builder(
                Language.GUI_ACTIVE.getText(AutoClicker.leftHolding.isActive()), (button) -> {
                    AutoClicker.leftHolding.setActive(!AutoClicker.leftHolding.isActive());
                    button.setMessage(Language.GUI_ACTIVE.getText(AutoClicker.leftHolding.isActive()));
                    AutoClicker.getInstance().saveConfig();
            })
            .dimensions(x - 200, y - 44, 130, 20)
            .build()
        ), "autoclicker-fabric.gui.help.active");
        
        this.buttonTooltips.put(this.addDrawableChild(
            ButtonWidget.builder(
                Language.GUI_ACTIVE.getText(AutoClicker.rightHolding.isActive()), (button) -> {
                    AutoClicker.rightHolding.setActive(!AutoClicker.rightHolding.isActive());
                    button.setMessage(Language.GUI_ACTIVE.getText(AutoClicker.rightHolding.isActive()));
                    AutoClicker.getInstance().saveConfig();
            })
            .dimensions(x - 65, y - 44, 130, 20)
            .build()
        ), "autoclicker-fabric.gui.help.active");
        
        this.buttonTooltips.put(this.addDrawableChild(
            ButtonWidget.builder(
                Language.GUI_ACTIVE.getText(AutoClicker.jumpHolding.isActive()), (button) -> {
                    AutoClicker.jumpHolding.setActive(!AutoClicker.jumpHolding.isActive());
                    button.setMessage(Language.GUI_ACTIVE.getText(AutoClicker.jumpHolding.isActive()));
                    AutoClicker.getInstance().saveConfig();
            })
            .dimensions(x + 70 , y - 44, 130, 20)
            .build()
        ), "autoclicker-fabric.gui.help.active");
        
        this.buttonTooltips.put(this.addDrawableChild(
            ButtonWidget.builder(
                Language.GUI_onDelay.getText(AutoClicker.leftHolding.isOnDelay), (button) -> {
                    AutoClicker.leftHolding.setonDelay(!AutoClicker.leftHolding.isOnDelay);
                    button.setMessage(Language.GUI_onDelay.getText(AutoClicker.leftHolding.isOnDelay));
                    AutoClicker.getInstance().saveConfig();
            })
            .dimensions(x - 200, y - 22, 130, 20)
            .build()
        ), "autoclicker-fabric.gui.help.onDelay");
        
        this.buttonTooltips.put(this.addDrawableChild(
            ButtonWidget.builder(
                Language.GUI_onDelay.getText(AutoClicker.rightHolding.isOnDelay), (button) -> {
                    AutoClicker.rightHolding.setonDelay(!AutoClicker.rightHolding.isOnDelay);
                    button.setMessage(Language.GUI_onDelay.getText(AutoClicker.rightHolding.isOnDelay));
                    AutoClicker.getInstance().saveConfig();
            })
            .dimensions(x - 65, y - 22, 130, 20)
            .build()
        ), "autoclicker-fabric.gui.help.onDelay");
        
        this.buttonTooltips.put(this.addDrawableChild(
            ButtonWidget.builder(
                Language.GUI_onDelay.getText(AutoClicker.jumpHolding.isOnDelay), (button) -> {
                    AutoClicker.jumpHolding.setonDelay(!AutoClicker.jumpHolding.isOnDelay);
                    button.setMessage(Language.GUI_onDelay.getText(AutoClicker.jumpHolding.isOnDelay));
                    AutoClicker.getInstance().saveConfig();
            })
            .dimensions(x + 70, y - 22, 130, 20)
            .build()
        ), "autoclicker-fabric.gui.help.onDelay");
        // 1000f represents the sliders maximum value
        this.sliderTooltips.put(this.addDrawableChild(new OptionsSliderWidget(x - 200, y, 130, 20, Language.GUI_SPEED.getText(), AutoClicker.leftHolding.getSpeed() / 1000f, value -> {
            AutoClicker.leftHolding.setSpeed(value);
            AutoClicker.getInstance().saveConfig();
        })), "autoclicker-fabric.gui.help.spam-speed");
        
        this.sliderTooltips.put(this.addDrawableChild(new OptionsSliderWidget(x - 65, y, 130, 20, Language.GUI_SPEED.getText(), AutoClicker.rightHolding.getSpeed() / 1000f, value -> {
            AutoClicker.rightHolding.setSpeed(value);
            AutoClicker.getInstance().saveConfig();
        })), "autoclicker-fabric.gui.help.spam-speed");
        
        this.sliderTooltips.put(this.addDrawableChild(new OptionsSliderWidget(x + 70, y, 130, 20, Language.GUI_SPEED.getText(), AutoClicker.jumpHolding.getSpeed() / 1000f, value -> {
            AutoClicker.jumpHolding.setSpeed(value);
            AutoClicker.getInstance().saveConfig();
        })), "autoclicker-fabric.gui.help.spam-speed");
        
        this.buttonTooltips.put(this.addDrawableChild(
            ButtonWidget.builder(
                Language.GUI_RESPECT_COOLDOWN.getText(AutoClicker.leftHolding.isRespectCooldown()), (button) -> {
                    AutoClicker.leftHolding.setRespectCooldown(!AutoClicker.leftHolding.isRespectCooldown());
                    button.setMessage(Language.GUI_RESPECT_COOLDOWN.getText(AutoClicker.leftHolding.isRespectCooldown()));
                    AutoClicker.getInstance().saveConfig();
            })
            .dimensions(x - 200, y + 22, 130, 20)
            .build()
        ), "autoclicker-fabric.gui.help.cooldown");
        
        this.buttonTooltips.put(this.addDrawableChild(
            ButtonWidget.builder(
              Language.GUI_MOB_MODE.getText(AutoClicker.leftHolding.isMobMode()), (button) -> {
                    AutoClicker.leftHolding.setMobMode(!AutoClicker.leftHolding.isMobMode());
                    button.setMessage(Language.GUI_MOB_MODE.getText(AutoClicker.leftHolding.isMobMode()));
                    AutoClicker.getInstance().saveConfig();
            })
            .dimensions(x - 200, y + 44, 130, 20)
            .build()
        ), "autoclicker-fabric.gui.help.mob-mode");
    }

    private void renderHelpingTip(MatrixStack stack, Text text) {
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
                this.width / 2f - 200,
                this.height / 2f - 56,
                0xFFFFFF);

        this.textRenderer.drawWithShadow(
                matrices, Language.GUI_USE.getText(), this.width / 2f - 65, this.height / 2f - 56, 0xFFFFFF);

        this.textRenderer.drawWithShadow(
                matrices, Language.GUI_JUMP.getText(), this.width / 2f + 70, this.height / 2f - 56, 0xFFFFFF);
        
        for (ButtonWidget button : buttonTooltips.keySet()) {
        	if (button.isHovered()) {
        		this.renderHelpingTip(matrices, Text.translatable(this.buttonTooltips.get(button)));
        	}
        }

        for (OptionsSliderWidget slider : sliderTooltips.keySet()) {
        	if (slider.isHovered()) {
        		this.renderHelpingTip(matrices, Text.translatable(this.sliderTooltips.get(slider)));
        	}
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == AutoClicker.rightClickToggle.getDefaultKey().getCode()) {
            this.close();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
