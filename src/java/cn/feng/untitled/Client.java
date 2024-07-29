package cn.feng.untitled;

import cn.feng.untitled.command.CommandManager;
import cn.feng.untitled.config.ConfigManager;
import cn.feng.untitled.event.EventBus;
import cn.feng.untitled.module.ModuleManager;
import cn.feng.untitled.network.NetworkManager;
import cn.feng.untitled.ui.font.FontLoader;
import cn.feng.untitled.ui.hud.HudManager;
import cn.feng.untitled.util.misc.Logger;
import de.florianmichael.viamcp.ViaMCP;
import org.lwjgl.opengl.Display;

public enum Client {
    instance;

    public final String CLIENT_NAME = "Untitled";
    public EventBus eventBus;
    public ModuleManager moduleManager;
    public CommandManager commandManager;
    public HudManager hudManager;
    public NetworkManager networkManager;
    public ConfigManager configManager;

    public void start() {
        Logger.info("Client starting up...");
        try {
            ViaMCP.create();
            ViaMCP.INSTANCE.initAsyncSlider(); // For top left aligned slider
        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.info("Loading fonts...");
        FontLoader.registerFonts();

        Logger.info("Initializing managers...");
        eventBus = new EventBus();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        hudManager = new HudManager();
        networkManager = new NetworkManager();
        configManager = new ConfigManager();

        Logger.info("Registering...");
        eventBus.register(moduleManager);
        eventBus.register(commandManager);
        eventBus.register(hudManager);
        moduleManager.registerModules();
        commandManager.registerCommands();
        configManager.loadConfigs();
        hudManager.initGUI();

        Display.setTitle(CLIENT_NAME);
        Logger.info("Done.");
    }

    public void stop() {
        Logger.info("Client stopping...");

        Logger.info("Saving configs...");
        configManager.saveConfigs();
    }
}
