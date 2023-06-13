package pro.mikey.autoclicker;

import java.util.HashMap;

import java.util.List;
import java.util.ListIterator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
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
                Language.GUI_SPAMMING.getText(AutoClicker.leftHolding.isSpamming()), (button) -> {
                    AutoClicker.leftHolding.setSpamming(!AutoClicker.leftHolding.isSpamming());
                    button.setMessage(Language.GUI_SPAMMING.getText(AutoClicker.leftHolding.isSpamming()));
                    AutoClicker.getInstance().saveConfig();
            })
            .dimensions(x - 200, y - 22, 130, 20)
            .build()
        ), "autoclicker-fabric.gui.help.spamming");
        
        this.buttonTooltips.put(this.addDrawableChild(
            ButtonWidget.builder(
                Language.GUI_SPAMMING.getText(AutoClicker.rightHolding.isSpamming()), (button) -> {
                    AutoClicker.rightHolding.setSpamming(!AutoClicker.rightHolding.isSpamming());
                    button.setMessage(Language.GUI_SPAMMING.getText(AutoClicker.rightHolding.isSpamming()));
                    AutoClicker.getInstance().saveConfig();
            })
            .dimensions(x - 65, y - 22, 130, 20)
            .build()
        ), "autoclicker-fabric.gui.help.spamming");
        
        this.buttonTooltips.put(this.addDrawableChild(
            ButtonWidget.builder(
                Language.GUI_SPAMMING.getText(AutoClicker.jumpHolding.isSpamming()), (button) -> {
                    AutoClicker.jumpHolding.setSpamming(!AutoClicker.jumpHolding.isSpamming());
                    button.setMessage(Language.GUI_SPAMMING.getText(AutoClicker.jumpHolding.isSpamming()));
                    AutoClicker.getInstance().saveConfig();
            })
            .dimensions(x + 70, y - 22, 130, 20)
            .build()
        ), "autoclicker-fabric.gui.help.spamming");
        
        this.sliderTooltips.put(this.addDrawableChild(new OptionsSliderWidget(x - 200, y, 130, 20, Language.GUI_SPEED.getText(), AutoClicker.leftHolding.getSpeed() / 50f, value -> {
            AutoClicker.leftHolding.setSpeed(value);
            AutoClicker.getInstance().saveConfig();
        })), "autoclicker-fabric.gui.help.spam-speed");
        
        this.sliderTooltips.put(this.addDrawableChild(new OptionsSliderWidget(x - 65, y, 130, 20, Language.GUI_SPEED.getText(), AutoClicker.rightHolding.getSpeed() / 50f, value -> {
            AutoClicker.rightHolding.setSpeed(value);
            AutoClicker.getInstance().saveConfig();
        })), "autoclicker-fabric.gui.help.spam-speed");
        
        this.sliderTooltips.put(this.addDrawableChild(new OptionsSliderWidget(x + 70, y, 130, 20, Language.GUI_SPEED.getText(), AutoClicker.jumpHolding.getSpeed() / 50f, value -> {
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

    private void renderHelpingTip(DrawContext drawContext, Text text) {
        int x = this.width / 2, y = this.height / 2;
        ListIterator<OrderedText> l = this.textRenderer.wrapLines(StringVisitable.plain(text.getString()), 270).listIterator();
        int line_pos = 0;
        while (l.hasNext()) {
            OrderedText e = l.next();
            drawContext.drawText(this.textRenderer, e,
                x - 140,
                y + 100 + line_pos,
                0xFFFFFF,
                false);
            line_pos += 10;
        }
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        this.renderBackground(drawContext);
        super.render(drawContext, mouseX, mouseY, delta);

        drawContext.drawTextWithShadow(
                this.textRenderer,
                Language.GUI_ATTACK.getText(),
                this.width / 2 - 200,
                this.height / 2 - 56,
                0xFFFFFF);

        drawContext.drawTextWithShadow(
            this.textRenderer, Language.GUI_USE.getText(), this.width / 2 - 65, this.height / 2 - 56, 0xFFFFFF);

        drawContext.drawTextWithShadow(
            this.textRenderer, Language.GUI_JUMP.getText(), this.width / 2 + 70, this.height / 2 - 56, 0xFFFFFF);
        
        for (ButtonWidget button : buttonTooltips.keySet()) {
        	if (button.isHovered()) {
        		this.renderHelpingTip(drawContext, Text.translatable(this.buttonTooltips.get(button)));
        	}
        }

        for (OptionsSliderWidget slider : sliderTooltips.keySet()) {
        	if (slider.isHovered()) {
        		this.renderHelpingTip(drawContext, Text.translatable(this.sliderTooltips.get(slider)));
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
