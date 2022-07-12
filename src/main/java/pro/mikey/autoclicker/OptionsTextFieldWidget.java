package pro.mikey.autoclicker;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class OptionsTextFieldWidget extends TextFieldWidget {
    public OptionsTextFieldWidget(int x, int y, int width, int height, String value) {
        super(MinecraftClient.getInstance().textRenderer, x, y, width, height, Text.literal(""));
        setMaxLength(256);
        setText(value);
    }
}
