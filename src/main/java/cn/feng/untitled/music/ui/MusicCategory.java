package cn.feng.untitled.music.ui;

import cn.feng.untitled.util.data.resource.ResourceType;
import cn.feng.untitled.util.data.resource.ResourceUtil;
import net.minecraft.util.ResourceLocation;

public enum MusicCategory {
    RECOMMENDED("home.png"),
    LIKED("favorite.png");

    public final ResourceLocation icon;
    MusicCategory(String iconFile) {
        icon = ResourceUtil.getResource(iconFile, ResourceType.ICON);
    }
}
