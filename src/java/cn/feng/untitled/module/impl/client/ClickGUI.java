package cn.feng.untitled.module.impl.client;

import cn.feng.untitled.Client;
import cn.feng.untitled.module.Module;
import cn.feng.untitled.module.ModuleCategory;
import cn.feng.untitled.value.impl.ModeValue;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class ClickGUI extends Module {
    public ModeValue mode = new ModeValue("Mode", "NeverLose", new String[]{"NeverLose", "Dropdown"});

    public ClickGUI() {
        super("ClickGUI", ModuleCategory.Client, Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEnable() {
        switch (mode.value) {
            case "Dropdown":
                mc.displayGuiScreen(Client.instance.uiManager.dropdownGUI);
                break;
            case "Window":
            default:
                mc.displayGuiScreen(Client.instance.uiManager.neverLoseGUI);
                break;
        }
        enabled = false;
    }
}
