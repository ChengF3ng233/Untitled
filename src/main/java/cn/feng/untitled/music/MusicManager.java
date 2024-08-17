package cn.feng.untitled.music;

import cn.feng.untitled.Client;
import cn.feng.untitled.config.ConfigManager;
import cn.feng.untitled.music.api.MusicAPI;
import cn.feng.untitled.music.ui.MusicPlayerScreen;
import cn.feng.untitled.music.thread.GetPlayListsThread;
import cn.feng.untitled.util.data.HttpUtil;
import net.minecraft.client.renderer.texture.DynamicTexture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author ChengFeng
 * @since 2024/8/12
 **/
public class MusicManager {
    public MusicPlayerScreen screen;

    public void initGUI() {
        screen = new MusicPlayerScreen();
        Client.instance.eventBus.register(screen);

        // 如果已经登录，获取歌单列表
        if (MusicAPI.user.isLoggedIn()) {
            new GetPlayListsThread().start();
            BufferedImage image = HttpUtil.downloadImage(MusicAPI.user.getAvatarUrl());
            File file = new File(ConfigManager.coverDir, "user_" + MusicAPI.user.getUid() + ".jpg");
            MusicAPI.user.setAvatarFile(file);
            try {
                ImageIO.write(image, "jpg", file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            MusicAPI.user.setAvatarTexture(new DynamicTexture(image));
        }
    }
}
