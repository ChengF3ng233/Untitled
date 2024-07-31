package cn.feng.untitled.ui.clickgui.window.panel.impl;

import cn.feng.untitled.Client;
import cn.feng.untitled.module.Module;
import cn.feng.untitled.module.ModuleCategory;
import cn.feng.untitled.ui.clickgui.window.CategoryType;
import cn.feng.untitled.ui.clickgui.window.ThemeColor;
import cn.feng.untitled.ui.clickgui.window.panel.Panel;
import cn.feng.untitled.ui.font.FontLoader;
import cn.feng.untitled.util.render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/7/31
 **/
public class CategoryPanel extends Panel {
    public ModuleCategory category;
    public List<ModulePanel> modulePanelList;

    public CategoryPanel(ModuleCategory category) {
        this.category = category;
        width = 80f;
        height = 30f;
        modulePanelList = new ArrayList<>();

        for (Module module : Client.instance.moduleManager.getModuleByCategory(category)) {
            modulePanelList.add(new ModulePanel(module));
        }
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        this.height = FontLoader.greyCliff(18).height();

        FontLoader.greyCliff(18).drawString(category.name(), x, y, Color.WHITE.getRGB());
    }
}
