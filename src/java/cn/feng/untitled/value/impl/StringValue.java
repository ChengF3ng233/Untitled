package cn.feng.untitled.value.impl;

import cn.feng.untitled.value.Value;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class StringValue extends Value<String> {
    public StringValue(String name, String defaultValue, String value) {
        super(name, defaultValue, value);
    }
}
