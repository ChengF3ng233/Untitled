package cn.feng.untitled.music;

import cn.feng.untitled.Client;
import cn.feng.untitled.event.api.SubscribeEvent;
import cn.feng.untitled.event.impl.TickEvent;
import cn.feng.untitled.music.api.MusicAPI;
import cn.feng.untitled.music.api.base.Music;
import cn.feng.untitled.music.thread.GetPlayListsThread;
import cn.feng.untitled.music.ui.MusicPlayerScreen;
import cn.feng.untitled.util.MinecraftInstance;
import cn.feng.untitled.util.data.HttpUtil;
import net.minecraft.client.renderer.texture.DynamicTexture;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ChengFeng
 * @since 2024/8/12
 **/
public class MusicManager extends MinecraftInstance {
    public MusicPlayerScreen screen;
    public Map<Long, Music> musicMap = new HashMap<>();

    public void initGUI() {
        screen = new MusicPlayerScreen();
        Client.instance.eventBus.register(screen);

        // 如果已经登录，获取歌单列表
        if (MusicAPI.user.isLoggedIn()) {
            new GetPlayListsThread().start();
            BufferedImage image = HttpUtil.downloadImage(MusicAPI.user.getAvatarUrl());
            MusicAPI.user.setAvatarTexture(new DynamicTexture(image));
        }
    }

    @SubscribeEvent
    private void onTick(TickEvent e) {
        if ((mc.thePlayer == null || mc.theWorld == null) && (!screen.player.isPaused())) screen.player.pause();
    }
}
