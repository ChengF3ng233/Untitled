package cn.feng.untitled.module.impl.movement;

import cn.feng.untitled.event.type.EventType;
import cn.feng.untitled.event.api.SubscribeEvent;
import cn.feng.untitled.event.impl.MotionEvent;
import cn.feng.untitled.module.Module;
import cn.feng.untitled.module.ModuleCategory;
import org.lwjgl.input.Keyboard;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class ToggleSprint extends Module {
    public ToggleSprint() {
        super("ToggleSprint", ModuleCategory.MOVEMENT, Keyboard.getKeyIndex("V"));
    }

    @SubscribeEvent
    private void onMotion(MotionEvent event) {
        if (event.getEventType() == EventType.POST || mc.thePlayer.movementInput.moveForward <= 0) return;

        mc.thePlayer.setSprinting(true);
    }

    @Override
    public void onDisable() {
        mc.thePlayer.setSprinting(false);
    }
}
