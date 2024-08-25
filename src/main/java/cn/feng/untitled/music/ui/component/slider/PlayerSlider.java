package cn.feng.untitled.music.ui.component.slider;

import cn.feng.untitled.Client;
import cn.feng.untitled.music.ui.ThemeColor;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.Display;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;

import static cn.feng.untitled.util.data.TimeUtil.formatTime;

/**
 * @author ChengFeng
 * @since 2024/8/14
 **/
public class PlayerSlider {
    private boolean dragging, hovering, cursorRestored = false;
    private float dragDelta;

    public void render(float x, float y, int mouseX, int mouseY, boolean isNano) {
        float width = 200f;

        hovering = RenderUtil.hovering(mouseX, mouseY, x, y - 3f, width, 5f);

        float sliderX = x + 15f;
        dragDelta = dragging? mouseX - sliderX : 0f;
        if (dragDelta < 0) dragDelta = 0;
        if (dragDelta > 170f) dragDelta = 170f;
        double currentTime = Client.instance.musicManager.screen.player.getCurrentTime();
        double totalTime = Client.instance.musicManager.screen.player.getMusic().getDuration();
        float currentWidth = dragging? dragDelta : (float) (170 * (currentTime / totalTime));
        currentWidth = Math.min(currentWidth, 170f);

        if (hovering) {
            cursorRestored = false;
            GLFW.glfwSetCursor(Display.getWindow(), GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR));
        } else if (!cursorRestored) {
            GLFW.glfwSetCursor(Display.getWindow(), MemoryUtil.NULL);
            cursorRestored = true;
        }

        if (isNano) {
            String formattedTime = formatTime(currentTime);
            NanoFontLoader.rubik.drawString(formattedTime, x - 5f, y - 6.5f / 2f - 0.2f, 13f, Color.WHITE);
        } else {
            RoundedUtil.drawRound(sliderX, y, 170, 1f, 1f, ThemeColor.barColor);
        }

        if (!isNano) {
            RoundedUtil.drawRound(x + 15f, y, currentWidth, 1f, 1f, (hovering || dragging)? ThemeColor.redColor : ThemeColor.barPlayedColor);
            if (hovering || dragging) {
                RoundedUtil.drawRound(sliderX + currentWidth - 2.5f, y - 2f, 5f, 5f, 2f, Color.WHITE);
            }
        } else {
            String restOfTime = formatTime(totalTime - currentTime);
            NanoFontLoader.rubik.drawString(restOfTime, x + 190f, y - 6.5f / 2f - 0.2f, 13f, ThemeColor.greyColor);
        }
    }

    public void mouseClicked(int mouseButton) {
        if (hovering && mouseButton == 0 && !dragging) {
            dragging = true;
        }
    }

    public void mouseReleased() {
        if (dragging) {
            dragging = false;
            long duration = Client.instance.musicManager.screen.player.getMusic().getDuration();
            double newTime = duration * (dragDelta / 170f);
            if (newTime < 0) newTime = 0;
            if (newTime > duration) newTime = duration;
            Client.instance.musicManager.screen.player.setCurrentTime(newTime);
        }
    }
}
