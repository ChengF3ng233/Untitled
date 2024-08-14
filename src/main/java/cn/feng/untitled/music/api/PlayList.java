package cn.feng.untitled.music.api;

import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/8/13
 **/
@Getter
@Setter
public class PlayList {
    private final String name;
    private final String description;
    private long id;
    private File coverImage;

    private final List<Music> musicList = new ArrayList<>();

    public PlayList(String name, String description, long id, File coverImage) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.coverImage = coverImage;
    }

    public PlayList(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
