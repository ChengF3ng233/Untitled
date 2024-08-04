package cn.feng.untitled.ui.widget.impl;

import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoUtil;
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
    private final NumberValue glow = new NumberValue("GlowRadius", 10f, 30f, 10f, 1f);
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

        NanoUtil.prepareNano();
        NanoUtil.scaleStart(renderX, renderY, anim.getOutput().floatValue());
        NanoFontLoader.script.drawGlowString("好的NanoVG使我的内存爆炸🤡", renderX, renderY, size.value.intValue(), NanoVG.NVG_ALIGN_LEFT, color.getColor());
        NanoUtil.scaleEnd();
        NanoUtil.endNano();

        width = NanoFontLoader.script.getStringWidth("好的NanoVG使我的内存爆炸🤡", size.value.intValue()) * anim.getOutput().floatValue();
    }
}
