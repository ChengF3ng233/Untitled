package cn.feng.untitled.util.animation.simple;


/**
 * @author ChengFeng
 * @since 2024/8/1
 **/
public class SimpleAnimation {
    public double current = 0.0f;
    public double target = 0.0f;
    public double speed;

    public SimpleAnimation(double speed) {
        this.speed = speed;
    }

    public SimpleAnimation() {

    }

    public double animate() {
        this.current = AnimationUtil.animate(this.target, this.current, this.speed);
        if (this.isFinished()) {
            this.current = this.target;
        }
        return this.current;
    }

    public double animate(double speed) {
        this.speed = speed;
        return animate();
    }

    public boolean isFinished() {
        return Math.abs(this.current - this.target) < 0.01f;
    }
}