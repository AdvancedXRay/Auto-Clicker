package pro.mikey.autoclicker;


import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class OptionsScreen extends Screen {
    private final HashMap<Button, String> buttonTooltips = new HashMap<>();
    private final HashMap<EditBox, String> sliderTooltips = new HashMap<>();
    private EditBox leftHoldingSpamSpeed;
    private EditBox rightHoldingSpamSpeed;
    private EditBox jumpHoldingSpamSpeed;

    protected OptionsScreen() {
        super(Component.empty());
    }

    public static Screen createScreen(@Nullable Screen parent) {
        return new OptionsScreen();
    }

    @Override
    protected void init() {
        int x = (this.width / 2), y = (this.height / 2);

        leftHoldingSpamSpeed = new EditBox(minecraft.font, x - 200, y-50, 130, 20, Component.literal(String.valueOf(AutoClicker.leftHolding.getSpeed())));
        rightHoldingSpamSpeed = new EditBox(minecraft.font, x - 65, y-50, 130, 20, Component.literal(String.valueOf(AutoClicker.rightHolding.getSpeed())));
        jumpHoldingSpamSpeed = new EditBox(minecraft.font, x + 70, y-50, 130, 20, Component.literal(String.valueOf(AutoClicker.jumpHolding.getSpeed())));

        leftHoldingSpamSpeed.setValue(String.valueOf(AutoClicker.leftHolding.getSpeed()));
        rightHoldingSpamSpeed.setValue(String.valueOf(AutoClicker.rightHolding.getSpeed()));
        jumpHoldingSpamSpeed.setValue(String.valueOf(AutoClicker.jumpHolding.getSpeed()));

        leftHoldingSpamSpeed.setResponder(s -> {
            if(s.startsWith("0") && s.length() > 1){
                s = s.substring(1);
                leftHoldingSpamSpeed.setValue(s);
            }

            try {
                AutoClicker.leftHolding.setSpeed(Integer.parseInt(s));
            }
            catch (NumberFormatException e){
                AutoClicker.leftHolding.setSpeed(0);
                leftHoldingSpamSpeed.setValue(String.valueOf(AutoClicker.leftHolding.getSpeed()));
            }

            AutoClicker.getInstance().saveConfig();
        });


        rightHoldingSpamSpeed.setResponder(s -> {
            if(s.startsWith("0") && s.length() > 1){
                s = s.substring(1);
                rightHoldingSpamSpeed.setValue(s);
            }

            try {
                AutoClicker.rightHolding.setSpeed(Integer.parseInt(s));
            }
            catch (NumberFormatException e){
                AutoClicker.rightHolding.setSpeed(0);
                rightHoldingSpamSpeed.setValue(String.valueOf(AutoClicker.rightHolding.getSpeed()));
            }

            AutoClicker.getInstance().saveConfig();
        });

        jumpHoldingSpamSpeed.setResponder(s -> {
            if(s.startsWith("0") && s.length() > 1){
                s = s.substring(1);
                jumpHoldingSpamSpeed.setValue(s);
            }

            try {
                AutoClicker.jumpHolding.setSpeed(Integer.parseInt(s));
            }
            catch (NumberFormatException e){
                AutoClicker.jumpHolding.setSpeed(0);
                jumpHoldingSpamSpeed.setValue(String.valueOf(AutoClicker.jumpHolding.getSpeed()));
            }

            AutoClicker.getInstance().saveConfig();
        });

        // Button Tooltips

        this.buttonTooltips.put(this.addRenderableWidget(
            Button.builder(
                Language.GUI_ACTIVE.getText(AutoClicker.leftHolding.isActive()), (button) -> {
                    AutoClicker.leftHolding.setActive(!AutoClicker.leftHolding.isActive());
                    button.setMessage(Language.GUI_ACTIVE.getText(AutoClicker.leftHolding.isActive()));
                    AutoClicker.getInstance().saveConfig();
            })
            .bounds(x - 200, y - 94, 130, 20)
            .build()
        ), "autoclicker-fabric.gui.help.active");

        this.buttonTooltips.put(this.addRenderableWidget(
            Button.builder(
                Language.GUI_ACTIVE.getText(AutoClicker.rightHolding.isActive()), (button) -> {
                    AutoClicker.rightHolding.setActive(!AutoClicker.rightHolding.isActive());
                    button.setMessage(Language.GUI_ACTIVE.getText(AutoClicker.rightHolding.isActive()));
                    AutoClicker.getInstance().saveConfig();
            })
            .bounds(x - 65, y - 94, 130, 20)
            .build()
        ), "autoclicker-fabric.gui.help.active");

        this.buttonTooltips.put(this.addRenderableWidget(
            Button.builder(
                Language.GUI_ACTIVE.getText(AutoClicker.jumpHolding.isActive()), (button) -> {
                    AutoClicker.jumpHolding.setActive(!AutoClicker.jumpHolding.isActive());
                    button.setMessage(Language.GUI_ACTIVE.getText(AutoClicker.jumpHolding.isActive()));
                    AutoClicker.getInstance().saveConfig();
            })
            .bounds(x + 70 , y - 94, 130, 20)
            .build()
        ), "autoclicker-fabric.gui.help.active");

        this.buttonTooltips.put(this.addRenderableWidget(
            Button.builder(
                Language.GUI_SPAMMING.getText(AutoClicker.leftHolding.isSpamming()), (button) -> {
                    AutoClicker.leftHolding.setSpamming(!AutoClicker.leftHolding.isSpamming());
                    button.setMessage(Language.GUI_SPAMMING.getText(AutoClicker.leftHolding.isSpamming()));
                    AutoClicker.getInstance().saveConfig();
            })
            .bounds(x - 200, y - 72, 130, 20)
            .build()
        ), "autoclicker-fabric.gui.help.spamming");

        this.buttonTooltips.put(this.addRenderableWidget(
            Button.builder(
                Language.GUI_SPAMMING.getText(AutoClicker.rightHolding.isSpamming()), (button) -> {
                    AutoClicker.rightHolding.setSpamming(!AutoClicker.rightHolding.isSpamming());
                    button.setMessage(Language.GUI_SPAMMING.getText(AutoClicker.rightHolding.isSpamming()));
                    AutoClicker.getInstance().saveConfig();
            })
            .bounds(x - 65, y - 72, 130, 20)
            .build()
        ), "autoclicker-fabric.gui.help.spamming");

        this.buttonTooltips.put(this.addRenderableWidget(
            Button.builder(
                Language.GUI_SPAMMING.getText(AutoClicker.jumpHolding.isSpamming()), (button) -> {
                    AutoClicker.jumpHolding.setSpamming(!AutoClicker.jumpHolding.isSpamming());
                    button.setMessage(Language.GUI_SPAMMING.getText(AutoClicker.jumpHolding.isSpamming()));
                    AutoClicker.getInstance().saveConfig();
            })
            .bounds(x + 70, y - 72, 130, 20)
            .build()
        ), "autoclicker-fabric.gui.help.spamming");

        this.sliderTooltips.put(this.addRenderableWidget(leftHoldingSpamSpeed), "autoclicker-fabric.gui.help.spam-speed");

        this.sliderTooltips.put(this.addRenderableWidget(rightHoldingSpamSpeed), "autoclicker-fabric.gui.help.spam-speed");

        this.sliderTooltips.put(this.addRenderableWidget(jumpHoldingSpamSpeed), "autoclicker-fabric.gui.help.spam-speed");

        this.buttonTooltips.put(this.addRenderableWidget(
            Button.builder(
                Language.GUI_RESPECT_COOLDOWN.getText(AutoClicker.leftHolding.isRespectCooldown()), (button) -> {
                    AutoClicker.leftHolding.setRespectCooldown(!AutoClicker.leftHolding.isRespectCooldown());
                    button.setMessage(Language.GUI_RESPECT_COOLDOWN.getText(AutoClicker.leftHolding.isRespectCooldown()));
                    AutoClicker.getInstance().saveConfig();
            })
            .bounds(x - 200, y - 28, 130, 20)
            .build()
        ), "autoclicker-fabric.gui.help.cooldown");

        this.buttonTooltips.put(this.addRenderableWidget(
                Button.builder(
                                Language.GUI_RESPECT_SHIELD.getText(AutoClicker.leftHolding.isRespectShield()), (button) -> {
                                    AutoClicker.leftHolding.setRespectShield(!AutoClicker.leftHolding.isRespectShield());
                                    button.setMessage(Language.GUI_RESPECT_SHIELD.getText(AutoClicker.leftHolding.isRespectShield()));
                                    AutoClicker.getInstance().saveConfig();
                                })
                .bounds(x - 200, y - 6, 130, 20)
                        .build()
        ), "autoclicker-fabric.gui.help.shield");

        this.buttonTooltips.put(this.addRenderableWidget(
            Button.builder(
              Language.GUI_MOB_MODE.getText(AutoClicker.leftHolding.isMobMode()), (button) -> {
                    AutoClicker.leftHolding.setMobMode(!AutoClicker.leftHolding.isMobMode());
                    button.setMessage(Language.GUI_MOB_MODE.getText(AutoClicker.leftHolding.isMobMode()));
                    AutoClicker.getInstance().saveConfig();
            })
            .bounds(x - 200, y + 16, 130, 20)
            .build()
        ), "autoclicker-fabric.gui.help.mob-mode");

        this.buttonTooltips.put(this.addRenderableWidget(
                Button.builder(
                    Language.GUI_HUD_ENABLED.getText(AutoClicker.hudConfig.isEnabled()), (button) -> {
                    AutoClicker.hudConfig.setEnabled(!AutoClicker.hudConfig.isEnabled());
                    button.setMessage(Language.GUI_HUD_ENABLED.getText(AutoClicker.hudConfig.isEnabled()));
                    AutoClicker.getInstance().saveConfig();
                })
                .bounds(x - 65, y + 38, 130, 20)
            .build()
        ), "autoclicker-fabric.gui.help.hud-enabled");

        this.buttonTooltips.put(this.addRenderableWidget(
                Button.builder(
                    Language.GUI_HUD_LOCATION.getText(AutoClicker.hudConfig.getLocation()), (button) -> {
                        AutoClicker.hudConfig.setLocation(getNextLocation(AutoClicker.hudConfig.getLocation()));
                        button.setMessage(Language.GUI_HUD_LOCATION.getText(AutoClicker.hudConfig.getLocation()));
                        AutoClicker.getInstance().saveConfig();
                    })
                .bounds(x - 65, y + 60, 130, 20)
                        .build()
        ), "autoclicker-fabric.gui.help.hud-location");
    }

    private String getNextLocation(String currentLocation){
        switch (currentLocation){
            case "top-left":
                return "top-right";
            case "top-right":
                return "bottom-left";
            case "bottom-left":
                return "bottom-right";
            case "bottom-right":
                return "top-left";
        }
        return "top-left";
    }

    private void renderHelpingTip(GuiGraphics context, Component text, int mouseX, int mouseY) {
        context.renderTooltip(
            this.font, this.font.split(FormattedText.of(text.getString()), 250), DefaultTooltipPositioner.INSTANCE, mouseX, mouseY);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        context.drawString(
            this.font,Language.GUI_ATTACK.getText().getVisualOrderText(), this.width / 2 - 200, this.height / 2 - 116, 0xFFFFFF);

        context.drawString(
            this.font, Language.GUI_USE.getText().getVisualOrderText(), this.width / 2 - 65, this.height / 2 - 116, 0xFFFFFF);

        context.drawString(
            this.font, Language.GUI_JUMP.getText().getVisualOrderText(), this.width / 2 + 70, this.height / 2 - 116, 0xFFFFFF);

        for (Button button : buttonTooltips.keySet()) {
        	if (button.isHovered()) {
        		this.renderHelpingTip(context, Component.translatable(this.buttonTooltips.get(button)), mouseX, mouseY);
        	}
        }

        for (EditBox widget : sliderTooltips.keySet()) {
        	if (widget.isHovered()) {
        		this.renderHelpingTip(context, Component.translatable(this.sliderTooltips.get(widget)), mouseX, mouseY);
        	}
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == AutoClicker.openConfig.getDefaultKey().getValue()) {
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
