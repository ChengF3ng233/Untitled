package cn.feng.untitled.ui.screen.component;

import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoUtil;
import cn.feng.untitled.util.animation.advanced.Animation;
import cn.feng.untitled.util.animation.advanced.Direction;
import cn.feng.untitled.util.animation.advanced.impl.SmoothStepAnimation;
import cn.feng.untitled.util.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/8/11
 **/
public class SkidButton {
    public float posX, posY, width, height;
    private final String text;
    public Runnable action;

    private final Animation xAnim = new SmoothStepAnimation(200, 1f, Direction.BACKWARDS);

    public SkidButton(String text, Runnable action) {
        this.text = text;
        this.action = action;
        this.width = NanoFontLoader.script.getStringWidth(text, 30f);
        this.height = 15f;
    }

    public void draw(float x, float y, int mouseX, int mouseY) {
        posX = x;
        posY = y;
        boolean hovering = RenderUtil.hovering(mouseX, mouseY, x, y + height / 4f, width, height);

        if (hovering && xAnim.getDirection() == Direction.BACKWARDS) {
            xAnim.changeDirection();
        } else if (!hovering && xAnim.getDirection() == Direction.FORWARDS) {
            xAnim.changeDirection();
        }

        float quadsWidth = 30f + NanoFontLoader.script.getStringWidth(text, 30f);
        Gui.drawNewRect(-quadsWidth * (1 - xAnim.getOutput().floatValue()), y + height + 3f, quadsWidth, 0.5f, Color.WHITE.getRGB());

        NanoUtil.transformStart(xAnim.getOutput().floatValue() * 5f, 0f);
        NanoFontLoader.script.drawGlowString(text, x, y, 30, Color.WHITE);
        NanoUtil.transformEnd();
    }

    public void onMouseClicked(int mouseX, int mouseY, int button) {
        boolean hovering = RenderUtil.hovering(mouseX, mouseY, posX, posY, width, height);
        if (hovering && button == 0) action.run();
    }
}
