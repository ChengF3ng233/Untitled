package cn.feng.untitled.event.api;

import cn.feng.untitled.event.type.EventPriority;

import java.lang.reflect.Method;

public class EventData {
    public final Object source;
    public final Method target;
    public final EventPriority priority;
    public EventData(Object source, Method target, EventPriority priority) {
        this.source = source;
        this.target = target;
        this.priority = priority;
    }
}