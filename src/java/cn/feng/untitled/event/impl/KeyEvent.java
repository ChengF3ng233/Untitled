package cn.feng.untitled.event.impl;

import cn.feng.untitled.event.api.CancellableEvent;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class KeyEvent extends CancellableEvent {
    public int key;

    public KeyEvent(int key) {
        this.key = key;
    }
}
