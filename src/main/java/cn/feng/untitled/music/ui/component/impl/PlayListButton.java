package cn.feng.untitled.music.ui.component.impl;

import cn.feng.untitled.Client;
import cn.feng.untitled.music.api.base.PlayList;
import cn.feng.untitled.music.thread.FetchMusicsThread;
import cn.feng.untitled.music.ui.ThemeColor;
import cn.feng.untitled.music.ui.component.Button;
import cn.feng.untitled.music.ui.gui.MusicPlayerGUI;
import cn.feng.untitled.music.ui.gui.impl.PlayListGUI;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.util.animation.advanced.Animation;
import cn.feng.untitled.util.animation.advanced.Direction;
import cn.feng.untitled.util.animation.advanced.impl.DecelerateAnimation;
import cn.feng.untitled.util.render.RenderUtil;
import net.minecraft.client.renderer.texture.DynamicTexture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * @author ChengFeng
 * @since 2024/8/13
 **/
public class PlayListButton extends Button {
    private final PlayList playList;
    private final PlayListGUI gui;

    private final Animation scaleAnim = new DecelerateAnimation(200, 0.1f, Direction.BACKWARDS);

    public PlayListButton(PlayList playList, MusicPlayerGUI parent) {
        this.playList = playList;
        gui = new PlayListGUI(playList, parent);
        height = 40f;
    }

    private void render(boolean isNano) {
        gui.setWidth(width);

        boolean draw = true;
        if (playList.getCoverTexture() == null && !isNano) {
            try {
                playList.setCoverTexture(new DynamicTexture(ImageIO.read(playList.getCoverImage())));
            } catch (IOException e) {
                draw = false;
            }
        }

        if (hovering && scaleAnim.getDirection() == Direction.BACKWARDS) {
            scaleAnim.changeDirection();
        } else if (!hovering && scaleAnim.getDirection() == Direction.FORWARDS) {
            scaleAnim.changeDirection();
        }

        if (isNano) {
            NanoFontLoader.misans.bold().drawGlowString(playList.getName(), posX + 70, posY, 20f, Color.WHITE);
            String description = NanoFontLoader.misans.trimStringToWidth(playList.getDescription(), 200f, 16f, false, true);
            NanoFontLoader.misans.drawString(description, posX + 70, posY + 15, 16f, ThemeColor.greyColor);
        } else if (draw) {
            RenderUtil.scaleStart(posX + 40, posY + 25, 1 + scaleAnim.getOutput().floatValue());
            RenderUtil.drawImage(playList.getCoverTexture(), posX + 20, posY, 40, 40);
            RenderUtil.scaleEnd();
        }
    }

    @Override
    public void onNano() {
        render(true);
    }

    @Override
    public void onRender2D() {
        render(false);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (hovering && button == 0) {
            if (playList.getMusicList().isEmpty()) {
                new FetchMusicsThread(playList, gui.buttons, true).start();
            }
            Client.instance.musicManager.screen.setCurrentGUI(gui);
        }
    }
}
