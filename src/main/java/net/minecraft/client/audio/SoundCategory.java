package net.minecraft.client.audio;

import com.google.common.collect.Maps;

import java.util.Map;

public enum SoundCategory {
    MASTER("master", 0),
    MUSIC("music", 1),
    RECORDS("record", 2),
    WEATHER("weather", 3),
    BLOCKS("block", 4),
    MOBS("hostile", 5),
    ANIMALS("neutral", 6),
    PLAYERS("player", 7),
    AMBIENT("ambient", 8);

    private static final Map<String, SoundCategory> NAME_CATEGORY_MAP = Maps.newHashMap();
    private static final Map<Integer, SoundCategory> ID_CATEGORY_MAP = Maps.newHashMap();

    static {
        for (SoundCategory soundcategory : values()) {
            if (NAME_CATEGORY_MAP.containsKey(soundcategory.getCategoryName()) || ID_CATEGORY_MAP.containsKey(soundcategory.getCategoryId())) {
                throw new Error("Clash in Sound Category ID & Name pools! Cannot insert " + soundcategory);
            }

            NAME_CATEGORY_MAP.put(soundcategory.getCategoryName(), soundcategory);
            ID_CATEGORY_MAP.put(soundcategory.getCategoryId(), soundcategory);
        }
    }

    private final String categoryName;
    private final int categoryId;

    SoundCategory(String name, int id) {
        this.categoryName = name;
        this.categoryId = id;
    }

    public static SoundCategory getCategory(String name) {
        return NAME_CATEGORY_MAP.get(name);
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public int getCategoryId() {
        return this.categoryId;
    }
}
