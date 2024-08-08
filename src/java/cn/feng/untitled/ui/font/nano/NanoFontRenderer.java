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
 * 所有的坐标都是mc的两倍，所以获取的时候要做乘除法
 * @author ChengFeng
 * @since 2024/8/3
 **/
public class NanoFontRenderer extends MinecraftInstance {
    private final int font;
    private float size;

    public NanoFontRenderer(String name, ResourceLocation resource) {
        try (InputStream inputStream = mc.getResourceManager().getResource(resource).getInputStream()) {
            byte[] data = inputStream.readAllBytes();
            ByteBuffer buffer = MemoryUtil.memAlloc(data.length);
            buffer.put(data).flip();

            font = nvgCreateFontMem(NanoLoader.vg, name, buffer, false);
            size = 16;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public float getSize() {
        return size / 2f;
    }

    public void setSize(float size) {
        this.size = size;
    }

    // Normal
    public void drawString(String text, float x, float y, float size, int align, Color color) {
        renderString(text, x, y, size, align, 0f, color);
    }

    public void drawString(String text, float x, float y, int align, Color color) {
        renderString(text, x, y, size, align, 0f, color);
    }

    public void drawString(String text, float x, float y, int align, Color color, boolean shadow) {
        drawString(text, x, y, size, align, color, shadow);
    }

    public void drawString(String text, float x, float y, float size, int align, Color col, boolean shadow) {
        int color = col.getRGB();

        if (shadow) {
            color = (color & 16579836) >> 2 | color & -16777216;
        }

        if (shadow) {
            renderString(text, x + 0.5f, y + 0.5f, size, align, 0f, new Color(color));
        }
        renderString(text, x, y, size, align, 0f, col);
    }

    public void drawString(String text, float x, float y, float size, Color color) {
        renderString(text, x, y, size, NVG_ALIGN_LEFT, 0f, color);
    }

    public void drawString(String text, float x, float y, Color color) {
        renderString(text, x, y, size, NVG_ALIGN_LEFT, 0f, color);
    }

    public void drawString(String text, float x, float y, Color col, boolean shadow) {
        drawString(text, x, y, size, NVG_ALIGN_LEFT, col, shadow);
    }

    // Glow
    public void drawGlowString(String text, float x, float y, Color color) {
        renderString(text, x, y, size, NVG_ALIGN_LEFT, 5f, color);
        renderString(text, x, y, size, NVG_ALIGN_LEFT, 0f, color);
    }

    public void drawGlowString(String text, float x, float y, Color col, boolean shadow) {
        int color = col.getRGB();

        if (shadow) {
            color = (color & 16579836) >> 2 | color & -16777216;
        }
        renderString(text, x + 0.5f, y + 0.5f, size, NVG_ALIGN_LEFT, 0f, new Color(color));
        drawGlowString(text, x, y, col);
    }

    public void drawGlowString(String text, float x, float y, float size, Color color) {
        renderString(text, x, y, size, NVG_ALIGN_LEFT, 5f, color);
        renderString(text, x, y, size, NVG_ALIGN_LEFT, 0f, color);
    }

    public void drawGlowString(String text, float x, float y, float size, int align, Color color) {
        renderString(text, x, y, size, align, 5f, color);
        renderString(text, x, y, size, align, 0f, color);
    }

    public void drawGlowString(String text, float x, float y, float size, float radius, int align, Color textColor, Color glowColor) {
        renderString(text, x, y, size, align, radius, glowColor);
        renderString(text, x, y, size, align, 0f, textColor);
    }

    public void drawGlowString(String text, float x, float y, float size, float radius, Color textColor, Color glowColor) {
        renderString(text, x, y, size, NVG_ALIGN_LEFT, radius, glowColor);
        renderString(text, x, y, size, NVG_ALIGN_LEFT, 0f, textColor);
    }

    private void renderString(String text, float x, float y, float size, int align, float blurRadius, Color color) {
        nvgSave(NanoLoader.vg);
        nvgBeginPath(NanoLoader.vg);

        NanoUtil.nvgColor(color);

        nvgTextAlign(NanoLoader.vg, align);
        nvgFontBlur(NanoLoader.vg, blurRadius);
        List<FontPair> fontPairs = NanoUtil.generateFontPair(text, this);

        float renderX = x * 2;
        float renderY = y * 2 + size;

        for (FontPair fontPair : fontPairs) {
            renderX = fontPair.renderer.renderString(fontPair.text, renderX, renderY, size);
        }
        nvgRestore(NanoLoader.vg);
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
     * @return 文本长度
     */
    public float getStringWidth(String text) {
        nvgFontFaceId(NanoLoader.vg, font);
        nvgFontSize(NanoLoader.vg, size);

        float[] bounds = new float[4];
        nvgTextBounds(NanoLoader.vg, 0, 0, text, bounds);

        return (bounds[2] - bounds[0]) / 2f;
    }

    /**
     * 因为字体大小是指定的，所以任何字体获取的宽度都一致
     *
     * @param text 文本
     * @param size 字体大小
     * @return 文本长度
     */
    public float getStringWidth(String text, float size) {
        float width = 0f;
        List<FontPair> fontPairs = NanoUtil.generateFontPair(text, this);
        for (FontPair fontPair : fontPairs) {
            width += fontPair.renderer.getStringPartWidth(fontPair.text, size);
        }
        return width;
    }

    private float getStringPartWidth(String text, float size) {
        nvgFontFaceId(NanoLoader.vg, font);
        nvgFontSize(NanoLoader.vg, size);

        float[] bounds = new float[4];
        nvgTextBounds(NanoLoader.vg, 0, 0, text, bounds);

        return (bounds[2] - bounds[0]) / 2f;
    }

    public String trimStringToWidth(String text, float width, float size) {
        return this.trimStringToWidth(text, width, size, false);
    }

    public String trimStringToWidth(String text, float width) {
        return this.trimStringToWidth(text, width, size, false);
    }

    public String trimStringToWidth(String text, float width, boolean reverse) {
        return this.trimStringToWidth(text, width, size, reverse);
    }

    public String trimStringToWidth(String text, float width, float size, boolean reverse) {
        if (text == null) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder();
            int startIndex = reverse ? text.length() - 1 : 0;
            int step = reverse ? -1 : 1;

            String nextChar = "";
            for (int i = startIndex; i <= text.length() - 1 && i >= 0 && getStringWidth(builder + nextChar, size) <= width; i += step) {
                builder.append(text.charAt(i));
                nextChar = reverse ? (i == 0 ? "" : String.valueOf(text.charAt(i + step))) : (i == text.length() - 1 ? "" : String.valueOf(text.charAt(i + step)));
            }

            if (reverse) builder.reverse();
            return builder.toString();
        }
    }
}
