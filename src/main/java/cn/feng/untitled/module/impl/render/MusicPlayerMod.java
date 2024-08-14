package cn.feng.untitled.module.impl.render;

import cn.feng.untitled.Client;
import cn.feng.untitled.module.Module;
import cn.feng.untitled.module.ModuleCategory;

/**
 * @author ChengFeng
 * @since 2024/8/12
 **/
public class MusicPlayerMod extends Module {
    public MusicPlayerMod() {
        super("MusicPlayerScreen", ModuleCategory.Render);
        locked = true;
    }

    @Override
    public void onEnable() {
        mc.displayScreen(Client.instance.musicManager.screen);
    }
}
