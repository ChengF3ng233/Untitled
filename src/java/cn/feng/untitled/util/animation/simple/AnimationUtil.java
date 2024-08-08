package cn.feng.untitled.util.animation.simple;

/**
 * @author ChengFeng
 * @since 2024/8/1
 **/
public class AnimationUtil {
    public static double animate(double target, double current, double speed) {
        boolean larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        current = larger ? current + factor : current - factor;
        return current;
    }
}
