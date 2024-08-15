package net.minecraft.util;

import lombok.Getter;

public class FrameTimer {
    /**
     * An array with the last 240 frames
     * -- GETTER --
     *  Return the array of frames

     */
    @Getter
    private final long[] frames = new long[240];

    /**
     * The last index used when 240 frames have been set
     * -- GETTER --
     *  Return the last index used when 240 frames have been set

     */
    @Getter
    private int lastIndex;

    /**
     * A counter
     */
    private int counter;

    /**
     * The next index to use in the array
     * -- GETTER --
     *  Return the index of the next frame in the array

     */
    @Getter
    private int index;

    /**
     * Add a frame at the next index in the array frames
     *
     * @param runningTime The game's uptime in nanoseconds
     */
    public void addFrame(long runningTime) {
        this.frames[this.index] = runningTime;
        ++this.index;

        if (this.index == 240) {
            this.index = 0;
        }

        if (this.counter < 240) {
            this.lastIndex = 0;
            ++this.counter;
        } else {
            this.lastIndex = this.parseIndex(this.index + 1);
        }
    }

    /**
     * Return a value from time and multiplier to display the lagometer
     *
     * @param time       The time corresponding to the frame
     * @param multiplier Use to multiply
     */
    public int getLagometerValue(long time, int multiplier) {
        double d0 = (double) time / 1.6666666E7D;
        return (int) (d0 * (double) multiplier);
    }

    /**
     * Change 240 to 0
     *
     * @param rawIndex The index to parse
     */
    public int parseIndex(int rawIndex) {
        return rawIndex % 240;
    }

}
