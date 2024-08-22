package cn.feng.untitled.util.data.resource;

import net.minecraft.util.ResourceLocation;

/**
 * @author ChengFeng
 * @since 2024/8/13
 **/
public class ResourceUtil {
    public static ResourceLocation getResource(String fileName, ResourceType type) {
        return new ResourceLocation(
                "untitled/" +
                        switch (type) {
                            case FONT -> "font/";
                            case ICON -> "icon/";
                            case IMAGE -> "image/";
                            case VIDEO -> "video/";
                        } +
                        fileName
        );
    }
}
