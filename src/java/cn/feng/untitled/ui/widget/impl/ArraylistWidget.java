package cn.feng.untitled.ui.widget.impl;

import cn.feng.untitled.Client;
import cn.feng.untitled.event.impl.ShaderEvent;
import cn.feng.untitled.module.Module;
import cn.feng.untitled.ui.font.awt.FontLoader;
import cn.feng.untitled.ui.font.awt.FontRenderer;
import cn.feng.untitled.ui.widget.Widget;
import cn.feng.untitled.util.animation.advanced.Direction;
import cn.feng.untitled.util.data.compare.CompareMode;
import cn.feng.untitled.util.data.compare.ModuleComparator;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.value.impl.ColorValue;
import cn.feng.untitled.value.impl.NumberValue;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/8/1
 **/
public class ArraylistWidget extends Widget {
    public ArraylistWidget() {
        super("Arraylist", true);

        this.x = 1f;
        this.y = 0f;
    }

    private final ColorValue textColor = new ColorValue("TextColor", Color.WHITE);
    private final ColorValue backgroundColor = new ColorValue("BackgroundColor", new Color(0, 0, 0, 100));
    private final NumberValue indexOffset = new NumberValue("IndexOffset", 6f, 20f, 0f, 1f);

    @Override
    public void onShader(ShaderEvent event) {
        render(event.bloom, !event.bloom);
    }

    @Override
    public void onRender() {
        render(true, false);
    }

    private void render(boolean drawStr, boolean blur) {
        float renderX = sr.getScaledWidth() * x;
        float renderY = sr.getScaledHeight() * y;

        FontRenderer font = FontLoader.greyCliff(20);
        List<Module> moduleList = new java.util.ArrayList<>(Client.instance.moduleManager.moduleList.stream().toList());
        moduleList.sort(new ModuleComparator(CompareMode.Length, font));

        float yGap = font.getFontHeight() + 6f;
        float moduleY = renderY;
        int index = 0;

        float maxWidth = (font.getStringWidth(moduleList.get(0).name)) + 6f;

        RenderUtil.scissorStart(renderX, moduleY, maxWidth, sr.getScaledHeight() - moduleY);

        for (Module module : moduleList) {
            double moduleX = renderX + maxWidth - 6f - (font.getStringWidth(module.name)) * module.horizontalAnim.getOutput();

            Gui.drawNewRect(moduleX, moduleY, font.getStringWidth(module.name) + 6f, yGap, blur? Color.BLACK.getRGB() : backgroundColor.getColor(index).getRGB());

            if (drawStr) {
                font.drawString(module.name, moduleX + 3f, moduleY + 2f, textColor.getColor(index).getRGB(), true);
            }

            moduleY += module.verticalAnim.getOutput().floatValue() * yGap;

            if (!module.enabled && module.horizontalAnim.getAnimation().finished(Direction.BACKWARDS) && module.verticalAnim.getDirection() == Direction.FORWARDS) {
                module.verticalAnim.changeDirection();
            }

            if (module.enabled && module.verticalAnim.finished(Direction.FORWARDS) && module.horizontalAnim.getDirection() == Direction.BACKWARDS) {
                module.horizontalAnim.changeDirection();
            }

            if (module.enabled) index += indexOffset.value.intValue();
        }

        RenderUtil.scissorEnd();

        this.width = maxWidth;
        this.height = moduleY - renderY;
    }
}
