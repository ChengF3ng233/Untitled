package cn.feng.untitled.util.animation.composed;

import cn.feng.untitled.util.animation.Animation;
import cn.feng.untitled.util.animation.ComposedAnimation;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;

/**
 * @author ChengFeng
 * @since 2024/7/29
 **/
@Getter
public class CustomAnimation extends ComposedAnimation<Double> {
    private Animation animation;
    private double startPoint;
    private double endPoint;
    private int duration;

    public CustomAnimation(Class<? extends Animation> animationClass, int ms, double startPoint, double endPoint) {
        this.createAnimation(animationClass, ms, startPoint, endPoint);
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.duration = ms;
    }

    public void setStartPoint(double startPoint) {
        this.startPoint = startPoint;
        this.animation.endPoint = this.endPoint - this.startPoint;
    }

    public void setEndPoint(double endPoint) {
        this.endPoint = endPoint;
        this.animation.endPoint = this.endPoint - this.startPoint;
    }

    public void setDuration(int duration) {
        this.duration = duration;
        this.animation.duration = duration;
    }

    @Override
    public Double getOutput() {
        return startPoint + animation.getOutput();
    }

    @Override
    public void changeDirection() {
        animation.changeDirection();
    }

    private void createAnimation(Class<? extends Animation> animationClass, int ms, double startPoint, double endPoint) {
        try {
            this.animation = animationClass.getConstructor(int.class, double.class).newInstance(ms, endPoint - startPoint);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
