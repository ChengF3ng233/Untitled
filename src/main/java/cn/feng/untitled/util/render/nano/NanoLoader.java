package cn.feng.untitled.util.render.nano;

import net.minecraft.client.Minecraft;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;

/**
 * @author ChengFeng
 * @since 2024/8/3
 **/
public class NanoLoader {
    public static long vg;
    public static void createContext() {
        vg = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS);

        NanoVG.nvgShapeAntiAlias(vg, true);
    }
    public static void deleteContext() {
        NanoVGGL3.nvgDelete(vg);
    }
    public static boolean shouldRender() {
        return (Minecraft.getMinecraft() != null) && Minecraft.getMinecraft().thePlayer != null;
    }
}
