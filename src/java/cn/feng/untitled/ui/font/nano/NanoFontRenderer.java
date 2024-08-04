package cn.feng.untitled.ui.font.nano;

import cn.feng.untitled.util.MinecraftInstance;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

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

    // Normal
    public void drawString(String text, float x, float y, float size, int align, Color color) {
        renderString(text, x, y, size, align, 0f, color);
    }

    public void drawString(String text, float x, float y, float size, Color color) {
        renderString(text, x, y, size, NVG_ALIGN_LEFT, 0f, color);
    }

    // Glow
    public void drawGlowString(String text, float x, float y, float size, Color color) {
        renderString(text, x, y, size, NVG_ALIGN_LEFT, 5f, color);
        renderString(text, x, y, size, NVG_ALIGN_LEFT, 0f, color);
    }

    public void drawGlowString(String text, float x, float y, float size, int align, Color color) {
        renderString(text, x, y, size, align, 5f, color);
        renderString(text, x, y, size, align, 0f, color);
    }

    private void renderString(String text, float x, float y, float size, int align, float blurRadius, Color color) {
        nvgBeginPath(NanoLoader.vg);

        NVGColor nvgColor = NVGColor.calloc();
        nvgColor.r(color.getRed() / 255f).g(color.getGreen() / 255f).b(color.getBlue() / 255f).a(color.getAlpha() / 255f);
        nvgFillColor(NanoLoader.vg, nvgColor);
        nvgColor.free();

        nvgTextAlign(NanoLoader.vg, align);
        nvgFontBlur(NanoLoader.vg, blurRadius);
        List<FontPair> fontPairs = NanoUtil.generateFontPair(text, this);

        float renderX = x * 2;
        float renderY = y * 2 + size;

        for (FontPair fontPair : fontPairs) {
            renderX = fontPair.renderer.renderString(fontPair.text, renderX, renderY, size);
        }
    }

    private float renderString(String text, float x, float y, float size) {
        nvgFontFaceId(NanoLoader.vg, font);
        nvgFontSize(NanoLoader.vg, size);
        return nvgText(NanoLoader.vg, x, y, text);
    }

    /**
     * 因为字体大小是指定的，所以任何字体获取的宽度都一致
     *
     * @param text 文本
     * @param size 字体大小
     * @return 文本长度
     */
    public float getStringWidth(String text, float size) {
        nvgFontFaceId(NanoLoader.vg, font);
        nvgFontSize(NanoLoader.vg, size);

        float[] bounds = new float[4];
        nvgTextBounds(NanoLoader.vg, 0, 0, text, bounds);

        return bounds[2] - bounds[0];
    }
}
