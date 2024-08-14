package cn.feng.untitled.music.api;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.renderer.texture.DynamicTexture;

import java.io.File;

/**
 * @author ChengFeng
 * @since 2024/8/13
 **/
@Getter
@Setter
public class Music {
    private final String name;
    private final String artist;
    private final String album;
    private String songURL;
    private final long duration;
    private final long id;
    private final File coverImage;
    private DynamicTexture texture;

    public Music(String name, String artist, String album, long id, long duration, File image) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.id = id;
        this.duration = duration;
        this.coverImage = image;
    }
}
