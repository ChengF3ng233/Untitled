package cn.feng.untitled.ui.clickgui.window;

import cn.feng.untitled.util.animation.Animation;
import cn.feng.untitled.util.animation.Direction;
import cn.feng.untitled.util.animation.impl.SmoothStepAnimation;
import cn.feng.untitled.util.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class WindowGUI extends GuiScreen {
    private float x, y, width, height;
    private Animation windowAnim;

    @Override
    public void initGui() {
        width = 400f;
        height = 250f;

        ScaledResolution sr = new ScaledResolution(mc);
        x = sr.getScaledWidth() / 2f - width / 2f;
        y = sr.getScaledHeight() / 2f - height / 2f;
        windowAnim = new SmoothStepAnimation(150, 1d);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (windowAnim.finished(Direction.BACKWARDS)) mc.displayGuiScreen(null);

        RenderUtil.scaleStart(x - 3, y - 3, windowAnim.getOutput().floatValue());

        RenderUtil.scaleEnd();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            windowAnim.changeDirection();
        }
    }
}
