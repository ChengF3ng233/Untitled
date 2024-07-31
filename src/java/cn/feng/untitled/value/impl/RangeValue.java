package cn.feng.untitled.value.impl;

import cn.feng.untitled.util.misc.Range;
import cn.feng.untitled.value.Value;

/**
 * @author ChengFeng
 * @since 2024/7/30
 **/
public class RangeValue extends Value<Range> {
    public Range range;
    public RangeValue(String name, Range defaultValue, Range range) {
        super(name, defaultValue);
        this.range = range;
    }
}
