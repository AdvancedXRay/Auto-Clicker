package pro.mikey.autoclicker;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;

@Deprecated
public class OptionsSliderWidget extends SliderWidget {
    public Consumer<Integer> onUpdate;

    public OptionsSliderWidget(int x, int y, int width, int height, Text text, double value, Consumer<Integer> onUpdate) {
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
