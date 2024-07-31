package cn.feng.untitled.value;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class Value<T> {
    public String name;
    public T defaultValue;
    public T value;

    public Value(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public void reset() {
        value = defaultValue;
    }
}
