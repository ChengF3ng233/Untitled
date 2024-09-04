package cn.feng.untitled.music.ui.gui.impl;

import cn.feng.untitled.music.api.MusicAPI;
import cn.feng.untitled.music.api.base.PlayList;
import cn.feng.untitled.music.ui.component.button.impl.PlayListButton;
import cn.feng.untitled.music.ui.gui.MusicPlayerGUI;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.util.render.nano.NanoUtil;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 虽然很奇怪，但是他就是叫“歌单”列表
 *
 * @author ChengFeng
 * @since 2024/8/13
 **/
@Getter
@Setter
public class PlayListListGUI extends MusicPlayerGUI {
    private List<PlayListButton> buttons = new ArrayList<>();

    public PlayListListGUI() {
        super(null);
    }

    @Override
    public boolean render(float x, float y, int mouseX, int mouseY, float cx, float cy, float scale) {
        ArrayList<PlayListButton> buttons = new ArrayList<>(this.buttons);
        if (buttons.isEmpty()) {
            NanoFontLoader.misans.drawGlowString(MusicAPI.user.isLoggedIn() ? "获取中" : "请先登录", x + width / 2f, y + 50f, 30f, NanoVG.NVG_ALIGN_CENTER, Color.WHITE);
            return false;
        }

        posX = x;
        posY = y + scrollAnim.getOutput().floatValue();

        float leftX = cx + (x - cx) * scale;
        float rightX = cx + ((x + width) - cx) * scale;
        float topY = cy + (y - cy) * scale;
        float bottomY = cy + ((y + height) - cy) * scale;

        float buttonY = posY;

        NanoUtil.scissorStart(leftX, topY, rightX - leftX, Math.max(bottomY - topY - 3f, 0f));
        for (PlayListButton button : buttons) {
            // 懒加载
            if (buttonY < bottomY) {
                button.width = this.width;
                button.updateState(posX, buttonY, mouseX, mouseY);
                button.render();
            }
            buttonY += button.height + 10f;
        }
        NanoUtil.scissorEnd();

        height = buttonY - posY;
        return true;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        ArrayList<PlayListButton> buttons = new ArrayList<>(this.buttons);
        for (PlayListButton button : buttons) {
            button.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    public void addPlayList(PlayList playList) {
        for (PlayListButton button : buttons) {
            if (button.playList.getId() == playList.getId()) return;
        }
        buttons.add(new PlayListButton(playList, this));
    }
}
