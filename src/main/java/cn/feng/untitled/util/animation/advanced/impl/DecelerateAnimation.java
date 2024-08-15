package cn.feng.untitled.util.animation.advanced.impl;

import cn.feng.untitled.util.animation.advanced.Animation;
import cn.feng.untitled.util.animation.advanced.Direction;

/**
 * @author ChengFeng
 * @since 2024/8/1
 **/
public class DecelerateAnimation extends Animation {

    public DecelerateAnimation(int ms, double endPoint) {
        super(ms, endPoint);
    }

    public DecelerateAnimation(int ms, double endPoint, Direction direction) {
        super(ms, endPoint, direction);
    }


    protected double getEquation(double x) {
        return 1 - ((x - 1) * (x - 1));
    }
}
