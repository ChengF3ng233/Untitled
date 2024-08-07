package cn.feng.untitled.ui.widget.impl;

import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.widget.Widget;
import cn.feng.untitled.util.animation.advanced.Animation;
import cn.feng.untitled.util.animation.advanced.impl.SmoothStepAnimation;
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
    private final ColorValue glowColor = new ColorValue("GlowColor", Color.WHITE);
    private final NumberValue glowRadius = new NumberValue("GlowRadius", 10f, 30f, 10f, 1f);
    private final NumberValue size = new NumberValue("FontSize", 30f, 80f, 20f, 1f);
    private final ColorValue color = new ColorValue("FontColor", Color.WHITE);

    private final Animation anim = new SmoothStepAnimation(2000, 1f);

    public TextWidget() {
        super("Text", true);
    }

    @Override
    public void onRender() {
        float renderX = x * sr.getScaledWidth();
        float renderY = y * sr.getScaledHeight();

        if (anim.finished(anim.getDirection())) anim.changeDirection();

        NanoFontLoader.script.drawGlowString(text.value, renderX, renderY, size.value.floatValue(), glowRadius.value.floatValue(), NanoVG.NVG_ALIGN_LEFT, color.getColor(), glowColor.getColor());

        width = NanoFontLoader.script.getStringWidth("Hello, NanoVG!我是小丑🤡我是大狗🐕我是科比🖊", size.value.intValue());
    }
}
