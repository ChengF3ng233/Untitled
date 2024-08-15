package net.minecraft.util;

import lombok.Getter;
import lombok.Setter;

@Setter
public class TupleIntJsonSerializable {
    /**
     * -- SETTER --
     *  Sets this tuple's integer value to the given value.
     * -- GETTER --
     *  Gets the integer value stored in this tuple.

     */
    @Getter
    private int integerValue;
    /**
     * -- SETTER --
     *  Sets this tuple's JsonSerializable value to the given value.
     */
    private IJsonSerializable jsonSerializableValue;

    public <T extends IJsonSerializable> T getJsonSerializableValue() {
        return (T) this.jsonSerializableValue;
    }

}
