package cn.feng.untitled.music.ui.component.slider;

import cn.feng.untitled.Client;
import cn.feng.untitled.music.ui.ThemeColor;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.util.data.resource.ResourceType;
import cn.feng.untitled.util.data.resource.ResourceUtil;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.Display;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/8/14
 **/
public class VolumeSlider {
    private boolean dragging, hovering, cursorRestored = false;
    private float dragDelta;

    public void render(float x, float y, int mouseX, int mouseY, boolean isNano) {
        float width = 100f;

        hovering = RenderUtil.hovering(mouseX, mouseY, x, y - 4f, width, 7f);

        float sliderX = x + 15f;
        double currentVolume = Client.instance.musicManager.screen.player.getVolume();
        double totalVolume = 100d;

        String fileName = (currentVolume == 0? "volume_0" : (currentVolume <= 50? "volume_1" : "volume_2")) + ".png";

        if (hovering) {
            cursorRestored = false;
            GLFW.glfwSetCursor(Display.getWindow(), GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR));
        } else if (!cursorRestored) {
            GLFW.glfwSetCursor(Display.getWindow(), MemoryUtil.NULL);
            cursorRestored = true;
        }

        if (!isNano) {
            RenderUtil.drawImage(ResourceUtil.getResource(fileName, ResourceType.ICON), x, y - 6f, 12f, 12f);
            RoundedUtil.drawRound(sliderX, y, 70, 1f, 1f, ThemeColor.barColor);
        }

        float currentWidth = dragging ? dragDelta : (float) (70 * (currentVolume / totalVolume));
        dragDelta = dragging ? mouseX - sliderX : 0f;
        if (dragDelta < 0) dragDelta = 0;
        if (dragDelta > 70f) dragDelta = 70f;
        if (dragging)
            Client.instance.musicManager.screen.player.setVolume((dragDelta / 70f) * 100d);
        if (!isNano) {
            RoundedUtil.drawRound(x + 15f, y, currentWidth, 1f, 1f, hovering? ThemeColor.redColor : ThemeColor.barPlayedColor);
            if (hovering) {
                RoundedUtil.drawRound(sliderX + currentWidth - 2.5f, y - 2f, 5f, 5f, 2f, Color.WHITE);
            }
        } else {
            NanoFontLoader.rubik.drawString(Math.round(currentVolume) + "%", x + 90f, y - 6.5f / 2f - 0.2f, 13f, Color.WHITE);
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
        }
    }
}
