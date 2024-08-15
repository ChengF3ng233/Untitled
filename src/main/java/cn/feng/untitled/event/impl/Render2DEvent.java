package cn.feng.untitled.event.impl;

import cn.feng.untitled.event.api.Event;

/**
 * @author ChengFeng
 * @since 2024/7/30
 **/
public class Render2DEvent extends Event {
    public float partialTicks;

    public Render2DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}
