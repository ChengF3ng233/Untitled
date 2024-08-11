package cn.feng.untitled.ui.screen;

import cn.feng.untitled.Client;
import cn.feng.untitled.util.animation.advanced.Animation;
import cn.feng.untitled.util.animation.advanced.Direction;
import cn.feng.untitled.util.animation.advanced.impl.SmoothStepAnimation;
import cn.feng.untitled.util.render.ColorUtil;
import cn.feng.untitled.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/8/6
 **/
public class SplashScreen extends GuiScreen {
    private final GuiScreen current;
    private final GuiScreen target;
    private final Animation alphaAnim;

    private final Minecraft mc = Minecraft.getMinecraft();

    public SplashScreen(GuiScreen current, GuiScreen target) {
        this.current = current;
        this.target = target;

        alphaAnim = new SmoothStepAnimation(500, 1d);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (alphaAnim.finished(Direction.FORWARDS)) {
            current.onGuiClosed();

            if (target == Client.instance.uiManager.mainScreen) {
                mc.gameSettings.showDebugInfo = false;
                mc.ingameGUI.getChatGUI().clearChatMessages();
            }

            mc.setIngameNotInFocus();
            ScaledResolution scaledresolution = new ScaledResolution(mc);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            target.setWorldAndResolution(mc, i, j);
            mc.skipRenderWorld = false;

            alphaAnim.changeDirection();
        } else if (alphaAnim.getDirection() == Direction.FORWARDS) {
            current.drawScreen(mouseX, mouseY, partialTicks);
        }

        if (alphaAnim.finished(Direction.BACKWARDS)) {
            mc.displayScreen(target);
        } else if (alphaAnim.getDirection() == Direction.BACKWARDS) {
            target.drawScreen(mouseX, mouseY, partialTicks);
        }

        ScaledResolution sr = new ScaledResolution(mc);
        RenderUtil.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), ColorUtil.applyOpacity(Color.BLACK, alphaAnim.getOutput().floatValue()).getRGB());
    }
}
