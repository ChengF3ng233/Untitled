package cn.feng.untitled.ui.widget.impl;

import cn.feng.untitled.Client;
import cn.feng.untitled.module.Module;
import cn.feng.untitled.module.impl.client.PostProcessing;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoFontRenderer;
import cn.feng.untitled.util.render.nano.NanoUtil;
import cn.feng.untitled.ui.widget.Widget;
import cn.feng.untitled.util.animation.advanced.Direction;
import cn.feng.untitled.util.data.compare.CompareMode;
import cn.feng.untitled.util.data.compare.ModuleComparator;
import cn.feng.untitled.value.impl.ColorValue;
import cn.feng.untitled.value.impl.NumberValue;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/8/1
 **/
public class ArraylistWidget extends Widget {
    public ArraylistWidget() {
        super("Arraylist", false);

        this.x = 1f;
        this.y = 0f;
    }

    private final NumberValue fontSize = new NumberValue("FontSize", 16f, 30f, 15f, 0.5f);
    private final ColorValue textColor = new ColorValue("TextColor", Color.WHITE);
    private final ColorValue backgroundColor = new ColorValue("BackgroundColor", new Color(0, 0, 0, 100));
    private final NumberValue indexOffset = new NumberValue("IndexOffset", 6f, 20f, 0f, 1f);

    @Override
    public void render() {
        super.render();
        NanoFontRenderer font = NanoFontLoader.misans;

        List<Module> moduleList = new java.util.ArrayList<>(Client.instance.moduleManager.moduleList.stream().toList());
        moduleList.sort(new ModuleComparator(CompareMode.Length, font));

        float yGap = font.getHeight(fontSize.getValue().floatValue()) + 6f;
        float moduleY = renderY + yGap / 2f;
        int index = 0;

        float maxWidth = (font.getStringWidth(moduleList.get(0).name));

        NanoUtil.scissorStart(renderX, renderY, maxWidth + 6f, sr.getScaledHeight() - renderY);

        for (Module module : moduleList) {
            double moduleX = renderX + maxWidth - 6f - (font.getStringWidth(module.name)) * module.horizontalAnim.getOutput();

            NanoUtil.drawRect((float) moduleX, moduleY - yGap / 2f, font.getStringWidth(module.name) + 6f, yGap, backgroundColor.getValue(index));
            if (PostProcessing.bloom.getValue()) {
                font.drawGlowString(module.name, (float) (moduleX + 3f), moduleY, fontSize.getValue().floatValue(), NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_MIDDLE, textColor.getValue(index));
            } else {
                font.drawString(module.name, (float) (moduleX + 3f), moduleY, fontSize.getValue().floatValue(), NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_MIDDLE, textColor.getValue(index), true);
            }

            moduleY += module.verticalAnim.getOutput().floatValue() * yGap;

            if (!module.enabled && module.horizontalAnim.getAnimation().finished(Direction.BACKWARDS) && module.verticalAnim.getDirection() == Direction.FORWARDS) {
                module.verticalAnim.changeDirection();
            }

            if (module.enabled && module.verticalAnim.finished(Direction.FORWARDS) && module.horizontalAnim.getDirection() == Direction.BACKWARDS) {
                module.horizontalAnim.changeDirection();
            }

            if (module.enabled) index += indexOffset.getValue().intValue();
        }

        NanoUtil.scissorEnd();

        this.width = maxWidth;
        this.height = moduleY - renderY;
    }
}
