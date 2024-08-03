package cn.feng.untitled.ui.widget.impl;

import cn.feng.untitled.module.impl.client.PostProcessing;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.widget.Widget;
import cn.feng.untitled.util.render.blur.BlurUtil;
import cn.feng.untitled.value.impl.ColorValue;
import cn.feng.untitled.value.impl.NumberValue;
import cn.feng.untitled.value.impl.StringValue;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/7/30
 **/
public class TextWidget extends Widget {
    private final StringValue text = new StringValue("DisplayText", "Content");
    private final NumberValue size = new NumberValue("FontSize", 30f, 80f, 20f, 1f);
    private final ColorValue color = new ColorValue("FontColor", Color.WHITE);

    public TextWidget() {
        super("Text", true);
    }

    @Override
    public void onRender() {
        float renderX = x * sr.getScaledWidth();
        float renderY = y * sr.getScaledHeight();

        NanoFontLoader.misans.drawString(text.value, renderX, renderY, size.value.intValue(), NanoVG.NVG_ALIGN_LEFT, color.getColor());
    }
}
