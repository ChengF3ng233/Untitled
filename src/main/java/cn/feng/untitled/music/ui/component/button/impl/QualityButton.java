package cn.feng.untitled.music.ui.component.button.impl;

import cn.feng.untitled.Client;
import cn.feng.untitled.music.api.base.MusicQuality;
import cn.feng.untitled.music.api.player.MusicPlayer;
import cn.feng.untitled.music.ui.ThemeColor;
import cn.feng.untitled.music.ui.component.button.Button;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoFontRenderer;
import cn.feng.untitled.ui.font.nano.NanoUtil;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/8/19
 **/
public class QualityButton extends Button {
    @Override
    public void render() {
        NanoFontRenderer fontRenderer = NanoFontLoader.misans;
        MusicPlayer player = Client.instance.musicManager.screen.player;
        height = 10f;
        width = fontRenderer.getStringWidth(player.getQuality().getDisplayName(), 13f) + 4f;
        NanoUtil.drawRoundedOutlineRect(posX, posY, width, height, 2f, 1f, ThemeColor.playerColor, hovering? ThemeColor.outlineColor.brighter() : ThemeColor.outlineColor);
        fontRenderer.drawString(player.getQuality().getDisplayName(), posX + width / 2f, posY + height / 2f, 13f, NanoVG.NVG_ALIGN_CENTER | NanoVG.NVG_ALIGN_MIDDLE, Color.WHITE);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        MusicPlayer player = Client.instance.musicManager.screen.player;
        if (player.getMusic() == null) return;
        if (hovering && button == 0) {
            int level = player.getQuality().getLevel() + 1;
            if (player.getMusic().getQuality() == null) {
                player.setQuality(MusicQuality.exhigh);
                return;
            }
            if (level > player.getMusic().getQuality().getLevel()) level = 0;
            player.setQuality(MusicQuality.getQuality(level));
        }
    }
}
