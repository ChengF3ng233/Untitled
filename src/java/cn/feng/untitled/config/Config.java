package cn.feng.untitled.config;

import cn.feng.untitled.Client;

import java.io.File;
import java.io.IOException;

/**
 * @author ChengFeng
 * @since 2024/7/29
 **/
public abstract class Config {
    public String name;
    protected File configFile;

    public Config(String  configName, String fileName) {
        this.name = configName;
        this.configFile = new File(Client.instance.configManager.rootDir, fileName);
    }

    public abstract void loadConfig();
    public abstract void saveConfig();
}