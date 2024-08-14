package cn.feng.untitled.music;

import cn.feng.untitled.music.api.MusicAPI;
import cn.feng.untitled.music.ui.MusicPlayerScreen;
import cn.feng.untitled.music.thread.GetPlayListsThread;
import cn.feng.untitled.util.data.HttpUtil;
import net.minecraft.client.renderer.texture.DynamicTexture;

/**
 * @author ChengFeng
 * @since 2024/8/12
 **/
public class MusicManager {
    public MusicPlayerScreen screen;

    public void initGUI() {
        screen = new MusicPlayerScreen();

        // 如果已经登录，获取歌单列表
        if (MusicAPI.user.isLoggedIn()) {
            new GetPlayListsThread().start();
            MusicAPI.user.setAvatarTexture(new DynamicTexture(HttpUtil.downloadImage(MusicAPI.user.getAvatarUrl(), 200, 200)));
        }
    }
}
