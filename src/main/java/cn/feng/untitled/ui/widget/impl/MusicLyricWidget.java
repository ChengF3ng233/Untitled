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
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.value.impl.BoolValue;
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

        spaceValue.setChangeAction((oldValue, newValue) -> {
            if (!Client.instance.loaded) return;
            MusicPlayer player = Client.instance.musicManager.screen.player;
            if (player.getMusic() == null || player.getMusic().getLyrics().isEmpty()) return;
            scrollAnim.setEndPoint(scrollAnim.getEndPoint() - player.getPlayedLyricCount() * (newValue - oldValue));
        });

        fontSize.setChangeAction((oldValue, newValue) -> {
            if (!Client.instance.loaded) return;
            MusicPlayer player = Client.instance.musicManager.screen.player;
            if (player.getMusic() == null || player.getMusic().getLyrics().isEmpty()) return;
            float newHeight = NanoFontLoader.misans.getHeight(newValue.floatValue());
            float oldHeight = NanoFontLoader.misans.getHeight(oldValue.floatValue());
            float newTransHeight = NanoFontLoader.misans.getHeight(newValue.floatValue() * 0.8f);
            float oldTransHeight = NanoFontLoader.misans.getHeight(oldValue.floatValue() * 0.8f);
            scrollAnim.setEndPoint(scrollAnim.getEndPoint() - player.getPlayedLyricCount() * (newHeight - oldHeight) * 2f - player.getPlayedTranslateCount() * (newTransHeight - oldTransHeight) * 2f);
        });
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
        // 整个歌词组件的高度
        height = heightValue.getValue().floatValue();
        MusicPlayer player = Client.instance.musicManager.screen.player;
        if (player.getMusic() == null || player.getMusic().getLyrics().isEmpty() || player.getMediaPlayer() == null)
            return;

        // 组件的左上角
        float renderX = x * sr.getScaledWidth();
        float renderY = y * sr.getScaledHeight();

        // 第一行歌词的Y（中间）
        float lyricTopY = renderY + scrollAnim.getOutput().floatValue() + height / 2f;

        // 歌词的X（中间）
        float lyricX = renderX + width / 2f;

        // 现在正在画的歌词的Y
        float lyricY = lyricTopY;

        // 字体的实际高度
        float fontSize = this.fontSize.getValue().floatValue();
        float lyricHeight = NanoFontLoader.misans.getHeight(fontSize);
        float translateHeight = NanoFontLoader.misans.getHeight(fontSize * 0.8f);

        // 歌词和翻译之间的间隔
        final float yGap = 2f;

        // 歌词和翻译
        List<LyricLine> lyrics = player.getMusic().getLyrics();
        Map<LyricLine, LyricLine> translateMap = player.getMusic().getTranslateMap();

        // 为最长的歌词留出空间
        NanoUtil.scissorStart(renderX - 3f, renderY, width + 6f, height);

        for (LyricLine lyricLine : lyrics) {
            boolean hasTranslateLine = player.getMusic().isHasTranslate() && translateMap.containsKey(lyricLine);
            float addition = spaceValue.getValue().floatValue() + (hasTranslateLine ? lyricHeight * 2f + yGap : lyricHeight);

            float lyricWidth = NanoFontLoader.misans.getStringWidth(lyricLine.getLine(), fontSize);
            if (lyricWidth > width) width = lyricWidth;
            lyricLine.render(lyricX, lyricY, fontSize, addition, player.getCurrentTime(), fontShadow.getValue(), scrollAnim);

            if (translateMap.containsKey(lyricLine)) {
                LyricLine translateLine = translateMap.get(lyricLine);
                float translateWidth = NanoFontLoader.misans.getStringWidth(translateLine.getLine(), fontSize * 0.8f);
                if (translateWidth > width) width = translateWidth;
                translateLine.render(lyricX, lyricY + lyricHeight + yGap + translateHeight / 2f, fontSize * 0.8f, addition, player.getCurrentTime(), fontShadow.getValue(), scrollAnim);
            }

            lyricY += addition;
        }

        NanoUtil.scissorEnd();
    }

    @Override
    public void onShader(ShaderEvent event) {
        MusicPlayer player = Client.instance.musicManager.screen.player;
        if (player.getMusic() == null || player.getMusic().getLyrics().isEmpty() || player.getMediaPlayer() == null)
            return;

        // 组件的左上角
        float renderX = x * sr.getScaledWidth();
        float renderY = y * sr.getScaledHeight();

        // 第一行歌词的Y（中间）
        float lyricTopY = renderY + scrollAnim.getOutput().floatValue() + height / 2f;

        // 歌词的X（中间）
        float lyricX = renderX + width / 2f;

        // 现在正在画的歌词的Y
        float lyricY = lyricTopY;

        // 字体的实际高度
        float fontSize = this.fontSize.getValue().floatValue();
        float lyricHeight = NanoFontLoader.misans.getHeight(fontSize);
        float translateHeight = NanoFontLoader.misans.getHeight(fontSize * 0.8f);

        // 歌词和翻译之间的间隔
        final float yGap = 2f;

        // 歌词和翻译
        List<LyricLine> lyrics = player.getMusic().getLyrics();
        Map<LyricLine, LyricLine> translateMap = player.getMusic().getTranslateMap();

        // 为最长的歌词留出空间
        RenderUtil.scissorStart(renderX - 3f, renderY, width + 6f, height);

        for (LyricLine lyricLine : lyrics) {
            boolean hasTranslateLine = player.getMusic().isHasTranslate() && translateMap.containsKey(lyricLine);
            float addition = spaceValue.getValue().floatValue() + (hasTranslateLine ? lyricHeight * 2f + yGap : lyricHeight);

            float lyricWidth = NanoFontLoader.misans.getStringWidth(lyricLine.getLine(), fontSize);

            if (bloomValue.getValue() && event.bloom) {
                Gui.drawNewRect(lyricX - lyricWidth / 2f - lyricHeight / 4f, lyricY - lyricHeight / 2f - lyricHeight / 4f, lyricWidth + lyricHeight / 2f, lyricHeight * 1.5f, Color.BLACK.getRGB());
            }

            if (blurValue.getValue() && !event.bloom) {
                Gui.drawNewRect(lyricX - lyricWidth / 2f - lyricHeight / 4f, lyricY - lyricHeight / 2f - lyricHeight / 4f, lyricWidth + lyricHeight / 2f, lyricHeight * 1.5f, Color.BLACK.getRGB());
            }

            if (translateMap.containsKey(lyricLine)) {
                LyricLine translateLine = translateMap.get(lyricLine);
                float translateWidth = NanoFontLoader.misans.getStringWidth(translateLine.getLine(), fontSize * 0.8f);
                if (bloomValue.getValue() && event.bloom) {
                    Gui.drawNewRect(lyricX - translateWidth / 2f - translateHeight / 4f, lyricY + lyricHeight / 2f, translateWidth + translateHeight / 2f, yGap + translateHeight * 2f, Color.BLACK.getRGB());
                }
                if (blurValue.getValue() && !event.bloom) {
                    Gui.drawNewRect(lyricX - translateWidth / 2f - translateHeight / 4f, lyricY + lyricHeight / 2f, translateWidth + translateHeight / 2f,yGap + translateHeight * 2f, Color.BLACK.getRGB());
                }
            }

            lyricY += addition;
        }

        RenderUtil.scissorEnd();
    }

    public void reset() {
        scrollAnim.setEndPoint(0f);
        width = 50f;
    }
}
