package cn.feng.untitled.util.misc;

import cn.feng.untitled.Client;
import cn.feng.untitled.util.MinecraftInstance;
import net.minecraft.util.ChatComponentText;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class ChatUtil extends MinecraftInstance {
    public static void sendMessage(String message) {
        mc.thePlayer.addChatMessage(new ChatComponentText("§f[§3" + Client.instance.CLIENT_NAME + "§f] " + message));
    }
}
