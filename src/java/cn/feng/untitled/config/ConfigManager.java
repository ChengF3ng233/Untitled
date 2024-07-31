package cn.feng.untitled.config;

import cn.feng.untitled.Client;
import cn.feng.untitled.util.MinecraftInstance;
import cn.feng.untitled.util.exception.MemberNotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/7/29
 **/
public class ConfigManager extends MinecraftInstance {
    public static final File rootDir = new File(mc.mcDataDir, Client.instance.CLIENT_NAME);
    public static final File fontDir = new File(rootDir, "font");
    private final List<Config> configList;

    public ConfigManager() {
        configList = new ArrayList<>();
        if (!rootDir.exists()) rootDir.mkdir();
        if (!fontDir.exists()) fontDir.mkdir();
    }

    public Config getConfig(String name) {
        for (Config config : configList) {
            if (config.name.equals(name)) return config;
        }

        throw new MemberNotFoundException("Config not found: " + name);
    }

    public void registerConfigs() {

    }

    public void loadConfigs() {
        configList.forEach(Config::loadConfig);
    }

    public void saveConfigs() {
        configList.forEach(it -> {
            if (!it.configFile.exists()) {
                try {
                    it.configFile.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        configList.forEach(Config::saveConfig);
    }
}
