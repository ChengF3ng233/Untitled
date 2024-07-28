package cn.feng.untitled.value.impl;

import cn.feng.untitled.value.Value;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class ColorValue extends Value<Color> {
    public float hue = 0;
    public float saturation = 1;
    public float brightness = 1;

    public ColorValue(String name, Color defaultValue, Color value) {
        super(name, defaultValue, value);
    }

    public void setValue(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hue = hsb[0];
        saturation = hsb[1];
        brightness = hsb[2];
    }

    public void setValue(float hue, float saturation, float brightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }
    public String getHexCode() {
        Color color = getValue();
        return String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    public Color getValue() {
        return Color.getHSBColor(hue, saturation, brightness);
    }
}
