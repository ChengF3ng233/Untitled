package cn.feng.untitled;

import cn.feng.untitled.util.Logger;
import org.lwjgl.opengl.Display;

public enum Client {
    instance;

    public final String CLIENT_NAME = "Untitled";

    public void start() {
        Logger.info("Client preparing...");
        Display.setTitle(CLIENT_NAME);
    }

    public void stop() {
        Logger.info("Client stopping...");
    }
}
