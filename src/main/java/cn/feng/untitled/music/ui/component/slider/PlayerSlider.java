package cn.feng.untitled.music.ui.component.slider;

import cn.feng.untitled.Client;
import cn.feng.untitled.music.ui.ThemeColor;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;

import java.awt.*;

import static cn.feng.untitled.util.data.TimeUtil.formatTime;

/**
 * @author ChengFeng
 * @since 2024/8/14
 **/
public class PlayerSlider {
    private boolean dragging, hovering;

    private float dragDelta;

    public void draw(float x, float y, int mouseX, int mouseY) {
        float width = 200f;

        hovering = RenderUtil.hovering(mouseX, mouseY, x, y, width, 5f);

        float sliderX = x + 15f;
        double currentTime = Client.instance.musicManager.screen.player.getCurrentTime();
        double totalTime = Client.instance.musicManager.screen.player.getMusic().getDuration();

        String formattedTime = formatTime(currentTime);
        NanoFontLoader.rubik.drawString(formattedTime, x - 5f, y - 6.5f / 2f - 0.2f, 13f, Color.WHITE);
        RoundedUtil.drawRound(sliderX, y, 170, 1f, 1f, ThemeColor.barColor);

        float currentWidth = dragging? mouseX - sliderX : (float) (170 * (currentTime / totalTime));
        dragDelta = dragging? mouseX - sliderX : 0f;
        if (dragDelta < 0) dragDelta = 0;
        if (dragDelta > 170f) dragDelta = 170f;
        RoundedUtil.drawRound(x + 15f, y, currentWidth, 1f, 1f, ThemeColor.barPlayedColor);

        String restOfTime = formatTime(totalTime - currentTime);
        NanoFontLoader.rubik.drawString(restOfTime, x + 190f, y - 6.5f / 2f - 0.2f, 13f, ThemeColor.greyColor);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
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
