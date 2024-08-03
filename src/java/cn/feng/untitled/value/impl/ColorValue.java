package cn.feng.untitled.value.impl;

import cn.feng.untitled.util.render.ColorUtil;
import cn.feng.untitled.value.Value;

import java.awt.*;
import java.util.Locale;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class ColorValue extends Value<Color> {
    public NumberValue hue = new NumberValue("Hue", 0f, 1f, 0f, 0.01f);
    public NumberValue saturation = new NumberValue("Saturation", 0f, 1f, 0f, 0.01f);
    public NumberValue brightness = new NumberValue("Brightness", 0f, 1f, 0f, 0.01f);
    public NumberValue opacity = new NumberValue("Opacity", 1f, 1f, 0.01f, 0.01f);
    public NumberValue speed = new NumberValue("Speed", 15f, 30f, 5f, 1f);

    public BoolValue rainbow = new BoolValue("Rainbow", false);
    public BoolValue fade = new BoolValue("Fade", false);

    public ColorValue(String name, Color defaultValue) {
        super(name, defaultValue);
        setColor(defaultValue);
    }

    public void setColor(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hue.value = (double) hsb[0];
        saturation.value = (double) hsb[1];
        brightness.value = (double) hsb[2];
        opacity.value = color.getAlpha() / 255d;
    }

    public void setColor(float hue, float saturation, float brightness, float opacity) {
        this.hue.value = (double) hue;
        this.saturation.value = (double) saturation;
        this.brightness.value = (double) brightness;
        this.opacity.value = (double) opacity;
    }

    public String getHexCode() {
        Color color = getColor();
        return String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()).toUpperCase(Locale.ROOT);
    }

    public Color getColor() {
        Color color = ColorUtil.applyOpacity(Color.getHSBColor(hue.value.floatValue(), saturation.value.floatValue(), brightness.value.floatValue()), opacity.value.floatValue());
        if (rainbow.value) {
            color =  ColorUtil.rainbow(speed.value.intValue(), 0, saturation.value.floatValue(), brightness.value.floatValue(), opacity.value.floatValue());
        }
        if (fade.value) {
            color = ColorUtil.fade(speed.value.intValue(), 0, color, opacity.value.floatValue());
        }
        return color;
    }

    public Color getColor(int index) {
        Color color = getColor();
        if (rainbow.value) {
            color = ColorUtil.rainbow(speed.value.intValue(), index, saturation.value.floatValue(), brightness.value.floatValue(), opacity.value.floatValue());
        }
        if (fade.value) {
            color = ColorUtil.fade(speed.value.intValue(), index, color, opacity.value.floatValue());
        }
        return color;
    }
}
