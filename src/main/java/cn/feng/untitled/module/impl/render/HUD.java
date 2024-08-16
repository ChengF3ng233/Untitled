package cn.feng.untitled.module.impl.render;

import cn.feng.untitled.module.Module;
import cn.feng.untitled.module.ModuleCategory;
import cn.feng.untitled.value.impl.BoolValue;

/**
 * This module only have one instance, so all fields are static.
 * @author ChengFeng
 * @since 2024/7/29
 **/
public class HUD extends Module {
    public HUD() {
        super("HUD", ModuleCategory.Render, true);
    }

    public static final BoolValue fancyFont = new BoolValue("FancyFont", true);
    public static final BoolValue crossHair = new BoolValue("CrossHair", true);
}
