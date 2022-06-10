package pro.mikey.autoclicker;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class TooltipButton extends ButtonWidget {
    Text tooltipCache;

    public TooltipButton(int x, int y, int width, int height, Text message, PressAction onPress, ButtonWidget.TooltipSupplier tooltipSupplier, String tooltipLangKey) {
        super(x, y, width, height, message, onPress, tooltipSupplier);
        this.tooltipCache = Text.translatable(tooltipLangKey);
    }
}
