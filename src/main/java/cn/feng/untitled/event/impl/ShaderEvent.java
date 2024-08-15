package cn.feng.untitled.event.impl;

import cn.feng.untitled.event.api.Event;

/**
 * @author ChengFeng
 * @since 2024/8/3
 **/
public class ShaderEvent extends Event {
    public boolean bloom;

    public ShaderEvent(boolean bloom) {
        this.bloom = bloom;
    }
}
