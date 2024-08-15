package cn.feng.untitled.config.impl;

import cn.feng.untitled.config.Config;
import cn.feng.untitled.music.api.MusicAPI;
import cn.feng.untitled.music.api.user.User;
import cn.feng.untitled.util.data.DataUtil;
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
        MusicAPI.user = DataUtil.gson.fromJson(object, User.class);
    }

    @Override
    public JsonObject saveConfig() {
        return DataUtil.gson.fromJson(DataUtil.gson.toJson(MusicAPI.user), JsonObject.class);
    }
}
