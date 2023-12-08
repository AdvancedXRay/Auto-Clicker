package pro.mikey.autoclicker;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class OptionsSliderWidget extends SliderWidget {
    public Consumer<Integer> onUpdate;
    private final Language baseText;

    public OptionsSliderWidget(int x, int y, int width, int height, Language text, double value, Consumer<Integer> onUpdate) {
        super(x, y, width, height, text.getText(), value);
        this.baseText = text;
        this.onUpdate = onUpdate;
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(baseText.getText((int) Math.round(this.value * 50)));
    }

    @Override
    protected void applyValue() {
        this.onUpdate.accept((int) Math.round(this.value * 50));
    }
}
