package cn.feng.untitled.util.animation.simple;

/**
 * @author ChengFeng
 * @since 2024/8/1
 **/
public class AnimationUtil {
    public static float animate(float target, float current, double speed) {
        boolean larger;
        boolean bl = larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        float dif = Math.max(target, current) - Math.min(target, current);
        float factor = (float) ((double) dif * speed);
        current = larger ? current + factor : current - factor;
        return current;
    }
}
