package cn.feng.untitled.ui;

import cn.feng.untitled.Client;
import cn.feng.untitled.event.api.SubscribeEvent;
import cn.feng.untitled.event.impl.ChatGUIEvent;
import cn.feng.untitled.event.impl.NanoEvent;
import cn.feng.untitled.event.impl.Render2DEvent;
import cn.feng.untitled.event.impl.ShaderEvent;
import cn.feng.untitled.ui.clickgui.dropdown.DropdownGUI;
import cn.feng.untitled.ui.clickgui.neverlose.NeverLoseGUI;
import cn.feng.untitled.ui.screen.main.SkidMainScreen;
import cn.feng.untitled.ui.widget.Widget;
import cn.feng.untitled.ui.widget.impl.ArraylistWidget;
import cn.feng.untitled.ui.widget.impl.MusicInfoWidget;
import cn.feng.untitled.ui.widget.impl.MusicLyricWidget;
import cn.feng.untitled.ui.widget.impl.MusicVisualizerWidget;
import cn.feng.untitled.util.MinecraftInstance;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/7/30
 **/
public class UIManager extends MinecraftInstance {
    public GuiScreen neverLoseGUI;
    public GuiScreen dropdownGUI;
    public GuiScreen mainScreen;

    public List<Widget> widgetList;

    public UIManager() {
        widgetList = new ArrayList<>();
    }

    public void registerWidgets() {
        register(new ArraylistWidget());
        register(new MusicVisualizerWidget());
        register(new MusicLyricWidget());
        register(new MusicInfoWidget());
    }

    public Widget getWidget(Class<? extends Widget> w) {
        for (Widget widget : widgetList) {
            if (widget.getClass() == w) return widget;
        }

        return null;
    }

    private void register(Widget widget) {
        this.widgetList.add(widget);
        Client.instance.moduleManager.register(widget);
    }

    public void initGUI() {
        neverLoseGUI = new NeverLoseGUI();
        dropdownGUI = new DropdownGUI();
        mainScreen = new SkidMainScreen();

        Client.instance.eventBus.register(neverLoseGUI);
    }

    @SubscribeEvent
    private void onRender2D(Render2DEvent e) {
        for (Widget widget : widgetList) {
            if (Client.instance.moduleManager.getModule(widget).enabled) {
                widget.updatePos();
                widget.onRender2D();
            }
        }
    }

    @SubscribeEvent
    private void onNano(NanoEvent event) {
        for (Widget widget : widgetList) {
            if (Client.instance.moduleManager.getModule(widget).enabled) {
                widget.updatePos();
                widget.onNano();
            }
        }
    }

    @SubscribeEvent
    private void onShader(ShaderEvent event) {
        for (Widget widget : widgetList) {
            if (Client.instance.moduleManager.getModule(widget).enabled) {
                widget.onShader(event);
            }
        }
    }

    @SubscribeEvent
    private void onChatGUI(ChatGUIEvent event) {
        Widget draggingWidget = null;
        for (Widget widget : widgetList) {
            if (Client.instance.moduleManager.getModule(widget).enabled) {
                if (widget.dragging) {
                    draggingWidget = widget;
                    break;
                }
            }
        }
        for (Widget widget : widgetList) {
            if (Client.instance.moduleManager.getModule(widget).enabled) {
                widget.onChatGUI(event.mouseX, event.mouseY, (draggingWidget == null || draggingWidget == widget));
            }
        }
    }
}
