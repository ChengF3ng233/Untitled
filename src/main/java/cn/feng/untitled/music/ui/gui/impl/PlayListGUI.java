package cn.feng.untitled.music.ui.gui.impl;

import cn.feng.untitled.music.api.base.Music;
import cn.feng.untitled.music.api.base.PlayList;
import cn.feng.untitled.music.thread.FetchMusicsThread;
import cn.feng.untitled.music.ui.MusicPlayerScreen;
import cn.feng.untitled.music.ui.ThemeColor;
import cn.feng.untitled.music.ui.component.button.impl.MusicButton;
import cn.feng.untitled.music.ui.gui.MusicPlayerGUI;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoUtil;
import cn.feng.untitled.util.render.RenderUtil;
import org.lwjgl.input.Mouse;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;
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
    private PlayList playList;
    private FetchMusicsThread fetchThread;

    // 针对已有歌单的情况
    public PlayListGUI(PlayList playList, MusicPlayerGUI parent) {
        super(parent);
        setPlayList(playList);
    }

    // 我喜欢的音乐 后面再获取列表
    public PlayListGUI(MusicPlayerGUI parent) {
        super(parent);
    }

    public void setPlayList(PlayList playList) {
        this.playList = playList;

        if (!this.playList.getMusicList().isEmpty()) {
            buttons.clear();
            for (Music music : playList.getMusicList()) {
                buttons.add(new MusicButton(music, playList));
            }
        }
    }

    @Override
    public boolean render(float x, float y, int mouseX, int mouseY, float cx, float cy, float scale) {
        // 如果playList是null，那就是还没有设置歌单
        if (playList == null) {
            NanoFontLoader.misans.drawGlowString("加载中", x + width / 2f, y + 50f, 30f, NanoVG.NVG_ALIGN_CENTER | NanoVG.NVG_ALIGN_TOP, Color.WHITE);
            return false;
        }

        // 上传歌单封面纹理
        if (playList.getCoverTexture() == 0) {
            playList.setCoverTexture(NanoUtil.genImageId(playList.getCoverImage()));
        }
        NanoUtil.drawImageRect(playList.getCoverTexture(), x + 10f, y + 10f, 50f, 50f);

        NanoFontLoader.misans.drawGlowString(playList.getName(), x + 65f, y + 10f, 25f, Color.WHITE);
        NanoFontLoader.misans.drawString(playList.getDescription(), x + 65f, y + 30f, 13f, ThemeColor.greyColor);

        // 歌曲信息
        NanoFontLoader.greyCliff.bold().drawString("#", x + 7f, y + 75f, 15f, ThemeColor.greyColor);
        NanoFontLoader.misans.drawString("歌曲", x + 15f, y + 75f, 15f, ThemeColor.greyColor);
        NanoFontLoader.misans.drawString("专辑", x + 166f, y + 75f, 15f, ThemeColor.greyColor);
        NanoFontLoader.misans.drawString("时长", x + 286f, y + 75f, 15f, ThemeColor.greyColor);

        if (buttons.isEmpty()) {
            // 如果buttons是空的，表示正在获取歌单全部歌曲
            NanoFontLoader.misans.drawGlowString("加载中", x + width / 2f, y + 110f, 30f, NanoVG.NVG_ALIGN_CENTER | NanoVG.NVG_ALIGN_TOP, Color.WHITE);
            return false;
        }

        // 歌曲列表的坐标（为了适应glScissor，进行scale计算）
        float buttonY = y + 90f;
        float leftX = cx + (x - cx) * scale;
        float rightX = cx + ((x + width) - cx) * scale;
        float topY = cy + (buttonY - cy) * scale;
        float bottomY = cy + ((buttonY + height) - cy) * scale;

        float realButtonY = buttonY + scrollAnim.getOutput().floatValue();

        NanoUtil.scissorStart(leftX, topY, rightX - leftX, Math.max(bottomY - topY - 93f, 0f));

        // 防止java.util.ConcurrentModificationException
        final List<MusicButton> buttonList = new ArrayList<>(buttons);

        for (MusicButton button : buttonList) {
            if (realButtonY < bottomY) {
                button.width = width;
                button.updateState(x, realButtonY, mouseX, mouseY);
                if (!RenderUtil.hovering(mouseX, mouseY, leftX, topY, rightX - leftX, bottomY - topY - 93f)) {
                    button.hovering = false;
                }
                button.render();
            }
            realButtonY += button.height;
        }


        if (isBottom && fetchThread == null && !playList.isCompletelyDownloaded()) {
            fetchThread = new FetchMusicsThread(playList, buttons, false);
            fetchThread.start();
        }

        if (fetchThread != null && !fetchThread.isAlive()) {
            fetchThread = null;
        }

        NanoFontLoader.misans.drawString((playList.isCompletelyDownloaded() || playList.getId() == -1) ? "已经到底了" : "正在加载更多", x + width / 2f, realButtonY + 10f, 13f, NanoVG.NVG_ALIGN_CENTER, ThemeColor.greyColor);

        NanoUtil.scissorEnd();

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
        int wheel = Mouse.getDWheel();
        scrollAnim.setStartPoint(scrollAnim.getOutput());
        if (wheel > 0) {
            scrollAnim.setEndPoint(scrollAnim.getEndPoint() + 20f);
        } else if (wheel < 0) {
            scrollAnim.setEndPoint(scrollAnim.getEndPoint() - 20f);
        }
        if (scrollAnim.getEndPoint() > 0) scrollAnim.setEndPoint(0f);
        float maxScroll = height + 20f - (MusicPlayerScreen.height - MusicPlayerScreen.topWidth - MusicPlayerScreen.bottomWidth - 90f);
        if (-scrollAnim.getEndPoint() > maxScroll) {
            scrollAnim.setEndPoint(-maxScroll);
            isBottom = true;
        } else isBottom = false;
        scrollAnim.getAnimation().reset();
    }
}
