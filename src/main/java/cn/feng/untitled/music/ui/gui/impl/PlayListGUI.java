package cn.feng.untitled.music.ui.gui.impl;

import cn.feng.untitled.music.api.Music;
import cn.feng.untitled.music.api.PlayList;
import cn.feng.untitled.music.thread.FetchPlayListThread;
import cn.feng.untitled.music.ui.MusicPlayerScreen;
import cn.feng.untitled.music.ui.ThemeColor;
import cn.feng.untitled.music.ui.component.impl.MusicButton;
import cn.feng.untitled.music.ui.gui.MusicPlayerGUI;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoUtil;
import cn.feng.untitled.util.render.RenderUtil;
import lombok.Setter;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.input.Mouse;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 这是歌单界面
 *
 * @author ChengFeng
 * @since 2024/8/13
 **/
public class PlayListGUI extends MusicPlayerGUI {
    public final List<MusicButton> buttons = new ArrayList<>();
    @Setter
    private PlayList playList;
    private FetchPlayListThread currentLoadThread;
    private DynamicTexture texture;

    private boolean noMore = false;

    public PlayListGUI(PlayList playList, MusicPlayerGUI parent) {
        super(parent);
        this.playList = playList;
        int index = 1;
        for (Music music : playList.getMusicList()) {
            buttons.add(new MusicButton(music, index, playList));
            index++;
        }
    }

    public PlayListGUI(MusicPlayerGUI parent) {
        super(parent);
    }

    @Override
    public boolean draw(float x, float y, int mouseX, int mouseY, float cx, float cy, float scale) {
        if (texture == null) {
            try {
                texture = new DynamicTexture(ImageIO.read(playList.getCoverImage()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        RenderUtil.drawImage(texture, x + 10f, y + 10f, 50f, 50f);
        NanoFontLoader.misans.drawGlowString(playList.getName(), x + 65f, y + 10f, 25f, Color.WHITE);
        NanoFontLoader.misans.drawString(playList.getDescription(), x + 65f, y + 30f, 13f, ThemeColor.greyColor);

        NanoFontLoader.greyCliffBold.drawString("#", x + 6f, y + 75f, 15f, ThemeColor.greyColor);
        NanoFontLoader.misans.drawString("歌曲", x + 18f, y + 75f, 15f, ThemeColor.greyColor);
        NanoFontLoader.misans.drawString("专辑", x + 157f, y + 75f, 15f, ThemeColor.greyColor);
        NanoFontLoader.misans.drawString("时长", x + 307f, y + 75f, 15f, ThemeColor.greyColor);

        float buttonY = y + 90f;
        float leftX = cx + (x - cx) * scale;
        float rightX = cx + ((x + width) - cx) * scale;
        float topY = cy + (buttonY - cy) * scale;
        float bottomY = cy + ((buttonY + height) - cy) * scale;

        if (buttons.isEmpty()) {
            NanoFontLoader.misans.drawGlowString("加载中", x + width / 2f, buttonY + 20f, 30f, NanoVG.NVG_ALIGN_CENTER, Color.WHITE);
            return false;
        }

        float realButtonY = buttonY + scrollAnim.getOutput().floatValue();

        RenderUtil.scissorStart(leftX, topY, rightX - leftX, Math.max(bottomY - topY - 93f, 0f));
        NanoUtil.scissorStart(leftX, topY, rightX - leftX, Math.max(bottomY - topY - 93f, 0f));
        for (MusicButton button : buttons) {
            if (realButtonY < bottomY) {
                button.width = width;
                button.updateState(x, realButtonY, mouseX, mouseY);
                if (!RenderUtil.hovering(mouseX, mouseY, leftX, topY, rightX - leftX, bottomY - topY - 93f)) {
                    button.hovering = false;
                }
                button.draw();
            }
            realButtonY += button.height;
        }

        if (currentLoadThread != null) {
            if (currentLoadThread.isAlive()) {
                height += 20f;
                NanoFontLoader.misans.drawString("正在加载更多", x + width / 2f, realButtonY + 2f, 13f, NanoVG.NVG_ALIGN_CENTER, ThemeColor.greyColor);
            } else {
                buttons.addAll(currentLoadThread.getButtonsTemp());
                noMore = currentLoadThread.isNoMore();
                currentLoadThread = null;
            }
        }

        if (isBottom && currentLoadThread == null) {
            if (noMore) {
                height += 20f;
                NanoFontLoader.misans.drawString("已经到底了", x + width / 2f, realButtonY + 2f, 13f, NanoVG.NVG_ALIGN_CENTER, ThemeColor.greyColor);
            } else {
                currentLoadThread = new FetchPlayListThread(playList);
                currentLoadThread.start();
            }
        }
        NanoUtil.scissorEnd();
        RenderUtil.scissorEnd();

        height = realButtonY - (buttonY + scrollAnim.getOutput().floatValue());

        return true;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (MusicButton button : buttons) {
            button.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void handleScroll() {
        // Scroll
        int wheel = Mouse.getDWheel();
        if (wheel != 0) {
            scrollAnim.setStartPoint(scrollAnim.getOutput());
            if (wheel > 0) {
                scrollAnim.setEndPoint(scrollAnim.getEndPoint() + 20f);
            } else {
                scrollAnim.setEndPoint(scrollAnim.getEndPoint() - 20f);
            }
            if (scrollAnim.getEndPoint() > 0) scrollAnim.setEndPoint(0f);
            float maxScroll = height + (noMore || (currentLoadThread != null && currentLoadThread.isAlive())? 20f : 0f) - (MusicPlayerScreen.height - MusicPlayerScreen.topWidth - MusicPlayerScreen.bottomWidth - 90f);
            if (-scrollAnim.getEndPoint() > maxScroll) {
                scrollAnim.setEndPoint(-maxScroll);
                isBottom = true;
            } else isBottom = false;
            scrollAnim.getAnimation().reset();
        }
    }

    @Override
    public void freeMemory() {
        for (MusicButton button : buttons) {
            if (button.texture != null) {
                GL11.glDeleteTextures(button.texture.getGlTextureId());
                button.texture = null;
            }
        }
    }
}
