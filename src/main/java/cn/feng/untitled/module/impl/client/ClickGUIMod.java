package cn.feng.untitled.module.impl.client;

import cn.feng.untitled.Client;
import cn.feng.untitled.module.Module;
import cn.feng.untitled.module.ModuleCategory;
import org.lwjgl.input.Keyboard;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class ClickGUIMod extends Module {
    public ClickGUIMod() {
        super("ClickGUI", ModuleCategory.Client, Keyboard.KEY_RSHIFT);
        locked = true;
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Client.instance.uiManager.clickGUI);
    }
}
