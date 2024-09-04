package cn.feng.untitled;

import cn.feng.untitled.command.CommandManager;
import cn.feng.untitled.config.ConfigManager;
import cn.feng.untitled.event.EventBus;
import cn.feng.untitled.module.ModuleManager;
import cn.feng.untitled.music.MusicManager;
import cn.feng.untitled.network.NetworkManager;
import cn.feng.untitled.ui.UIManager;
import cn.feng.untitled.ui.font.awt.AWTFontLoader;
import cn.feng.untitled.util.misc.Logger;
import de.florianmichael.viamcp.ViaMCP;
import dev.tr7zw.entityculling.EntityCulling;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public enum Client {
    instance;

    public final String CLIENT_NAME = "Untitled";
    public EventBus eventBus;
    public ModuleManager moduleManager;
    public CommandManager commandManager;
    public UIManager uiManager;
    public NetworkManager networkManager;
    public ConfigManager configManager;
    public MusicManager musicManager;

    public boolean loaded = false;

    public void start() {
        Logger.info("Client starting up...");
        long start = System.currentTimeMillis();

        try {
            ViaMCP.create();
            ViaMCP.INSTANCE.initAsyncSlider(); // For top left aligned slider
        } catch (Exception e) {
            e.printStackTrace();
        }

        EntityCulling.instance.onInitialize();

        Logger.info("Initializing managers...");
        eventBus = new EventBus();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        uiManager = new UIManager();
        networkManager = new NetworkManager();
        configManager = new ConfigManager();
        musicManager = new MusicManager();

        Logger.info("Loading fonts...");
        AWTFontLoader.registerFonts();

        Logger.info("Registering...");
        eventBus.register(moduleManager);
        eventBus.register(commandManager);
        eventBus.register(uiManager);
        eventBus.register(musicManager);
        moduleManager.registerModules();
        commandManager.registerCommands();
        configManager.registerConfigs();
        uiManager.registerWidgets();

        Logger.info("Miscellaneous...");
        configManager.loadConfigs();
        uiManager.initGUI();
        musicManager.initGUI();
        Keyboard.enableRepeatEvents(false);

        Display.setTitle(CLIENT_NAME + " | LWJGL Version " + Sys.getVersion());
        Logger.info("Finished loading in " + (System.currentTimeMillis() - start) / 1000f + " seconds.");

        loaded = true;
    }

    public void stop() {
        Logger.info("Client stopping...");
        Logger.info("Saving configs...");
        configManager.saveConfigs();
    }
}
