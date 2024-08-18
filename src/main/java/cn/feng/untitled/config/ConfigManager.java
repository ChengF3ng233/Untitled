package cn.feng.untitled.config;

import cn.feng.untitled.Client;
import cn.feng.untitled.config.impl.ModuleConfig;
import cn.feng.untitled.config.impl.MusicConfig;
import cn.feng.untitled.config.impl.WidgetConfig;
import cn.feng.untitled.util.MinecraftInstance;
import cn.feng.untitled.util.exception.MemberNotFoundException;
import cn.feng.untitled.util.misc.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/7/29
 **/
public class ConfigManager extends MinecraftInstance {
    public static final File rootDir = new File(mc.mcDataDir, Client.instance.CLIENT_NAME);
    public static final File coverDir = new File(rootDir, "cover");
    public static final File musicDir = new File(rootDir, "music");
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final List<Config> configList;

    public ConfigManager() {
        configList = new ArrayList<>();
        if (!rootDir.exists()) rootDir.mkdir();
        if (!coverDir.exists()) coverDir.mkdir();
        if (!musicDir.exists()) musicDir.mkdir();
    }

    public Config getConfig(String name) {
        for (Config config : configList) {
            if (config.name.equals(name)) return config;
        }

        throw new MemberNotFoundException("Config not found: " + name);
    }

    public Config getConfig(Class<? extends Config> klass) {
        for (Config config : configList) {
            if (config.getClass() == klass) return config;
        }

        throw new MemberNotFoundException("Config not found: " + klass.getName());
    }

    public void saveConfig(Class<? extends Config> klass) {
        Config config = getConfig(klass);
        JsonObject object = config.saveConfig();
        try {
            PrintWriter pw = new PrintWriter(config.configFile);
            pw.write(ConfigManager.gson.toJson(object));
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            Logger.error("Failed to write " + config.name + " klass.");
        }
    }

    public void registerConfigs() {
        configList.add(new ModuleConfig());
        configList.add(new MusicConfig());
        configList.add(new WidgetConfig());
    }

    public void loadConfigs() {
        for (Config config : configList) {
            if (!config.configFile.exists()) continue;
            try {
                JsonObject object = ConfigManager.gson.fromJson(new FileReader(config.configFile), JsonObject.class);
                config.loadConfig(object);
            } catch (FileNotFoundException e) {
                Logger.error("Failed to load config " + config.name);
            }
        }
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
        for (Config config : configList) {
            JsonObject object = config.saveConfig();
            try {
                PrintWriter pw = new PrintWriter(config.configFile);
                pw.write(ConfigManager.gson.toJson(object));
                pw.flush();
                pw.close();
            } catch (FileNotFoundException e) {
                Logger.error("Failed to write " + config.name + " config.");
            }
        }
    }
}
