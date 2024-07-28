package cn.feng.untitled.value.impl;

import cn.feng.untitled.util.misc.StringUtil;
import cn.feng.untitled.value.Value;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class ModeValue extends Value<String> {
    public String[] values;

    public ModeValue(String name, String defaultValue, String[] values) {
        super(name, defaultValue, defaultValue);
        this.values = values;
    }

    public void next() {
        int index = StringUtil.arrayIndex(values, value) + 1;
        if (index == values.length) {
            value = values[0];
            return;
        }
        value = values[index];
    }

    public void previous() {
        int index = StringUtil.arrayIndex(values, value) - 1;
        if (index == -1) {
            value = values[values.length - 1];
            return;
        }
        value = values[index];
    }
}
