package cn.feng.untitled.event.impl;

import cn.feng.untitled.event.api.Event;

/**
 * @author ChengFeng
 * @since 2024/8/5
 **/
public class Render3DEvent extends Event {
    public float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}
