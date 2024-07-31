package cn.feng.untitled.ui;

import cn.feng.untitled.Client;
import cn.feng.untitled.event.api.SubscribeEvent;
import cn.feng.untitled.event.impl.Render2DEvent;
import cn.feng.untitled.ui.clickgui.dropdown.DropdownGUI;
import cn.feng.untitled.ui.clickgui.window.NeverLoseGUI;
import cn.feng.untitled.ui.hud.Widget;
import cn.feng.untitled.ui.hud.impl.TextWidget;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/7/30
 **/
public class UIManager {
    public GuiScreen neverLoseGUI;
    public GuiScreen dropdownGUI;

    public List<Widget> widgetList;

    public UIManager() {
        widgetList = new ArrayList<>();
    }

    public void registerWidgets() {
        register(new TextWidget());
    }

    private void register(Widget widget) {
        this.widgetList.add(widget);
        Client.instance.moduleManager.register(widget);
    }

    public void initGUI() {
        neverLoseGUI = new NeverLoseGUI();
        dropdownGUI = new DropdownGUI();
    }

    @SubscribeEvent
    private void onRender2D(Render2DEvent event) {
        for (Widget widget : widgetList) {
            if (Client.instance.moduleManager.getModule(widget).enabled)
                widget.render();
        }
    }
}
