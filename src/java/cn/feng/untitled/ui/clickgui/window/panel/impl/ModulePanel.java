package cn.feng.untitled.ui.clickgui.window.panel.impl;

import cn.feng.untitled.module.Module;
import cn.feng.untitled.ui.clickgui.window.NeverLoseGUI;
import cn.feng.untitled.ui.clickgui.window.ThemeColor;
import cn.feng.untitled.ui.clickgui.window.panel.Panel;
import cn.feng.untitled.ui.font.FontLoader;
import cn.feng.untitled.ui.font.FontRenderer;
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
    public Module module;

    public ModulePanel(Module module) {
        this.module = module;
        this.width = (NeverLoseGUI.width - NeverLoseGUI.leftWidth - 30f) / 2f;
        this.height = 10f;
        valuePanelList = new ArrayList<>();

        for (Value<?> value : module.valueList) {
            valuePanelList.add(new ValuePanel(value));
        }
    }

    @Override
    public void init() {
        valuePanelList.forEach(ValuePanel::init);
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        FontRenderer font = FontLoader.rubik(15);
        font.drawString(module.name, x + 5f, y, ThemeColor.grayColor.getRGB());
        RoundedUtil.drawRound(x, y + font.height() + 3f, width, height, 3f, ThemeColor.panelColor);

        FontRenderer font2 = FontLoader.greyCliff(16);
        font2.drawString("Enabled", x + 5f, y + font.height() + 5f, Color.WHITE.getRGB());
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        valuePanelList.forEach(it -> it.onMouseClick(mouseX, mouseY, button));
    }
}
