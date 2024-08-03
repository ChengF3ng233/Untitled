package cn.feng.untitled.ui.font.nano;

import cn.feng.untitled.module.impl.client.PostProcessing;
import cn.feng.untitled.util.MinecraftInstance;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * @author ChengFeng
 * @since 2024/8/3
 **/
public class NanoFontRenderer extends MinecraftInstance {
    private final int font;

    public NanoFontRenderer(String name, ResourceLocation resource) {
        try (InputStream inputStream = mc.getResourceManager().getResource(resource).getInputStream()) {
            byte[] data = inputStream.readAllBytes();
            ByteBuffer buffer = MemoryUtil.memAlloc(data.length);
            buffer.put(data).flip();

            font = nvgCreateFontMem(NanoLoader.vg, name, buffer, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void drawString(String text, float x, float y, float size, int align, Color color) {
        nvgBeginFrame(NanoLoader.vg, mc.displayWidth, mc.displayHeight, 1f);
        nvgBeginPath(NanoLoader.vg);
        nvgFontFaceId(NanoLoader.vg, font);
        nvgFontSize(NanoLoader.vg, size);
        NVGColor nvgColor = NVGColor.create();
        nvgRGBAf(color.getRed() / 255f, color.getRed() / 255f, color.getRed() / 255f, color.getRed() / 255f, nvgColor);
        nvgFillColor(NanoLoader.vg, nvgColor);
        nvgTextAlign(NanoLoader.vg, align);

        nvgText(NanoLoader.vg, x, y, text);
        nvgEndFrame(NanoLoader.vg);
    }
}
