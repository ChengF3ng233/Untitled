package cn.feng.untitled.ui.widget.impl;

import cn.feng.untitled.Client;
import cn.feng.untitled.event.impl.ShaderEvent;
import cn.feng.untitled.music.api.base.LyricLine;
import cn.feng.untitled.music.api.player.MusicPlayer;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoUtil;
import cn.feng.untitled.ui.widget.Widget;
import cn.feng.untitled.util.animation.advanced.composed.CustomAnimation;
import cn.feng.untitled.util.animation.advanced.impl.SmoothStepAnimation;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.value.impl.NumberValue;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * @author ChengFeng
 * @since 2024/8/14
 **/
public class MusicLyricWidget extends Widget {
    public MusicLyricWidget() {
        super("MusicLyric", true);
        x = 0.7f;
        y = 0.7f;
    }

    private final NumberValue heightValue = new NumberValue("Height", 100, 150, 30, 0.1);
    private final NumberValue spaceValue = new NumberValue("Space", 20, 30, 15, 0.1);
    private final CustomAnimation scrollAnim = new CustomAnimation(SmoothStepAnimation.class, 150, 0f, 0f);

    @Override
    public void onNano() {
        height = heightValue.value.floatValue();
        MusicPlayer player = Client.instance.musicManager.screen.player;
        if (player.getMusic() == null || player.getMusic().getLyrics().isEmpty() || player.getMediaPlayer() == null)
            return;

        float lyricTopY = y * sr.getScaledHeight() + scrollAnim.getOutput().floatValue() + height / 2f;
        float lyricX = x * sr.getScaledWidth();
        float lyricY = lyricTopY;

        List<LyricLine> lyrics = player.getMusic().getLyrics();
        Map<LyricLine, LyricLine> translateMap = player.getMusic().getTranslateMap();

        NanoUtil.scissorStart(lyricX - 5f, y * sr.getScaledHeight(), width + 10f, height);

        for (LyricLine lyricLine : lyrics) {
            float addY = player.getMusic().isHasTranslate() && translateMap.containsKey(lyricLine) ? spaceValue.value.floatValue() + 10f : spaceValue.value.floatValue();
            float lyricWidth = NanoFontLoader.misans.getStringWidth(lyricLine.getLine(), 17f);
            if (lyricWidth > width) width = lyricWidth;
            lyricLine.render(lyricX, lyricY, width, spaceValue.value.floatValue(), player.getCurrentTime(), scrollAnim, translateMap.getOrDefault(lyricLine, null));
            if (translateMap.containsKey(lyricLine)) {
                LyricLine translateLine = translateMap.get(lyricLine);
                float translateWidth = NanoFontLoader.misans.getStringWidth(translateLine.getLine(), 15f);
                if (translateWidth > width) width = translateWidth;
                translateLine.render(lyricX + width / 2f - translateWidth / 2f, lyricY + 12f, translateWidth, spaceValue.value.floatValue(), player.getCurrentTime(), scrollAnim, null);
            }
            lyricY += addY;
        }

        NanoUtil.scissorEnd();
    }

    @Override
    public void onShader(ShaderEvent event) {
        MusicPlayer player = Client.instance.musicManager.screen.player;
        if (player.getMusic() == null || player.getMusic().getLyrics().isEmpty() || player.getMediaPlayer() == null)
            return;

        float lyricTopY = y * sr.getScaledHeight() + scrollAnim.getOutput().floatValue() + height / 2f;
        float lyricX = x * sr.getScaledWidth();
        float lyricY = lyricTopY;

        List<LyricLine> lyrics = player.getMusic().getLyrics();
        Map<LyricLine, LyricLine> translateMap = player.getMusic().getTranslateMap();

        RenderUtil.scissorStart(lyricX - 5f, y * sr.getScaledHeight(), width + 10f, height);

        for (LyricLine lyricLine : lyrics) {
            float addY = player.getMusic().isHasTranslate() && translateMap.containsKey(lyricLine) ? spaceValue.value.floatValue() + 10f : spaceValue.value.floatValue();
            float lyricWidth = NanoFontLoader.misans.getStringWidth(lyricLine.getLine(), 17f);
            Gui.drawNewRect(lyricX + width / 2f - lyricWidth / 2f - 2f, lyricY - 1f, lyricWidth + 4f, 11f, Color.BLACK.getRGB());
            if (translateMap.containsKey(lyricLine)) {
                LyricLine translateLine = translateMap.get(lyricLine);
                float translateWidth = NanoFontLoader.misans.getStringWidth(translateLine.getLine(), 15f);
                Gui.drawNewRect(lyricX + width / 2f - translateWidth / 2f - 2f, lyricY + 9f, translateWidth + 4f, 12f, Color.BLACK.getRGB());
            }
            lyricY += addY;
        }

        RenderUtil.scissorEnd();
    }

    public void reset() {
        scrollAnim.setEndPoint(0f);
        width = 50f;
    }
}
