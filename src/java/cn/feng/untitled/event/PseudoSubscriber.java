package cn.feng.untitled.event;

import java.util.function.Consumer;

public class PseudoSubscriber<T extends Event> {

    private final Consumer<T> processor;

    public PseudoSubscriber(Consumer<T> processor) {
        this.processor = processor;
    }

    @SubscribeEvent
    private void onEvent(T event) {
        processor.accept(event);
    }

}