package cn.feng.untitled.event.impl;

import cn.feng.untitled.event.api.Event;

/**
 * @author ChengFeng
 * @since 2024/8/3
 **/
public class ChatGUIEvent extends Event {
    public int mouseX, mouseY;

    public ChatGUIEvent(int mouseX, int mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }
}
