package cn.feng.untitled.ui.widget.impl;

import cn.feng.untitled.ui.clickgui.neverlose.ThemeColor;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.widget.Widget;
import cn.feng.untitled.util.animation.advanced.Animation;
import cn.feng.untitled.util.animation.advanced.impl.SmoothStepAnimation;
import cn.feng.untitled.util.render.RoundedUtil;
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
    private final StringValue text = new StringValue("ClientName", "Untitled");
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
        float gap = 5f;
        width = NanoFontLoader.monster.getStringWidth(text.value, size.value.intValue()) + 2 * gap;
        height = size.value.floatValue() / 2f;

        if (anim.finished(anim.getDirection())) anim.changeDirection();

        RoundedUtil.drawRound(renderX, renderY, width, height, size.value.floatValue() / 10f, ThemeColor.panelColor);
        NanoFontLoader.monster.drawGlowString(text.value, renderX + gap, renderY - height / 2.5f, size.value.floatValue(), glowRadius.value.floatValue(), NanoVG.NVG_ALIGN_MIDDLE, color.getColor(), glowColor.getColor());
    }
}
