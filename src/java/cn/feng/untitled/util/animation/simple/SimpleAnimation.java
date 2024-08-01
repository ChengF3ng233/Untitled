package cn.feng.untitled.util.animation.simple;


/**
 * @author ChengFeng
 * @since 2024/8/1
 **/
public class SimpleAnimation {
    public float current = 0.0f;
    public float target = 0.0f;
    public float speed;

    public SimpleAnimation(float speed) {
        this.speed = speed;
    }

    public float animate() {
        this.current = AnimationUtil.animate(this.target, this.current, this.speed);
        if (this.isFinished()) {
            this.current = this.target;
        }
        return this.current;
    }

    public boolean isFinished() {
        return Math.abs(this.current - this.target) < 0.01f;
    }
}