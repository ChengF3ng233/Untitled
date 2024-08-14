package cn.feng.untitled.music.ui.component.impl;

import cn.feng.untitled.Client;
import cn.feng.untitled.config.impl.MusicConfig;
import cn.feng.untitled.music.api.MusicAPI;
import cn.feng.untitled.music.api.User;
import cn.feng.untitled.music.ui.gui.impl.LoginGUI;
import cn.feng.untitled.music.ui.ThemeColor;
import cn.feng.untitled.music.ui.component.Button;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.util.data.HttpUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;

import java.awt.*;

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
    public void draw() {
        User user = MusicAPI.user;
        String text = user.isLoggedIn() ? user.getNickname() : "未登录";
        width = 20f + NanoFontLoader.misans.getStringWidth(text, 14);
        RoundedUtil.drawRound(posX + 20, posY + height / 2f, width - 20, height, 2f, ThemeColor.playerColor);

        if (user.isLoggedIn()) {
            if (user.getAvatarTexture() == null) {
                user.setAvatarTexture(new DynamicTexture(HttpUtil.downloadImage(user.getAvatarUrl(), 200, 200)));
            }
            GlStateManager.bindTexture(user.getAvatarTexture().getGlTextureId());
            RoundedUtil.drawRoundTextured(posX, posY + 4f, 12, 12, 5f, 1f);
        }
        NanoFontLoader.misans.drawString(text, posX + 20, posY + 0.5f + height / 2f, 14f, hovering ? Color.WHITE : ThemeColor.greyColor);

        if (loginGUI != null) {
            if (loginGUI.draw(0, 0, 0, 0, 0, 0, 0) /*参数不重要*/) {
                loginGUI = null;
                Client.instance.configManager.saveConfig(MusicConfig.class);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 0 && hovering && !MusicAPI.user.isLoggedIn()) loginGUI = new LoginGUI();
    }
}
