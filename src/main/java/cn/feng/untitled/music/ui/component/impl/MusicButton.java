package cn.feng.untitled.music.ui.component.impl;

import cn.feng.untitled.music.api.base.Music;
import cn.feng.untitled.music.api.base.PlayList;
import cn.feng.untitled.music.thread.ChangeMusicThread;
import cn.feng.untitled.music.ui.ThemeColor;
import cn.feng.untitled.music.ui.component.Button;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoFontRenderer;
import cn.feng.untitled.util.misc.TimerUtil;
import cn.feng.untitled.util.render.ColorUtil;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.DynamicTexture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * @author ChengFeng
 * @since 2024/8/13
 **/
public class MusicButton extends Button {
    private final Music music;
    private final int index;
    private final PlayList playList;

    public DynamicTexture texture;

    public MusicButton(Music music, int index, PlayList playList) {
        this.music = music;
        this.index = index;
        this.playList = playList;
    }

    @Override
    public void draw() {
        height = 30f;
        if (hovering) {
            RoundedUtil.drawRound(posX, posY, width - 1f, height, 0f, ColorUtil.applyOpacity(ThemeColor.greyColor, 0.1f));
        }

        float textX = posX + 7f;
        float textY = posY + 7f;
        NanoFontLoader.greyCliffBold.drawString(index + "", textX, textY + 4f, 13f, ThemeColor.greyColor);
        if (texture == null) {
            try {
                texture = new DynamicTexture(ImageIO.read(music.getCoverImage()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        RenderUtil.drawImage(texture, textX + 10f, textY, 16f, 16f);

        NanoFontRenderer font = NanoFontLoader.misans;

        String s1 = font.trimStringToWidth(music.getName(), 120f, 15f, false, true);
        font.drawString(s1, textX + 30f, textY, 15f, music.isFree()? Color.WHITE : ThemeColor.redColor);
        String s2 = font.trimStringToWidth(music.getArtist(), 100f, 15f, false, true);
        font.drawString(s2, textX + 30f, textY + 9f, 12f, ThemeColor.greyColor);

        Gui.drawNewRect(textX + 150f, textY, 0.5f, 20f, ColorUtil.applyOpacity(ThemeColor.greyColor, 0.2f).getRGB());

        String s = font.trimStringToWidth(music.getAlbum(), 80f, 15f, false, true);
        font.drawString(s, textX + 160f, textY + 5f, 15f, ThemeColor.greyColor);

        long totalSeconds = music.getDuration() / 1000;

        // 计算分钟和剩余秒数
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        String timeFormatted = String.format("%d:%02d", minutes, seconds);
        Gui.drawNewRect(textX + 270f, textY, 0.5f, 20f, ColorUtil.applyOpacity(ThemeColor.greyColor, 0.2f).getRGB());
        font.drawString(timeFormatted, textX + 280f, textY + 5f, 15f, ThemeColor.greyColor);
    }

    private TimerUtil clickTimer = new TimerUtil();

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (hovering && button == 0) {
            if (!clickTimer.hasTimeElapsed(1500)) {
                new ChangeMusicThread(music, playList).start();
            }
            clickTimer.reset();
        }
    }
}
