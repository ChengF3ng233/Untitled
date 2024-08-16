package cn.feng.untitled.module.impl.render;

import cn.feng.untitled.module.Module;
import cn.feng.untitled.module.ModuleCategory;
import cn.feng.untitled.value.impl.BoolValue;
import cn.feng.untitled.value.impl.NumberValue;

/**
 * @author ChengFeng
 * @since 2024/8/16
 **/
public class Animations extends Module {
    public Animations() {
        super("Animations", ModuleCategory.Render);
        locked = true;
    }

    public static final BoolValue translate = new BoolValue("Translate", false);
    public static final NumberValue translateX = new NumberValue("TranslateX", 0f, 2f, -2f, 0.005f);
    public static final NumberValue translateY = new NumberValue("TranslateY", 0f, 2f, -2f, 0.005f);
    public static final NumberValue translateZ = new NumberValue("TranslateZ", 0f, 2f, -2f, 0.005f);

    public static final BoolValue itemAnim = new BoolValue("ItemAnimation", true);
    public static final BoolValue everythingBlock = new BoolValue("EverythingBlock", true);
}
