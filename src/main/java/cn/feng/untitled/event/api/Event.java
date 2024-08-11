package cn.feng.untitled.event.api;

import cn.feng.untitled.event.type.EventType;
import cn.feng.untitled.event.type.PacketType;

public abstract class Event {

    private boolean cancelled;
    private EventType eventType = EventType.PRE;
    private PacketType packetType = PacketType.SEND;

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public PacketType getPacketType() {
        return packetType;
    }

    public void setPacketType(PacketType packetType) {
        this.packetType = packetType;
    }

    public boolean isCancellable() {
        return this instanceof CancellableEvent || getClass().isAnnotationPresent(Cancellable.class);
    }

    public boolean isCancelled() {
        if (!isCancellable()) {
            return false;
        }

        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        if (!isCancellable()) {
            throw new IllegalArgumentException("Cannot cancel an uncancellable event!");
        }

        this.cancelled = cancelled;
    }

    public void cancel() {
        if (!isCancellable()) {
            throw new IllegalArgumentException("Cannot cancel an uncancellable event!");
        }

        setCancelled(true);
    }

    public void uncancel() {
        if (!isCancellable()) {
            throw new IllegalArgumentException("Cannot uncancel an uncancellable event!");
        }

        setCancelled(false);
    }

}