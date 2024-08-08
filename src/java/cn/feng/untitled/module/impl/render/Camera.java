package cn.feng.untitled.module.impl.render;

import cn.feng.untitled.event.api.SubscribeEvent;
import cn.feng.untitled.event.impl.UpdateEvent;
import cn.feng.untitled.module.Module;
import cn.feng.untitled.module.ModuleCategory;
import cn.feng.untitled.util.misc.ChatUtil;
import cn.feng.untitled.value.impl.BoolValue;
import cn.feng.untitled.value.impl.NumberValue;

/**
 * @author ChengFeng
 * @since 2024/8/8
 **/
public class Camera extends Module {
    public Camera() {
        super("Camera", ModuleCategory.Render);
        if (!enabled)
            toggle();
        locked = true;
    }

    public static final BoolValue clip = new BoolValue("CameraClip", false);
    public static final BoolValue animation = new BoolValue("CameraAnimation", true);
    public static final BoolValue motion = new BoolValue("MotionCamera", false);

    public static final BoolValue transform = new BoolValue("Transform", false);
    public static final NumberValue x = new NumberValue("TransformX", 0f, 5f, 0f, 0.1f);
    public static final NumberValue y = new NumberValue("TransformY", 0f, 5f, 0f, 0.1f);
    public static final NumberValue z = new NumberValue("TransformZ", 0f, 5f, 0f, 0.1f);

    @SubscribeEvent
    private void onUpdate(UpdateEvent event) {

    }
}
