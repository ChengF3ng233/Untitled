package cn.feng.untitled.util.misc;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author ChengFeng
 * @since 2024/7/30
 **/
public class Range {
    public double maximum, minimum;
    public double random() {
        return ThreadLocalRandom.current().nextDouble(minimum, maximum);
    }
}
