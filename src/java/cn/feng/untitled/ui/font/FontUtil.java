package cn.feng.untitled.ui.font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;

/**
 * @author ChengFeng
 * @since 2024/7/29
 **/
public class FontUtil {
    private static final IResourceManager RESOURCE_MANAGER = Minecraft.getMinecraft().getResourceManager();

    public static java.awt.Font getResource(String resource, int size) {
        try {
            return java.awt.Font.createFont(0, RESOURCE_MANAGER.getResource(new ResourceLocation(resource)).getInputStream()).deriveFont((float) size);
        } catch (IOException | FontFormatException var3) {
            return null;
        }
    }
}
