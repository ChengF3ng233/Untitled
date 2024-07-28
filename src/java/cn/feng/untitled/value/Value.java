package cn.feng.untitled.value;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class Value<T> {
    public String name;
    public T defaultValue;
    public T value;

    public Value(String name, T defaultValue, T value) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = value;
    }

    public void reset() {
        value = defaultValue;
    }
}
