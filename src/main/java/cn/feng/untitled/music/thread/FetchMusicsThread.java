package cn.feng.untitled.music.thread;

import cn.feng.untitled.Client;
import cn.feng.untitled.music.api.base.Music;
import cn.feng.untitled.music.api.MusicAPI;
import cn.feng.untitled.music.api.base.PlayList;
import cn.feng.untitled.music.ui.component.button.impl.MusicButton;
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
    private final boolean updateList;

    public FetchMusicsThread(PlayList playList, List<MusicButton> target, boolean updateList) {
        this.playList = playList;
        this.target = target;
        this.updateList = updateList;
        setName("Music-FetchMusics");
    }

    @Override
    public void run() {
        List<Music> musicList = MusicAPI.fetchMusicList(playList);
        for (Music music : musicList) {
            target.add(new MusicButton(music, playList));
        }
        if (updateList) {
            Client.instance.musicManager.screen.player.setPlaylist(playList);
        }
        Client.instance.musicManager.screen.player.setMusicButtons(target);
    }
}
