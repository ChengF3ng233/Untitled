package cn.feng.untitled.ui.clickgui.window.panel.impl;

import cn.feng.untitled.Client;
import cn.feng.untitled.module.Module;
import cn.feng.untitled.ui.clickgui.window.NeverLoseGUI;
import cn.feng.untitled.ui.clickgui.window.ThemeColor;
import cn.feng.untitled.ui.clickgui.window.component.impl.ButtonComponent;
import cn.feng.untitled.ui.clickgui.window.panel.Panel;
import cn.feng.untitled.ui.font.FontLoader;
import cn.feng.untitled.ui.font.FontRenderer;
import cn.feng.untitled.util.misc.ChatUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import cn.feng.untitled.value.Value;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class ModulePanel extends Panel {
    private final List<ValuePanel> valuePanelList;
    private final ButtonComponent enableBtn;
    public Module module;

    public ModulePanel(Module module) {
        this.module = module;
        valuePanelList = new ArrayList<>();

        for (Value<?> value : module.valueList) {
            valuePanelList.add(new ValuePanel(value));
        }

        enableBtn = new ButtonComponent(module);
        height = 0f;
    }

    @Override
    public void init() {
        width = (NeverLoseGUI.width - NeverLoseGUI.leftWidth - 30f) / 2f;
        valuePanelList.forEach(ValuePanel::init);
        enableBtn.init();
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        FontRenderer font = FontLoader.rubik(15);

        final float gap = 5f;
        float panelY = y + font.height() + 3f;
        float contentX = x + 3f;
        float contentY = panelY + gap + 2f;

        // Module name
        font.drawString(module.name, contentX, y, ThemeColor.grayColor.getRGB());

        // Panel
        RoundedUtil.drawRoundOutline(x, panelY, width, height, 3f, 0.2f, ThemeColor.panelColor, ThemeColor.outlineColor);

        FontRenderer font2 = FontLoader.greyCliff(16);
        font2.drawString("Enabled", contentX, contentY, Color.WHITE.getRGB());
        enableBtn.draw(contentX, contentY, mouseX, mouseY);

        float valueY = contentY + enableBtn.height + gap;

        for (ValuePanel panel : valuePanelList) {
            panel.draw(contentX, valueY, mouseX, mouseY);
            if (panel.height != 0) valueY += panel.height + gap;
            if (valuePanelList.indexOf(panel) != valuePanelList.size() - 1) {
                RoundedUtil.drawRound(contentX, valueY - gap - 2.5f, width - 6f, 0.15f, 0f, ThemeColor.grayColor);
            }
        }

        height = valueY - panelY - gap;
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        valuePanelList.forEach(it -> it.onMouseClick(mouseX, mouseY, button));
        enableBtn.onMouseClick(mouseX, mouseY, button);
        Client.instance.configManager.saveConfigs();
    }
}
