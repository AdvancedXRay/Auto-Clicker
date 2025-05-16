package pro.mikey.autoclicker;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

@Deprecated
public class OptionsSliderWidget extends AbstractSliderButton {
    public Consumer<Integer> onUpdate;

    public OptionsSliderWidget(int x, int y, int width, int height, Component text, double value, Consumer<Integer> onUpdate) {
        super(x, y, width, height, text, value);
        this.onUpdate = onUpdate;
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Language.GUI_SPEED.getText((int) Math.round(this.value * 1200)));
    }

    @Override
    protected void applyValue() {
        this.onUpdate.accept((int) Math.round(this.value * 1200));
    }
}
