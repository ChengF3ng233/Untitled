package cn.feng.untitled.module;

import cn.feng.untitled.Client;
import cn.feng.untitled.util.MinecraftInstance;
import cn.feng.untitled.value.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class Module extends MinecraftInstance {
    public String name;
    public ModuleCategory category;
    public int key;
    public boolean enabled;
    public boolean splittable = true;
    public List<Value<?>> valueList = new ArrayList<>();

    public Module(String name, ModuleCategory category, int key, boolean enabled) {
        this.name = name;
        this.category = category;
        this.key = key;
        this.enabled = enabled;
    }

    public Module(String name, ModuleCategory category, boolean enabled) {
        this.name = name;
        this.category = category;
        this.enabled = enabled;
        this.key = -1;
    }

    public Module(String name, ModuleCategory category, int key) {
        this.name = name;
        this.category = category;
        this.key = key;
        this.enabled = false;
    }

    public Module(String name, ModuleCategory category) {
        this.name = name;
        this.category = category;
        this.enabled = false;
        this.key = -1;
    }

    public void onEnable() {

    }

    public void onDisable() {

    }

    public void toggle() {
        this.enabled = !this.enabled;
        if (enabled) {
            onEnable();
            Client.instance.eventBus.register(this);
        } else {
            onDisable();
            Client.instance.eventBus.unregister(this);
        }
    }
}
