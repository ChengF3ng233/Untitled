package cn.feng.untitled.value.impl;

import cn.feng.untitled.value.Value;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class BoolValue extends Value<Boolean> {
    public BoolValue(String name, boolean defaultValue, boolean value) {
        super(name, defaultValue, value);
    }
}
