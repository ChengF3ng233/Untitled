package cn.feng.untitled.module.impl.render;

import cn.feng.untitled.module.Module;
import cn.feng.untitled.module.ModuleCategory;
import cn.feng.untitled.value.impl.BoolValue;

/**
 * @author ChengFeng
 * @since 2024/8/8
 **/
public class Camera extends Module {
    public Camera() {
        super("Camera", ModuleCategory.Render);
        locked = true;
    }

    public static final BoolValue clip = new BoolValue("CameraClip", false);
    public static final BoolValue animation = new BoolValue("CameraAnimation", true);
    public static final BoolValue motion = new BoolValue("MotionCamera", false);
}
