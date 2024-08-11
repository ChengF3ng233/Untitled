package cn.feng.untitled.ui.widget.impl;

import cn.feng.untitled.Client;
import cn.feng.untitled.event.impl.ShaderEvent;
import cn.feng.untitled.module.Module;
import cn.feng.untitled.module.impl.client.PostProcessing;
import cn.feng.untitled.ui.font.awt.FontLoader;
import cn.feng.untitled.ui.font.awt.FontRenderer;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoFontRenderer;
import cn.feng.untitled.ui.font.nano.NanoLoader;
import cn.feng.untitled.ui.font.nano.NanoUtil;
import cn.feng.untitled.ui.widget.Widget;
import cn.feng.untitled.util.animation.advanced.Direction;
import cn.feng.untitled.util.data.compare.CompareMode;
import cn.feng.untitled.util.data.compare.ModuleComparator;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.value.impl.ColorValue;
import cn.feng.untitled.value.impl.NumberValue;
import net.minecraft.client.gui.Gui;
import org.lwjgl.nanovg.NanoVG;

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

    private final NumberValue fontSize = new NumberValue("FontSize", 16f, 30f, 15f, 0.5f);
    private final ColorValue textColor = new ColorValue("TextColor", Color.WHITE);
    private final ColorValue backgroundColor = new ColorValue("BackgroundColor", new Color(0, 0, 0, 100));
    private final NumberValue indexOffset = new NumberValue("IndexOffset", 6f, 20f, 0f, 1f);

    @Override
    public void onRender() {
        float renderX = sr.getScaledWidth() * x;
        float renderY = sr.getScaledHeight() * y;

        NanoFontRenderer font = NanoFontLoader.misans;
        font.setSize(fontSize.value.floatValue());

        List<Module> moduleList = new java.util.ArrayList<>(Client.instance.moduleManager.moduleList.stream().toList());
        moduleList.sort(new ModuleComparator(CompareMode.Length, font));

        float yGap = font.getSize() + 6f;
        float moduleY = renderY;
        int index = 0;

        float maxWidth = (font.getStringWidth(moduleList.get(0).name));

        RenderUtil.scissorStart(renderX, moduleY, (maxWidth + 6f), (sr.getScaledHeight() - moduleY));

      //  NanoUtil.beginFrame();
        NanoUtil.scissorStart(renderX, moduleY, maxWidth + 6f, sr.getScaledHeight() - moduleY);

        for (Module module : moduleList) {
            double moduleX = renderX + maxWidth - 6f - (font.getStringWidth(module.name)) * module.horizontalAnim.getOutput();

            Gui.drawNewRect(moduleX, moduleY, font.getStringWidth(module.name) + 6f, yGap, backgroundColor.getColor(index).getRGB());

            if (PostProcessing.bloom.value) {
                font.drawGlowString(module.name, (float) (moduleX + 3f),  moduleY, textColor.getColor(index), true);
            } else {
                font.drawString(module.name, (float) (moduleX + 3f),  moduleY, textColor.getColor(index), true);
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

        NanoUtil.scissorEnd();
 //       NanoUtil.endFrame();
        RenderUtil.scissorEnd();

        this.width = maxWidth;
        this.height = moduleY - renderY;
    }
}