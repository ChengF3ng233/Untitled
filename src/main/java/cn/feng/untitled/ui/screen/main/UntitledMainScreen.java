package cn.feng.untitled.ui.screen.main;

import cn.feng.untitled.ui.clickgui.neverlose.ThemeColor;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoLoader;
import cn.feng.untitled.ui.font.nano.NanoUtil;
import cn.feng.untitled.ui.screen.component.UntitledButton;
import cn.feng.untitled.util.animation.advanced.Direction;
import cn.feng.untitled.util.animation.advanced.composed.ColorAnimation;
import cn.feng.untitled.util.render.RenderUtil;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.opengl.GL11;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class UntitledMainScreen extends GuiScreen {

    private final float buttonWidth = 130f;
    private final float buttonHeight = 30f;
    private final List<UntitledButton> buttonList = new ArrayList<>();

    private final ColorAnimation colorAnim = new ColorAnimation(Color.WHITE, Color.GRAY, 200);

    public UntitledMainScreen() {
        buttonList.add(new UntitledButton("Single Player", () -> mc.displayGuiScreen(new GuiSelectWorld(this))));
        buttonList.add(new UntitledButton("Multi Player", () -> mc.displayGuiScreen(new GuiMultiplayer(this))));
        buttonList.add(new UntitledButton("Alt Manager", () -> JOptionPane.showMessageDialog(null, "我几把没写呢")));
        buttonList.add(new UntitledButton("Options", () -> mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings))));
        for (UntitledButton untitledButton : buttonList) {
            untitledButton.width = buttonWidth;
            untitledButton.height = buttonHeight;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        float centerX = sr.getScaledWidth() / 2f;
        float centerY = sr.getScaledHeight() / 2f;

        float offsetX = (mouseX - centerX) / sr.getScaledWidth();
        float offsetY = (mouseY - centerY) / sr.getScaledHeight();
        boolean hovering = RenderUtil.hovering(mouseX, mouseY, sr.getScaledWidth() - 40, 0, 40, 40);

        if (hovering && colorAnim.getDirection() == Direction.FORWARDS) {
            colorAnim.changeDirection();
        } else if (!hovering && colorAnim.getDirection() == Direction.BACKWARDS) {
            colorAnim.changeDirection();
        }

        RenderUtil.drawImage(new ResourceLocation("untitled/image/background.png"), 0, 0, sr.getScaledWidth(), sr.getScaledHeight());

        RenderUtil.drawImage(new ResourceLocation("untitled/icon/power.png"), sr.getScaledWidth() - 20, 8, 10, 10, colorAnim.getOutput());

        GL11.glPushMatrix();
        GL11.glTranslatef(offsetX * 5f, offsetY * 5f, 1f);

        NanoUtil.beginFrame();
        NanoVG.nvgTranslate(NanoLoader.vg, offsetX * 20f, offsetY * 20f);

        NanoFontLoader.script.drawGlowString("Untitled Client", centerX, (sr.getScaledHeight() / 2f) * 0.3f, 50f, 15f, NanoVG.NVG_ALIGN_CENTER, Color.WHITE, ThemeColor.focusedColor);
        NanoFontLoader.script.drawGlowString("Some birds are not meant to be caged, their feathers are just too bright.", centerX, sr.getScaledHeight() * 0.8f, 25f, 5f, NanoVG.NVG_ALIGN_CENTER, Color.WHITE, Color.WHITE);

        float gap = 10f;
        float buttonX, buttonY;
        int buttonIndex = 0;

        for (UntitledButton untitledButton : buttonList) {
            buttonX = buttonIndex % 2 == 0 ? sr.getScaledWidth() / 2f - gap / 2f - buttonWidth : sr.getScaledWidth() / 2f + gap / 2f;
            buttonY = buttonIndex <= 1 ? sr.getScaledHeight() / 2f - gap / 2f - buttonHeight : sr.getScaledHeight() / 2f + gap / 2f;
            untitledButton.draw(buttonX, buttonY, mouseX, mouseY);
            buttonIndex++;
        }

        NanoUtil.endFrame();

        GL11.glPopMatrix();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ScaledResolution sr = new ScaledResolution(mc);

        if (RenderUtil.hovering(mouseX, mouseY, sr.getScaledWidth() - 40, 0, 40, 40) && mouseButton == 0) {
            mc.shutdown();
        }

        for (UntitledButton untitledButton : buttonList) {
            untitledButton.onMouseClicked(mouseX, mouseY, mouseButton);
        }
    }
}
