package net.optifine.reflect;

import lombok.Getter;

public class ReflectorFields {
    @Getter
    private final ReflectorClass reflectorClass;
    @Getter
    private final Class fieldType;
    @Getter
    private int fieldCount;
    private ReflectorField[] reflectorFields;

    public ReflectorFields(ReflectorClass reflectorClass, Class fieldType, int fieldCount) {
        this.reflectorClass = reflectorClass;
        this.fieldType = fieldType;

        if (reflectorClass.exists()) {
            if (fieldType != null) {
                this.reflectorFields = new ReflectorField[fieldCount];

                for (int i = 0; i < this.reflectorFields.length; ++i) {
                    this.reflectorFields[i] = new ReflectorField(reflectorClass, fieldType, i);
                }
            }
        }
    }

    public ReflectorField getReflectorField(int index) {
        return index >= 0 && index < this.reflectorFields.length ? this.reflectorFields[index] : null;
    }
}
