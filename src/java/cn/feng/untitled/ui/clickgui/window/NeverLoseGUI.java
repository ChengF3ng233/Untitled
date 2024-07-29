package cn.feng.untitled.ui.clickgui.window;

import cn.feng.untitled.util.animation.Animation;
import cn.feng.untitled.util.animation.Direction;
import cn.feng.untitled.util.animation.impl.SmoothStepAnimation;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import cn.feng.untitled.util.render.StencilUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class NeverLoseGUI extends GuiScreen {
    private float x, y, width, height, leftWidth, topWidth, radius;
    private boolean dragging;
    private float dragX, dragY;
    private Animation windowAnim;

    @Override
    public void initGui() {
        width = 420f;
        height = 310f;
        leftWidth = 90f;
        topWidth = 35f;
        radius = 4f;
        dragging = false;

        ScaledResolution sr = new ScaledResolution(mc);
        x = sr.getScaledWidth() / 2f - width / 2f;
        y = sr.getScaledHeight() / 2f - height / 2f;
        windowAnim = new SmoothStepAnimation(150, 1d);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (windowAnim.finished(Direction.BACKWARDS)) mc.displayGuiScreen(null);

        // Drag
        if (dragging) {
            x += mouseX - dragX;
            y += mouseY - dragY;
            dragX = mouseX;
            dragY = mouseY;
        }

        RenderUtil.scaleStart(x + width / 2, y + height / 2, windowAnim.getOutput().floatValue());

        // Window
        RoundedUtil.drawRound(x, y, width, height, radius, ThemeColor.windowColor);

        // Category
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(x - 1f, y + 1f, leftWidth + radius, height + 1f, radius, ThemeColor.categoryColor);
        StencilUtil.readStencilBuffer(1);
        RenderUtil.scissorStart(x, y, leftWidth, height);
        RoundedUtil.drawRoundOutline(x - radius, y - radius, leftWidth + radius + 0.4f, height + radius * 2, radius, 0.2f, ThemeColor.categoryColor, ThemeColor.outlineColor);
        RenderUtil.scissorEnd();
        StencilUtil.uninitStencilBuffer();

        // Top
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(x + leftWidth, y, width - leftWidth, topWidth, radius, Color.BLACK);
        StencilUtil.readStencilBuffer(1);
        RenderUtil.scissorStart(x + leftWidth, y, width - leftWidth, topWidth);
        RoundedUtil.drawRoundOutline(x + leftWidth - radius, y - radius, width - leftWidth + radius * 2, topWidth + radius, radius, 0.2f, ThemeColor.titleColor, ThemeColor.outlineColor);
        RenderUtil.scissorEnd();
        StencilUtil.uninitStencilBuffer();

        //Title
        float gap = 2f;

        RenderUtil.scaleEnd();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            windowAnim.changeDirection();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (RenderUtil.hovering(mouseX, mouseY, x, y, width, topWidth)) {
            dragging = true;
            dragX = mouseX;
            dragY = mouseY;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
