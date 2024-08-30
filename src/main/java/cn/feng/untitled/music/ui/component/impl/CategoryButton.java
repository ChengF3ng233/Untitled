package cn.feng.untitled.music.ui.component.impl;

import cn.feng.untitled.music.ui.MusicCategory;
import cn.feng.untitled.music.ui.ThemeColor;
import cn.feng.untitled.music.ui.component.Button;
import cn.feng.untitled.music.ui.gui.MusicPlayerGUI;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoUtil;
import cn.feng.untitled.util.render.ColorUtil;
import cn.feng.untitled.util.render.RenderUtil;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/8/13
 **/
@Getter
@Setter
public class CategoryButton extends Button {
    private final String text;
    private final MusicCategory category;
    private final MusicPlayerGUI gui;
    private boolean selected = false;

    public CategoryButton(String text, MusicCategory category, MusicPlayerGUI gui) {
        this.text = text;
        this.category = category;
        this.gui = gui;
        width = 80;
        height = 16;
    }

    @Override
    public void render() {
        float textY = posY + height / 2f;
        NanoUtil.drawRoundedRect(posX, posY, width, height, 3f, selected ? ThemeColor.redColor : (hovering ? new Color(50, 50, 50, 150) : ColorUtil.TRANSPARENT_COLOR));
        NanoFontLoader.misans.drawString(text, posX + 20f, textY, 17f, NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_MIDDLE, Color.WHITE);
        NanoUtil.drawImageRect(category.icon, posX + 3f, textY - 6f, 12, 12);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        boolean hovering = RenderUtil.hovering(mouseX, mouseY, posX, posY + height / 4f, width, height);
        if (hovering && button == 0) {
            selected = true;
        }
        if (!hovering) {
            selected = false;
        }
    }
}
