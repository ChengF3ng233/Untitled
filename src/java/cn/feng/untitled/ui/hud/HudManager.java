package cn.feng.untitled.ui.hud;

import cn.feng.untitled.ui.clickgui.dropdown.DropdownGUI;
import cn.feng.untitled.ui.clickgui.window.WindowGUI;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class HudManager {
    public GuiScreen windowGUI;
    public GuiScreen dropdownGUI;

    public void initGUI() {
        windowGUI = new WindowGUI();
        dropdownGUI = new DropdownGUI();
    }
}
