package cn.feng.untitled.ui.widget.impl;

import cn.feng.untitled.Client;
import cn.feng.untitled.module.Module;
import cn.feng.untitled.ui.font.FontLoader;
import cn.feng.untitled.ui.font.FontRenderer;
import cn.feng.untitled.ui.widget.Widget;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/8/1
 **/
public class ArraylistWidget extends Widget {
    public ArraylistWidget() {
        super("Arraylist", true);

        ScaledResolution sr = new ScaledResolution(mc);
        this.x = sr.getScaledWidth() - 2f;
        this.y = 2f;
    }

    @Override
    public void render() {
        List<Module> enabledModules = Client.instance.moduleManager.getModuleByState(true);

        float moduleY = y;
        for (Module module : enabledModules) {
            FontRenderer font = FontLoader.greyCliff(18);
            font.drawString(module.name, x - font.getStringWidth(module.name), moduleY, Color.WHITE.getRGB());
            moduleY += font.height() + 2f;
        }
    }
}
