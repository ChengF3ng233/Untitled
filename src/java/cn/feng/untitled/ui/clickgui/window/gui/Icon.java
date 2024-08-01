package cn.feng.untitled.ui.clickgui.window.gui;

import cn.feng.untitled.util.animation.advanced.Direction;
import cn.feng.untitled.util.animation.advanced.composed.ColorAnimation;
import cn.feng.untitled.util.render.RenderUtil;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/8/1
 **/
public class Icon {
    private final ResourceLocation resource;
    public float x, y;
    public final float size;
    private final ColorAnimation colorAnim;

    public Icon(ResourceLocation resource, float size) {
        this.resource = resource;
        this.size = size;
        this.colorAnim = new ColorAnimation(new Color(255, 255, 255, 70), Color.WHITE, 100);
    }

    public void draw(float x, float y, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        boolean hovered = RenderUtil.hovering(mouseX, mouseY, this.x, this.y, size, size);
        if (hovered) {
            if (colorAnim.getDirection() == Direction.BACKWARDS) colorAnim.changeDirection();
        } else if (colorAnim.getDirection() == Direction.FORWARDS) colorAnim.changeDirection();
        RenderUtil.drawImage(resource, this.x, this.y, size, size, colorAnim.getOutput());
    }
}
