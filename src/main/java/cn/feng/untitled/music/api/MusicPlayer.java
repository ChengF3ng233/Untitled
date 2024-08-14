package cn.feng.untitled.music.api;

import cn.feng.untitled.music.thread.ChangeMusicThread;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

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
    @Setter
    private PlayMode playMode = PlayMode.ORDERED;
    @Getter
    private float[] magnitudes;

    private List<Music> currentMusicList;

    public MusicPlayer() {
        new JFXPanel();
    }

    public double getCurrentTime() {
        return mediaPlayer.getCurrentTime().toMillis();
    }

    public void setCurrentTime(double time) {
        mediaPlayer.seek(Duration.millis(time));
    }

    public double getVolume() {
        return mediaPlayer.getVolume() * 100d;
    }

    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume / 100d);
    }

    public void setMusic(Music music) {
        this.music = music;
        if (music.getSongURL() == null) {
            new ChangeMusicThread(music).start();
            return;
        }
        double volume = mediaPlayer == null? 1d : mediaPlayer.getVolume();
        Media media = new Media(music.getSongURL());
        if (mediaPlayer != null) mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(this::next);
        mediaPlayer.setAudioSpectrumInterval(0.01);
        mediaPlayer.setAudioSpectrumListener((timestamp, duration, magnitudes, phases) -> {
            this.magnitudes = magnitudes;
        });
        mediaPlayer.setVolume(volume);
    }

    public void play() {
        mediaPlayer.play();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void previous() {
        int index = currentMusicList.indexOf(music) - 1;
        if (index < 0) index = currentMusicList.size() - 1;
        setMusic(currentMusicList.get(index));
    }

    public void next() {
        int index = currentMusicList.indexOf(music) + 1;
        if (index >= currentMusicList.size() - 1) index = 0;
        setMusic(currentMusicList.get(index));
    }

    public void setMusicList(List<Music> list) {
        currentMusicList = list;
        if (playMode == PlayMode.SHUFFLE) Collections.shuffle(currentMusicList);
    }

    public boolean isPaused() {
        return mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED;
    }
}
