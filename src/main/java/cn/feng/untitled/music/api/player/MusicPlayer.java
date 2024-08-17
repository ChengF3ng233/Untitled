package cn.feng.untitled.music.api.player;

import cn.feng.untitled.Client;
import cn.feng.untitled.config.ConfigManager;
import cn.feng.untitled.music.api.base.LyricLine;
import cn.feng.untitled.music.api.base.Music;
import cn.feng.untitled.music.thread.ChangeMusicThread;
import cn.feng.untitled.ui.widget.impl.MusicInfoWidget;
import cn.feng.untitled.ui.widget.impl.MusicLyricWidget;
import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
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
                FFmpeg ffmpeg = FFmpeg.atPath(new File("../ffmpeg/bin").toPath());

                // 构建转换命令
                ffmpeg.addInput(UrlInput.fromUrl(songURL))
                        .addOutput(UrlOutput.toPath(converted.toPath()))
                        .addArguments("-c:a", "pcm_s16le")
                        .addArguments("-threads", Runtime.getRuntime().availableProcessors() + "")
                        .execute();
            }

            media = new Media(converted.toURI().toString());
        } else {
            media = new Media(songURL);
        }

        double volume = mediaPlayer == null ? 1d : mediaPlayer.getVolume();
        if (mediaPlayer != null) mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(this::next);
        mediaPlayer.setAudioSpectrumInterval(0.025);
        mediaPlayer.setAudioSpectrumListener((timestamp, duration, magnitudes, phases) -> {
            this.magnitudes = magnitudes;
        });
        mediaPlayer.setVolume(volume);
        music.getLyrics().forEach(LyricLine::reset);
        ((MusicLyricWidget) Client.instance.uiManager.getWidget(MusicLyricWidget.class)).reset();
        ((MusicInfoWidget) Client.instance.uiManager.getWidget(MusicInfoWidget.class)).reset();
        mediaPlayer.play();
    }

    public int getPlayedLyricCount() {
        int count = 0;
        for (LyricLine lyric : music.getLyrics()) {
            if (getCurrentTime() > lyric.getStart() + lyric.getDuration()) count++;
        }
        return count;
    }

    public int getPlayedTranslateCount() {
        int count = 0;
        for (LyricLine lyric : music.getLyrics()) {
            if (getCurrentTime() > lyric.getStart() + lyric.getDuration() && music.getTranslateMap().containsKey(lyric)) count++;
        }
        return count;
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
