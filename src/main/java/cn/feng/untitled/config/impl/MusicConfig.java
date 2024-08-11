package cn.feng.untitled.config.impl;

import cn.feng.untitled.config.Config;
import cn.feng.untitled.music.api.MusicAPI;
import com.google.gson.JsonObject;

/**
 * @author ChengFeng
 * @since 2024/8/12
 **/
public class MusicConfig extends Config {
    public MusicConfig() {
        super("music", "Music.json");
    }

    @Override
    public void loadConfig(JsonObject object) {
        if (object.has("cookie")) MusicAPI.cookie = object.get("cookie").getAsString();
    }

    @Override
    public JsonObject saveConfig() {
        JsonObject config = new JsonObject();
        config.addProperty("cookie", MusicAPI.cookie);
        return config;
    }
}
