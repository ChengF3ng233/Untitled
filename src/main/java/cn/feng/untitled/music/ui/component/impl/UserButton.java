package cn.feng.untitled.music.ui.component.impl;

import cn.feng.untitled.Client;
import cn.feng.untitled.config.impl.MusicConfig;
import cn.feng.untitled.music.api.MusicAPI;
import cn.feng.untitled.music.api.user.User;
import cn.feng.untitled.music.ui.ThemeColor;
import cn.feng.untitled.music.ui.component.Button;
import cn.feng.untitled.music.ui.gui.impl.LoginGUI;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoUtil;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * @author ChengFeng
 * @since 2024/8/13
 **/
public class UserButton extends Button {
    private LoginGUI loginGUI;

    public UserButton() {
        height = 10f;
    }

    @Override
    public void render() {
        User user = MusicAPI.user;
        String text = user.isLoggedIn() ? user.getNickname() : "未登录";

        NanoUtil.drawRoundedRect(posX + 20, posY + height / 2f, width - 20, height, 2f, ThemeColor.playerColor);
        NanoFontLoader.misans.drawString(text, posX + 20, posY + 0.5f + height / 2f, 14f, hovering ? Color.WHITE : ThemeColor.greyColor);
        width = 20f + NanoFontLoader.misans.getStringWidth(text, 14);
        if (user.isLoggedIn()) {
            if (user.getAvatarTexture() == 0) {
                try {
                    user.setAvatarTexture(NanoUtil.genImageId(new URL(user.getAvatarUrl()).openStream()));
                } catch (IOException e) {

                }
            }
            NanoUtil.drawImageCircle(user.getAvatarTexture(), posX + 20 - 4 - 6, posY + height / 2f + 5f, 6);
        }

        if (loginGUI != null && loginGUI.render(0, 0, 0, 0, 0, 0, 0) /*参数不重要*/) {
            loginGUI = null;
            Client.instance.configManager.saveConfig(MusicConfig.class);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 0 && hovering && !MusicAPI.user.isLoggedIn()) loginGUI = new LoginGUI();
    }
}
