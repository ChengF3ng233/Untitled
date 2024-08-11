package cn.feng.untitled.module.impl.render;

import cn.feng.untitled.module.Module;
import cn.feng.untitled.module.ModuleCategory;
import cn.feng.untitled.value.impl.BoolValue;
import cn.feng.untitled.value.impl.NumberValue;

/**
 * @author ChengFeng
 * @since 2024/8/8
 **/
public class Camera extends Module {
    public static final BoolValue clip = new BoolValue("Clip", false);
    public static final BoolValue animation = new BoolValue("Animation", true);
    public static final BoolValue motion = new BoolValue("Motion", false);
    public static final NumberValue interpolation = new NumberValue("MotionInterpolation", 0.15f, 0.5f, 0.05f, 0.05f);
    public static final BoolValue transform = new BoolValue("Transform", false);
    public static final NumberValue x = new NumberValue("TransformX", 0f, 5f, -5f, 0.1f);
    public static final NumberValue y = new NumberValue("TransformY", 0f, 5f, 0f, 0.1f);
    public static final NumberValue z = new NumberValue("TransformZ", 0f, 5f, -5f, 0.1f);

    public Camera() {
        super("Camera", ModuleCategory.Render);
        if (!enabled)
            toggle();
        locked = true;

        motion.toggleAction = () -> {
            if (motion.value) {
                mc.entityRenderer.prevRenderX = mc.getRenderViewEntity().posX;
                mc.entityRenderer.prevRenderY = mc.getRenderViewEntity().posY + mc.getRenderViewEntity().getEyeHeight();
                mc.entityRenderer.prevRenderZ = mc.getRenderViewEntity().posZ;
            }
        };
    }
}
