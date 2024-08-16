package cn.feng.untitled.ui.widget.impl;

import cn.feng.untitled.Client;
import cn.feng.untitled.event.impl.ShaderEvent;
import cn.feng.untitled.music.api.base.LyricLine;
import cn.feng.untitled.music.api.player.MusicPlayer;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoUtil;
import cn.feng.untitled.ui.widget.Widget;
import cn.feng.untitled.util.animation.advanced.composed.CustomAnimation;
import cn.feng.untitled.util.animation.advanced.impl.DecelerateAnimation;
import cn.feng.untitled.util.animation.advanced.impl.SmoothStepAnimation;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.value.impl.BoolValue;
import cn.feng.untitled.value.impl.FontValue;
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

    private final BoolValue fontShadow = new BoolValue("FontShadow", false);
    private final NumberValue fontSize = new NumberValue("FontSize", 17, 30, 15, 0.1);
    private final BoolValue blurValue = new BoolValue("Blur", true);
    private final BoolValue bloomValue = new BoolValue("Bloom", true);
    private final NumberValue heightValue = new NumberValue("Height", 100, 150, 30, 0.1);
    private final NumberValue spaceValue = new NumberValue("Space", 20, 30, 15, 0.1);
    private final CustomAnimation scrollAnim = new CustomAnimation(DecelerateAnimation.class, 300, 0f, 0f);

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
            float addY = player.getMusic().isHasTranslate() && translateMap.containsKey(lyricLine) ? spaceValue.value.floatValue() + fontSize.value.floatValue() / 2f : spaceValue.value.floatValue();
            float lyricWidth = NanoFontLoader.misans.getStringWidth(lyricLine.getLine(), fontSize.value.floatValue());
            if (lyricWidth > width) width = lyricWidth;
            lyricLine.render(lyricX, lyricY, width, spaceValue.value.floatValue(), player.getCurrentTime(), scrollAnim, translateMap.getOrDefault(lyricLine, null), fontShadow.value, fontSize.value.floatValue());
            if (translateMap.containsKey(lyricLine)) {
                LyricLine translateLine = translateMap.get(lyricLine);
                float translateWidth = NanoFontLoader.misans.getStringWidth(translateLine.getLine(), fontSize.value.floatValue() * 0.7f);
                if (translateWidth > width) width = translateWidth;
                translateLine.render(lyricX + width / 2f - translateWidth / 2f, lyricY + 12f, translateWidth, spaceValue.value.floatValue(), player.getCurrentTime(), scrollAnim, null, fontShadow.value, fontSize.value.floatValue());
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
            float addY = player.getMusic().isHasTranslate() && translateMap.containsKey(lyricLine) ? spaceValue.value.floatValue() + fontSize.value.floatValue() / 2f : spaceValue.value.floatValue();
            float lyricWidth = NanoFontLoader.misans.getStringWidth(lyricLine.getLine(), fontSize.value.floatValue());
            if (bloomValue.value && event.bloom) {
                Gui.drawNewRect(lyricX + width / 2f - lyricWidth / 2f - fontSize.value.floatValue() / 7f, lyricY - 2f, lyricWidth + fontSize.value.floatValue() / 4f, fontSize.value.floatValue() / 2f + 4f, Color.BLACK.getRGB());
            }
            if (blurValue.value && !event.bloom) {
                Gui.drawNewRect(lyricX + width / 2f - lyricWidth / 2f - fontSize.value.floatValue() / 7f, lyricY - 2f, lyricWidth + fontSize.value.floatValue() / 4f, fontSize.value.floatValue() / 2f + 4f, Color.BLACK.getRGB());
            }
            if (translateMap.containsKey(lyricLine)) {
                LyricLine translateLine = translateMap.get(lyricLine);
                float translateWidth = NanoFontLoader.misans.getStringWidth(translateLine.getLine(), fontSize.value.floatValue() * 0.7f);
                if (bloomValue.value && event.bloom) {
                    Gui.drawNewRect(lyricX + width / 2f - translateWidth / 2f - fontSize.value.floatValue() * 0.7f / 7f, lyricY + 9f, translateWidth + fontSize.value.floatValue() * 0.7f / 4f, fontSize.value.floatValue() * 0.7f / 2f + 4f, Color.BLACK.getRGB());
                }
                if (blurValue.value && !event.bloom) {
                    Gui.drawNewRect(lyricX + width / 2f - translateWidth / 2f - fontSize.value.floatValue() * 0.7f / 7f, lyricY + 9f, translateWidth + fontSize.value.floatValue() * 0.7f / 4f, fontSize.value.floatValue() * 0.7f / 2f + 4f, Color.BLACK.getRGB());
                }
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
