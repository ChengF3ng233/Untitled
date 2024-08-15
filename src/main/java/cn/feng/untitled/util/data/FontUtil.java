package cn.feng.untitled.util.data;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;


/**
 * @author ChengFeng
 * @since 2024/7/30
 **/
public class FontUtil {
    private static final IResourceManager RESOURCE_MANAGER = Minecraft.getMinecraft().getResourceManager();

    public static Font getResource(String resource, int size) {
        try {
            return Font.createFont(0, RESOURCE_MANAGER.getResource(new ResourceLocation(resource)).getInputStream()).deriveFont((float)size);
        } catch (IOException | FontFormatException var3) {
            return null;
        }
    }

}
