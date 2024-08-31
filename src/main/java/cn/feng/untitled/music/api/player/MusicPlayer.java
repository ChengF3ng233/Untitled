package cn.feng.untitled.music.api.player;

import cn.feng.untitled.Client;
import cn.feng.untitled.config.ConfigManager;
import cn.feng.untitled.music.api.base.LyricLine;
import cn.feng.untitled.music.api.base.Music;
import cn.feng.untitled.music.api.base.MusicQuality;
import cn.feng.untitled.music.api.base.PlayList;
import cn.feng.untitled.music.thread.ChangeMusicThread;
import cn.feng.untitled.music.thread.FetchMusicsThread;
import cn.feng.untitled.music.ui.component.button.impl.MusicButton;
import cn.feng.untitled.ui.widget.impl.MusicLyricWidget;
import cn.feng.untitled.util.data.FFMPEGUtil;
import cn.feng.untitled.util.misc.Logger;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/8/14
 **/
public class MusicPlayer {
    @Getter
    private Music music;
    @Getter
    private MediaPlayer mediaPlayer;
    @Getter
    private PlayMode playMode = PlayMode.LOOP;
    @Getter
    private MusicQuality quality = MusicQuality.exhigh;
    @Getter
    private float[] magnitudes;

    @Setter
    private List<MusicButton> musicButtons;
    private PlayList playList;
    private List<Music> currentMusicList;

    public MusicPlayer() {
        new JFXPanel();
    }

    public double getCurrentTime() {
        return mediaPlayer.getCurrentTime().toMillis();
    }

    public void setCurrentTime(double time) {
        mediaPlayer.seek(Duration.millis(time));
        MusicLyricWidget widget = (MusicLyricWidget) Client.instance.uiManager.getWidget(MusicLyricWidget.class);
        widget.reset();
        music.getLyrics().forEach(LyricLine::reset);
        music.getTranslatedLyrics().forEach(LyricLine::reset);
    }

    public double getVolume() {
        return mediaPlayer.getVolume() * 100d;
    }

    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume / 100d);
    }

    public void setQuality(MusicQuality quality) {
        this.quality = quality;
        for (Music m : Client.instance.musicManager.musicMap.values()) {
            m.setSongURL(null);
        }
    }

    public void setMusic(Music music) {
        this.music = music;
        String songURL = music.getSongURL();
        Media media;

        if (songURL == null) {
            new ChangeMusicThread(music).start();
            return;
        }

        if (songURL.endsWith("flac")) {
            File converted = new File(ConfigManager.musicDir, music.getId() + ".wav");
            if (!converted.exists()) {
                FFMPEGUtil.convertMusic(songURL, converted);
            }
            media = new Media(converted.toURI().toString());
        } else {
            media = new Media(songURL);
        }

        double volume = mediaPlayer == null ? 1d : mediaPlayer.getVolume();
        if (mediaPlayer != null) mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(() -> next(false));
        mediaPlayer.setAudioSpectrumInterval(0.025);
        mediaPlayer.setAudioSpectrumListener((timestamp, duration, magnitudes, phases) -> {
            this.magnitudes = magnitudes;
        });
        mediaPlayer.setVolume(volume);
        music.getLyrics().forEach(LyricLine::reset);

        MusicLyricWidget widget = (MusicLyricWidget) Client.instance.uiManager.getWidget(MusicLyricWidget.class);
        widget.reset();
        Double value = widget.maxWidthValue.getValue();
        widget.maxWidthValue.getChangeAction().accept(value, value);

        mediaPlayer.play();

        if (currentMusicList != null && currentMusicList.indexOf(music) == currentMusicList.size() - 1 && !playList.isCompletelyDownloaded()) {
            // 预加载
            new FetchMusicsThread(playList, musicButtons, false).start();
        }
    }

    public void play() {
        mediaPlayer.play();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void previous(boolean force) {
        int index;
        if (playMode == PlayMode.RECYCLED && !force) {
            index = currentMusicList.indexOf(music);
        } else {
            index = currentMusicList.indexOf(music) - 1;
            if (index < 0) index = (playMode == PlayMode.LISTED ? 0 : currentMusicList.size() - 1);
        }
        setMusic(currentMusicList.get(index));
    }

    public void next(boolean force) {
        int index;
        if (playMode == PlayMode.RECYCLED && !force) {
            index = currentMusicList.indexOf(music);
        } else {
            index = currentMusicList.indexOf(music) + 1;
            if (index > currentMusicList.size() - 1)
                index = (playMode == PlayMode.LISTED ? currentMusicList.size() - 1 : 0);
        }
        setMusic(currentMusicList.get(index));
    }

    public void setPlaylist(PlayList playList) {
        this.playList = playList;
        setPlayMode(this.playMode, true);
        Logger.info("Playlist updated: " + playList.getName());
    }

    public void setPlayMode(PlayMode playMode, boolean force) {
        if (this.playMode == playMode && !force) return;
        this.playMode = playMode;
        switch (playMode) {
            case SHUFFLED -> Collections.shuffle(currentMusicList);
            case LISTED, LOOP -> currentMusicList = new ArrayList<>(playList.getMusicList());
        }
    }

    public boolean isPaused() {
        return mediaPlayer == null || mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED;
    }
}
