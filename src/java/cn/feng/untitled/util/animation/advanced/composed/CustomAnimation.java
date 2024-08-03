package cn.feng.untitled.util.animation.advanced.composed;

import cn.feng.untitled.util.animation.advanced.Animation;
import cn.feng.untitled.util.animation.advanced.ComposedAnimation;
import cn.feng.untitled.util.animation.advanced.Direction;
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
        this.createAnimation(animationClass, ms, startPoint, endPoint, Direction.FORWARDS);
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.duration = ms;
    }

    public CustomAnimation(Class<? extends Animation> animationClass, int ms, double startPoint, double endPoint, Direction direction) {
        this.createAnimation(animationClass, ms, startPoint, endPoint, direction);
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.duration = ms;
    }

    public CustomAnimation(Class<? extends Animation> animationClass, int duration) {
        this.createAnimation(animationClass, duration, startPoint, endPoint, Direction.FORWARDS);
        this.duration = duration;
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

    public Direction getDirection() {
        return animation.getDirection();
    }

    @Override
    public Double getOutput() {
        return startPoint + animation.getOutput();
    }

    @Override
    public void changeDirection() {
        animation.changeDirection();
    }

    private void createAnimation(Class<? extends Animation> animationClass, int ms, double startPoint, double endPoint, Direction direction) {
        try {
            this.animation = animationClass.getConstructor(int.class, double.class, Direction.class).newInstance(ms, endPoint - startPoint, direction);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
