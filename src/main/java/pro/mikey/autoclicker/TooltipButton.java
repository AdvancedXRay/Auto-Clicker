package pro.mikey.autoclicker;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class TooltipButton extends ButtonWidget {
    TranslatableText tooltipCache;

    public TooltipButton(int x, int y, int width, int height, Text message, PressAction onPress, ButtonWidget.TooltipSupplier tooltipSupplier, String tooltipLangKey) {
        super(x, y, width, height, message, onPress, tooltipSupplier);
        this.tooltipCache = new TranslatableText(tooltipLangKey);
    }
}
