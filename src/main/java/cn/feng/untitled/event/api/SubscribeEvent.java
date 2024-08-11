package cn.feng.untitled.event.api;

import cn.feng.untitled.event.type.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SubscribeEvent {
    EventPriority priority() default EventPriority.NORMAL;
}