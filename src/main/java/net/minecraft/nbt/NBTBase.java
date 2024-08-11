package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class NBTBase {
    public static final String[] NBT_TYPES = new String[]{"END", "BYTE", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "BYTE[]", "STRING", "LIST", "COMPOUND", "INT[]"};

    /**
     * Creates a new NBTBase object that corresponds with the passed in id.
     */
    protected static NBTBase createNewByType(byte id) {
        return switch (id) {
            case 0 -> new NBTTagEnd();
            case 1 -> new NBTTagByte();
            case 2 -> new NBTTagShort();
            case 3 -> new NBTTagInt();
            case 4 -> new NBTTagLong();
            case 5 -> new NBTTagFloat();
            case 6 -> new NBTTagDouble();
            case 7 -> new NBTTagByteArray();
            case 8 -> new NBTTagString();
            case 9 -> new NBTTagList();
            case 10 -> new NBTTagCompound();
            case 11 -> new NBTTagIntArray();
            default -> null;
        };
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    abstract void write(DataOutput output) throws IOException;

    abstract void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException;

    public abstract String toString();

    /**
     * Gets the type byte for the tag.
     */
    public abstract byte getId();

    /**
     * Creates a clone of the tag.
     */
    public abstract NBTBase copy();

    /**
     * Return whether this compound has no tags.
     */
    public boolean hasNoTags() {
        return false;
    }

    public boolean equals(Object p_equals_1_) {
        if (!(p_equals_1_ instanceof NBTBase nbtbase)) {
            return false;
        } else {
            return this.getId() == nbtbase.getId();
        }
    }

    public int hashCode() {
        return this.getId();
    }

    protected String getString() {
        return this.toString();
    }

    public abstract static class NBTPrimitive extends NBTBase {
        public abstract long getLong();

        public abstract int getInt();

        public abstract short getShort();

        public abstract byte getByte();

        public abstract double getDouble();

        public abstract float getFloat();
    }
}
