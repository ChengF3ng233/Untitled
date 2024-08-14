package cn.feng.untitled.music.ui.component.slider;

import cn.feng.untitled.Client;
import cn.feng.untitled.music.ui.ThemeColor;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/8/14
 **/
public class VolumeSlider {
    private boolean dragging, hovering;
    private float dragDelta;

    public void draw(float x, float y, int mouseX, int mouseY) {
        float width = 100f;

        hovering = RenderUtil.hovering(mouseX, mouseY, x, y - 3f, width, 7f);

        float sliderX = x + 15f;
        double currentVolume = Client.instance.musicManager.screen.player.getVolume();
        double totalVolume = 100d;

        NanoFontLoader.misans.drawString("音量", x, y - 6.5f / 2f - 0.2f, 13f, Color.WHITE);
        RoundedUtil.drawRound(sliderX, y, 70, 1f, 1f, ThemeColor.barColor);

        float currentWidth = dragging ? mouseX - sliderX : (float) (70 * (currentVolume / totalVolume));
        dragDelta = dragging ? mouseX - sliderX : 0f;
        if (dragDelta < 0) dragDelta = 0;
        if (dragDelta > 70f) dragDelta = 70f;
        if (dragging)
            Client.instance.musicManager.screen.player.setVolume((dragDelta / 70f) * 100d);
        RoundedUtil.drawRound(x + 15f, y, currentWidth, 1f, 1f, ThemeColor.barPlayedColor);
        NanoFontLoader.rubik.drawString(Math.round(currentVolume) + "%", x + 90f, y - 6.5f / 2f - 0.2f, 13f, Color.WHITE);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
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
