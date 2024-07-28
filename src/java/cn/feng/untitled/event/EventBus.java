package cn.feng.untitled.event;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

public class EventBus {
    private final Map<Class<? extends Event>, List<EventData>> registry = new HashMap<>();

    private boolean isBadMethod(Method method) {
        return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(SubscribeEvent.class);
    }

    private void sortList(Class<? extends Event> clazz) {
        ArrayList<EventData> flexible = new ArrayList<>();
        for (EventPriority priority : EventPriority.values()) {
            for (EventData data : registry.get(clazz)) {
                if (data.priority == priority) {
                    flexible.add(data);
                }
            }
        }

        registry.put(clazz, flexible);
    }

    public void cleanRegistry() {
        registry.entrySet().removeIf(entry -> entry.getValue() == null || entry.getValue().isEmpty());
    }

    private void register(Class<? extends Event> event, Method method, Object instance) {
        EventData data = new EventData(instance, method, method.getAnnotation(SubscribeEvent.class).priority());
        data.target.setAccessible(true);
        if (registry.containsKey(event)) {
            if (!registry.get(event).contains(data)) {
                registry.get(event).add(data);
                sortList(event);
            }
        } else {
            registry.put(event, new ArrayList<EventData>() {
                {
                    add(data);
                }
            });
        }
    }

    private void registerReflection(Object instance, Method method) {
        method.setAccessible(true);
        if (!isBadMethod(method)) {
            register((Class<? extends Event>) method.getParameterTypes()[0], method, instance);
        }
    }

    private void registerInterface(Object instance, Class<?> clazz) {
        for (Class<?> inter : clazz.getInterfaces()) {
            Class<?> interSup = inter.getSuperclass();
            for (Method method : inter.getDeclaredMethods()) {
                registerReflection(instance, method);
            }

            while (interSup != null && (interSup = interSup.getSuperclass()) != null) {
                for (Method method : interSup.getDeclaredMethods()) {
                    registerReflection(instance, method);
                }
            }

            registerInterface(instance, inter);
        }
    }

    public void register(Object instance, SubscriberDepth depth) {
        Objects.requireNonNull(instance);
        Class<?> clazz = instance.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            registerReflection(instance, method);
        }

        if (depth == SubscriberDepth.SUPER) {
            for (Method method : clazz.getSuperclass().getDeclaredMethods()) {
                registerReflection(instance, method);
            }

            for (Class<?> inter : clazz.getInterfaces()) {
                for (Method method : inter.getDeclaredMethods()) {
                    registerReflection(instance, method);
                }
            }
        }

        if (depth == SubscriberDepth.DEEP_SUPER) {
            Class<?> sup = clazz.getSuperclass();
            for (Method method : sup.getDeclaredMethods()) {
                registerReflection(instance, method);
            }

            while ((sup = sup.getSuperclass()) != null) {
                for (Method method : sup.getDeclaredMethods()) {
                    registerReflection(instance, method);
                }
            }

            registerInterface(instance, clazz);
        }
    }

    public void register(Object instance) {
        register(instance, SubscriberDepth.NONE);
    }

    public <T extends Event> void register(Class<T> clazz, Consumer<T> processor) {
        try {
            PseudoSubscriber<T> subscriber = new PseudoSubscriber<>(processor);
            register(clazz, subscriber.getClass().getDeclaredMethod("onEvent", Event.class), subscriber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unregister(Object instance) {
        for (List<EventData> value : registry.values()) {
            value.removeIf(data -> data.source.equals(instance));
        }

        cleanRegistry();
    }

    public void post(Event event) {
        try {
            List<EventData> registered = registry.get(event.getClass());
            if (registered != null) {
                for (EventData data : registry.get(event.getClass())) {
                    data.target.invoke(data.source, event);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Class<? extends Event>, List<EventData>> getRegistry() {
        return registry;
    }
}