package cn.feng.untitled.event.impl;

import cn.feng.untitled.event.api.CancellableEvent;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class UpdateEvent extends CancellableEvent {
    public double x, y, z;
    public float yaw, pitch;
    public boolean ground;

    public UpdateEvent(double x, double y, double z, float yaw, float pitch, boolean ground) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.ground = ground;
    }
}
