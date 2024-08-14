package cn.feng.untitled.music.thread;

import cn.feng.untitled.Client;
import cn.feng.untitled.music.api.Music;
import cn.feng.untitled.music.api.MusicAPI;
import cn.feng.untitled.music.api.PlayList;

/**
 * @author ChengFeng
 * @since 2024/8/14
 **/
public class ChangeMusicThread extends Thread {
    private final Music music;
    private final PlayList playList;

    public ChangeMusicThread(Music music, PlayList playList) {
        this.music = music;
        this.playList = playList;
        setName("Music-ChangeMusic");
    }

    public ChangeMusicThread(Music music) {
        this.music = music;
        this.playList = null;
        setName("Music-ChangeMusic");
    }

    @Override
    public void run() {
        if (music.getSongURL() == null) {
            music.setSongURL(MusicAPI.getMusicURL(music.getId()));
        }
        Client.instance.musicManager.screen.player.setMusic(music);
        if (playList != null) {
            Client.instance.musicManager.screen.player.setMusicList(playList.getMusicList());
        }
        Client.instance.musicManager.screen.player.play();
    }
}
