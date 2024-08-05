package cn.feng.untitled.module.impl.render;

import cn.feng.untitled.module.Module;
import cn.feng.untitled.module.ModuleCategory;

/**
 * @author ChengFeng
 * @since 2024/8/5
 **/
public class FullBright extends Module {
    public FullBright() {
        super("FullBright", ModuleCategory.Render);
    }

    private float oldGamma;

    @Override
    public void onEnable() {
        oldGamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 1000f;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = oldGamma;
    }
}
