package cn.feng.untitled;

import cn.feng.untitled.command.CommandManager;
import cn.feng.untitled.event.EventBus;
import cn.feng.untitled.module.ModuleManager;
import cn.feng.untitled.ui.font.FontUtil;
import cn.feng.untitled.ui.hud.HudManager;
import cn.feng.untitled.util.misc.Logger;
import org.lwjgl.opengl.Display;

public enum Client {
    instance;

    public final String CLIENT_NAME = "Untitled";
    public EventBus eventBus;
    public ModuleManager moduleManager;
    public CommandManager commandManager;
    public HudManager hudManager;

    public void start() {
        Logger.info("Client preparing...");

        Logger.info("Loading fonts...");
        FontUtil.setupFonts();

        Logger.info("Initializing managers...");
        eventBus = new EventBus();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        hudManager = new HudManager();

        Logger.info("Registering...");
        eventBus.register(moduleManager);
        eventBus.register(commandManager);
        eventBus.register(hudManager);
        moduleManager.registerModules();
        commandManager.registerCommands();
        hudManager.initGUI();

        Display.setTitle(CLIENT_NAME);
    }

    public void stop() {
        Logger.info("Client stopping...");
    }
}
