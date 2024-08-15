package net.minecraft.util;

public class IntegerCache {
    private static final Integer[] CACHE = new Integer[65535];

    static {
        int i = 0;

        for (int j = CACHE.length; i < j; ++i) {
            CACHE[i] = i;
        }
    }

    /**
     * Get an Integer from the cache if it exists, otherwise return {@code Integer.valueOf()}
     */
    public static Integer getInteger(int value) {
        return value >= 0 && value < CACHE.length ? CACHE[value] : Integer.valueOf(value);
    }
}
