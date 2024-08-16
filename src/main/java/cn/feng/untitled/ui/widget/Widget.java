package cn.feng.untitled.ui.widget;

import cn.feng.untitled.event.impl.ShaderEvent;
import cn.feng.untitled.ui.clickgui.neverlose.ThemeColor;
import cn.feng.untitled.ui.font.awt.FontLoader;
import cn.feng.untitled.util.MinecraftInstance;
import cn.feng.untitled.util.animation.advanced.Direction;
import cn.feng.untitled.util.animation.advanced.composed.ColorAnimation;
import cn.feng.untitled.util.render.ColorUtil;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public abstract class Widget extends MinecraftInstance {
    public String name;
    /**
     * x, y都使用相对位置 （百分比），防止因窗口缩放导致组件乱动
     */
    public float x, y;
    public float width, height;
    public final boolean defaultOn;
    public boolean dragging;
    private int dragX, dragY;
    protected ScaledResolution sr = new ScaledResolution(mc);
    private final ColorAnimation colorAnim;

    public Widget(String name, boolean defaultOn) {
        this.name = name;
        this.defaultOn = defaultOn;
        this.x = 0f;
        this.y = 0f;
        this.width = 100f;
        this.height = 100f;

        colorAnim = new ColorAnimation(Color.WHITE, ThemeColor.grayColor, 100);
    }

    public void onShader(ShaderEvent event) {

    }

    public void onNano() {

    }

    public void onRender2D() {

    }

    public void updatePos() {
        sr = new ScaledResolution(mc);
        float renderX = x * sr.getScaledWidth();
        float renderY = y * sr.getScaledHeight();
        if (renderX < 0f) x = 0f;
        if (renderX > sr.getScaledWidth() - width) x = (sr.getScaledWidth() - width) / sr.getScaledWidth();
        if (renderY < 0f) y = 0f;
        if (renderY > sr.getScaledHeight() - height) y = (sr.getScaledHeight() - height) / sr.getScaledHeight();
    }

    public final void onChatGUI(int mouseX, int mouseY, boolean drag) {
        float renderX = x * sr.getScaledWidth();
        float renderY = y * sr.getScaledHeight();

        boolean hovering = RenderUtil.hovering(mouseX, mouseY, renderX, renderY, width, height);

        if (hovering && colorAnim.getDirection() == Direction.FORWARDS && drag) {
            colorAnim.changeDirection();
        } else if (!hovering && colorAnim.getDirection() == Direction.BACKWARDS) {
            colorAnim.changeDirection();
        }

        FontLoader.rubik(16).drawString(name, renderX, renderY - FontLoader.rubik(16).getFontHeight() - 3f, colorAnim.getOutput().getRGB(), true);
        RoundedUtil.drawRoundOutline(renderX, renderY, width, height, 3f, 0.2f, ColorUtil.TRANSPARENT_COLOR, colorAnim.getOutput());

        if (hovering && Mouse.isButtonDown(0) && !dragging && drag) {
             dragging = true;
             dragX = mouseX;
             dragY = mouseY;
        }

        if (!Mouse.isButtonDown(0)) dragging = false;

        if (dragging) {
            float deltaX = (float) (mouseX - dragX) / sr.getScaledWidth();
            float deltaY = (float) (mouseY - dragY) / sr.getScaledHeight();

            x += deltaX;
            y += deltaY;

            dragX = mouseX;
            dragY = mouseY;
        }
    }
}
