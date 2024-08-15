package cn.feng.untitled.value.impl;

import cn.feng.untitled.util.data.MathUtil;
import cn.feng.untitled.value.Value;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class NumberValue extends Value<Double> {
    public double maximum;
    public double minimum;
    public double step;
    public NumberValue(String name, double defaultValue, double maximum, double minimum, double step) {
        super(name, defaultValue);
        this.maximum = maximum;
        this.minimum = minimum;
        this.step = step;
    }

    public void setValue(double newValue) {
        value = MathUtil.round(newValue, step);
    }
}
