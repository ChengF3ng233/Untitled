package cn.feng.untitled.ui.clickgui.gui;

import cn.feng.untitled.ui.clickgui.ThemeColor;
import cn.feng.untitled.ui.font.awt.AWTFont;
import cn.feng.untitled.ui.font.awt.CenterType;
import cn.feng.untitled.util.animation.advanced.Direction;
import cn.feng.untitled.util.animation.advanced.composed.ColorAnimation;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/8/1
 **/
public class IconButton {
    public float x, y, width, height;
    private final AWTFont AWTFont;
    private final int size;
    private final String text;
    private final ResourceLocation resource;
    private final float gap;

    private ColorAnimation colAnim;

    public IconButton(AWTFont AWTFont, float height, String text, ResourceLocation resource, int size, float gap) {
        this.AWTFont = AWTFont;
        this.height = height;
        this.text = text;
        this.resource = resource;
        this.size = size;
        this.gap = gap;

        this.width = 4 * gap + size + AWTFont.getStringWidth(text);
        colAnim = new ColorAnimation(ThemeColor.barColor, ThemeColor.focusedColor, 200);
        colAnim.changeDirection();
    }

    public void draw(float x, float y, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        boolean hovered = RenderUtil.hovering(mouseX, mouseY, this.x, this.y, width, height);
        if (hovered) {
            if (colAnim.getDirection() == Direction.BACKWARDS) colAnim.changeDirection();
        } else if (colAnim.getDirection() == Direction.FORWARDS) colAnim.changeDirection();
        RoundedUtil.drawRound(this.x, this.y, width, height, 3f, colAnim.getOutput());
        RenderUtil.drawImage(resource, this.x + gap, this.y + gap / 2f, size, size);
        AWTFont.drawCenteredString(text, this.x + 2 * gap + size, this.y + height / 2f, Color.WHITE.getRGB(), CenterType.Vertical, true);
    }
}
