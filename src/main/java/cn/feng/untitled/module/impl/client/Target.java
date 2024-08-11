package cn.feng.untitled.module.impl.client;

import cn.feng.untitled.module.Module;
import cn.feng.untitled.module.ModuleCategory;
import cn.feng.untitled.value.impl.BoolValue;

/**
 * @author ChengFeng
 * @since 2024/8/5
 **/
public class Target extends Module {
    public Target() {
        super("Target", ModuleCategory.Client);
        locked = true;
    }

    public static final BoolValue players = new BoolValue("Players", true);
    public static final BoolValue mobs = new BoolValue("Mobs", true);
    public static final BoolValue animals = new BoolValue("Animals", true);
}
