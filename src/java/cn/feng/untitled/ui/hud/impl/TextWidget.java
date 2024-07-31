package cn.feng.untitled.ui.hud.impl;

import cn.feng.untitled.ui.font.Font;
import cn.feng.untitled.ui.font.FontLoader;
import cn.feng.untitled.ui.hud.Widget;
import cn.feng.untitled.value.impl.ColorValue;
import cn.feng.untitled.value.impl.NumberValue;
import cn.feng.untitled.value.impl.StringValue;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/7/30
 **/
public class TextWidget extends Widget {
    public TextWidget() {
        super("Text", true);
    }

    private final StringValue text = new StringValue("DisplayText", "Content");
    private final NumberValue size = new NumberValue("FontSize", 18f, 50f, 10f, 1f);
    private final ColorValue color = new ColorValue("FontColor", Color.WHITE);

    @Override
    public void render() {
        Font font = FontLoader.rubik(size.value.intValue());
        width = font.getStringWidth(text.value);
        height = (float) font.height();
        font.drawString("你好", x, y, color.getValue().getRGB());
    }
}
