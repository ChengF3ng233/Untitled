package net.optifine.reflect;

import lombok.Getter;

import java.lang.reflect.Field;

@Getter
public class FieldLocatorFixed implements IFieldLocator {
    private final Field field;

    public FieldLocatorFixed(Field field) {
        this.field = field;
    }

}
