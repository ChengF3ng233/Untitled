package net.optifine.config;

import lombok.Getter;

@Getter
public class RangeInt {
    private final int min;
    private final int max;

    public RangeInt(int min, int max) {
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
    }

    public boolean isInRange(int val) {
        return val >= this.min && val <= this.max;
    }

    public String toString() {
        return "min: " + this.min + ", max: " + this.max;
    }
}
