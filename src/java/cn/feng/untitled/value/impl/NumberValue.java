package cn.feng.untitled.value.impl;

import cn.feng.untitled.util.misc.MathUtil;
import cn.feng.untitled.value.Value;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class NumberValue extends Value<Double> {
    public double step;
    public NumberValue(String name, double defaultValue, double value, double step) {
        super(name, defaultValue, value);
        this.step = step;
    }

    public void setValue(double newValue) {
        value = MathUtil.round(newValue, step);
    }
}
