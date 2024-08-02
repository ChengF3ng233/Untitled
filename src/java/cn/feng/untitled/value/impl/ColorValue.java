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
    public NumberValue speed = new NumberValue("RainbowSpeed", 15f, 30f, 5f, 1f);

    public BoolValue rainbow = new BoolValue("Rainbow", false);

    public ColorValue(String name, Color defaultValue) {
        super(name, defaultValue);
        setColor(defaultValue);
    }

    public void setColor(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hue.value = (double) hsb[0];
        saturation.value = (double) hsb[1];
        brightness.value = (double) hsb[2];
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
        if (rainbow.value) return ColorUtil.rainbow(speed.value.intValue(), 0, saturation.value.floatValue(), brightness.value.floatValue(), opacity.value.floatValue());
        return ColorUtil.applyOpacity(Color.getHSBColor(hue.value.floatValue(), saturation.value.floatValue(), brightness.value.floatValue()), opacity.value.floatValue());
    }
}
