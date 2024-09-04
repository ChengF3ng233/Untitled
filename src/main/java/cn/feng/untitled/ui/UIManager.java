package cn.feng.untitled.ui;

import cn.feng.untitled.Client;
import cn.feng.untitled.event.api.SubscribeEvent;
import cn.feng.untitled.event.impl.ChatGUIEvent;
import cn.feng.untitled.event.impl.NanoEvent;
import cn.feng.untitled.event.impl.Render2DEvent;
import cn.feng.untitled.event.impl.ShaderEvent;
import cn.feng.untitled.event.type.EventPriority;
import cn.feng.untitled.ui.clickgui.ClickGUI;
import cn.feng.untitled.ui.screen.main.FlatMainScreen;
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
    public GuiScreen clickGUI;
    public GuiScreen mainScreen;

    public List<Widget> widgetList;

    public UIManager() {
        widgetList = new ArrayList<>();
    }

    public void registerWidgets() {
        register(new ArraylistWidget());
        register(new MusicInfoWidget());
        register(new MusicLyricWidget());
        register(new MusicVisualizerWidget());
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
        clickGUI = new ClickGUI();
        mainScreen = new FlatMainScreen();

        Client.instance.eventBus.register(clickGUI);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    private void onNano(NanoEvent event) {
        for (Widget widget : widgetList) {
            if (Client.instance.moduleManager.getModule(widget).enabled) {
                widget.updatePos();
                widget.render();
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
            if (Client.instance.moduleManager.getModule(widget).enabled && widget.dragging) {
                draggingWidget = widget;
                break;
            }
        }

        for (Widget widget : widgetList) {
            if (Client.instance.moduleManager.getModule(widget).enabled) {
                widget.onChatGUI(event.mouseX, event.mouseY, (draggingWidget == null || draggingWidget == widget));
                if (widget.dragging) draggingWidget = widget;
            }
        }
    }
}
