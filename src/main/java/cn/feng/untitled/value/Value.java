package cn.feng.untitled.value;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
@Getter
public class Value<T> {
    @Setter
    private String name;
    @Setter
    private T defaultValue;
    protected T value;

    @Setter
    private BiConsumer<T, T> changeAction = (oldValue, newValue) -> {};

    public Value(String name, T defaultValue) {
        this.setName(name);
        this.setDefaultValue(defaultValue);
        this.setValue(defaultValue);
    }

    protected Value(String name, T defaultValue, boolean init) {
        this.setName(name);
        this.setDefaultValue(defaultValue);
        if (init) {
            this.setValue(defaultValue);
        }
    }

    public void reset() {
        setValue(getDefaultValue());
    }

    public void setValue(T value) {
        if (Objects.equals(this.value, value)) return;
        changeAction.accept(this.value, value);
        this.value = value;
    }
}
