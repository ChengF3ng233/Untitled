package cn.feng.untitled.music.thread;

import cn.feng.untitled.Client;
import cn.feng.untitled.music.api.base.LyricLine;
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
        if (music.getSongURL() == null) {
            music.setSongURL(MusicAPI.getMusicURL(music.getId()));
        }
        List<LyricLine> lyrics = music.getLyrics();
        if (lyrics.isEmpty()) {
            // 请求逐字歌词
            music.setLyrics(MusicAPI.getLyrics(music.getId()));
            // 纠正低级格式的duration
            music.correctLyricDuration();
        }
        List<LyricLine> translatedLyrics = music.getTranslatedLyrics();
        if (translatedLyrics.isEmpty() && music.isHasTranslate()) {
            // 请求翻译
            music.setTranslatedLyrics(MusicAPI.getTranslatedLyrics(music.getId()));
            if (!music.getTranslatedLyrics().isEmpty()) {
                music.generateTranslateMap();
            } else music.setHasTranslate(false);
        }
        Client.instance.musicManager.screen.player.setMusic(music);
        if (playList != null) {
            Client.instance.musicManager.screen.player.setMusicList(playList.getMusicList());
        }
    }
}
