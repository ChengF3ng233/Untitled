package cn.feng.untitled.event.impl;

import cn.feng.untitled.event.api.CancellableEvent;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class ChatEvent extends CancellableEvent {
    public String text;

    public ChatEvent(String text) {
        this.text = text;
    }
}
