package cn.feng.untitled.module.impl.client;

import cn.feng.untitled.module.Module;
import cn.feng.untitled.module.ModuleCategory;
import cn.feng.untitled.value.impl.BoolValue;
import cn.feng.untitled.value.impl.NumberValue;

/**
 * @author ChengFeng
 * @since 2024/8/8
 **/
public class EntityCullingMod extends Module {
    public EntityCullingMod() {
        super("EntityCulling", ModuleCategory.Client);
    }

    public static final BoolValue through = new BoolValue("NameTagThroughWalls", true);
    public static final BoolValue armorStands = new BoolValue("SkipArmorStands", true);
    public static final NumberValue tracingDist = new NumberValue("TracingDistance", 128f, 256f, 64f, 1f);
    public static final NumberValue sleepDelay = new NumberValue("SleepDelay", 10f, 20f, 5f, 1f);
    public static final NumberValue hitboxLimit = new NumberValue("HitboxLimit", 50f, 80f, 30f, 1f);
}
