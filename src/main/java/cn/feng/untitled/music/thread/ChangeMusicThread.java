package cn.feng.untitled.music.thread;

import cn.feng.untitled.Client;
import cn.feng.untitled.music.api.base.LyricLine;
import cn.feng.untitled.music.api.base.LyricPair;
import cn.feng.untitled.music.api.base.Music;
import cn.feng.untitled.music.api.MusicAPI;
import cn.feng.untitled.music.api.base.PlayList;

import java.util.List;

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
        // 请求歌曲URL
        if (music.getSongURL() == null) {
            music.setSongURL(MusicAPI.getMusicURL(music.getId(), false, Client.instance.musicManager.screen.player.getQuality()));
        }

        List<LyricLine> lyrics = music.getLyrics();
        if (lyrics.isEmpty()) {
            // 请求逐字歌词
            LyricPair pair = MusicAPI.getLyrics(music.getId());
            music.setLyrics(pair.lyrics());
            // 纠正低级格式的duration
            music.correctLyricDuration();
            music.setTranslatedLyrics(pair.translatedLyrics());
            if (!music.getTranslatedLyrics().isEmpty()) {
                music.generateTranslateMap();
                music.setHasTranslate(true);
            }
        }

        Client.instance.musicManager.screen.player.setMusic(music);
        if (playList != null) {
            Client.instance.musicManager.screen.player.setPlaylist(playList);
        }
    }
}
