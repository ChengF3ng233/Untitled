package cn.feng.untitled.config;

import com.google.gson.JsonObject;

import java.io.File;

/**
 * @author ChengFeng
 * @since 2024/7/29
 **/
public abstract class Config {
    public String name;
    protected File configFile;

    public Config(String configName, String fileName) {
        this.name = configName;
        this.configFile = new File(ConfigManager.rootDir, fileName);
    }

    public abstract void loadConfig(JsonObject object);

    public abstract JsonObject saveConfig();
}
