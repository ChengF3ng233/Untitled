package cn.feng.untitled.event.impl;

import cn.feng.untitled.event.api.Event;

/**
 * @author ChengFeng
 * @since 2024/8/5
 **/
public class NanoEvent extends Event {
    public long vg;

    public NanoEvent(long vg) {
        this.vg = vg;
    }
}
