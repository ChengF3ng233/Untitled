package cn.feng.untitled.music.thread;

import cn.feng.untitled.Client;
import cn.feng.untitled.music.api.base.Music;
import cn.feng.untitled.music.api.MusicAPI;
import cn.feng.untitled.music.api.base.PlayList;
import cn.feng.untitled.music.ui.component.impl.MusicButton;
import lombok.Getter;

import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/8/14
 **/
@Getter
public class FetchMusicsThread extends Thread {
    private final PlayList playList;
    private final List<MusicButton> target;

    public FetchMusicsThread(PlayList playList, List<MusicButton> target) {
        this.playList = playList;
        this.target = target;
        setName("Music-FetchMusics");
    }

    @Override
    public void run() {
        MusicAPI.fetchMusicList(playList);
        for (Music music : playList.getMusicList()) {
            target.add(new MusicButton(music, playList));
        }
        Client.instance.musicManager.screen.player.setMusicList(playList.getMusicList());
    }
}
