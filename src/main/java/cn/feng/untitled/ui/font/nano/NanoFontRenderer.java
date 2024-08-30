package cn.feng.untitled.ui.font.nano;

import cn.feng.untitled.util.MinecraftInstance;
import cn.feng.untitled.util.data.resource.ResourceType;
import cn.feng.untitled.util.data.resource.ResourceUtil;
import cn.feng.untitled.util.misc.Logger;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static cn.feng.untitled.ui.font.nano.NanoLoader.vg;
import static org.lwjgl.nanovg.NanoVG.*;

/**
 * 此类输出的所有坐标均以guiScale为2的mc为准。
 *
 * @author ChengFeng
 * @since 2024/8/3
 **/
public class NanoFontRenderer extends MinecraftInstance {
    @Getter
    private final String name;
    private int font;
    private final NanoFontRenderer boldRenderer;
    @Setter
    private float size;

    public NanoFontRenderer(String name, String fileName) {
        this.name = name;
        try (InputStream inputStream = ResourceUtil.getResourceAsStream(fileName + ".ttf", ResourceType.FONT)) {
            byte[] data = inputStream.readAllBytes();
            ByteBuffer buffer = MemoryUtil.memAlloc(data.length);
            buffer.put(data).flip();

            font = nvgCreateFontMem(vg, name, buffer, false);
            size = 16;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boldRenderer = new NanoFontRenderer(name, fileName, font);
    }

    private NanoFontRenderer(String name, String fileName, int plainFont) {
        this.name = name + "-bold";
        try (InputStream inputStream = ResourceUtil.getResourceAsStream(fileName + "-bold.ttf", ResourceType.FONT)) {
            byte[] data = inputStream.readAllBytes();
            ByteBuffer buffer = MemoryUtil.memAlloc(data.length);
            buffer.put(data).flip();

            font = nvgCreateFontMem(vg, this.name, buffer, false);
            size = 16;
        } catch (NullPointerException ee) {
            Logger.warn("Bold font " + fileName + " not found.");
            font = plainFont;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boldRenderer = this;
    }

    public NanoFontRenderer bold() {
        return boldRenderer;
    }

    public float getHeight(float size) {
        nvgFontFaceId(vg, font);
        nvgFontSize(vg, size);
        float[] bounds = new float[4];
        nvgTextBounds(vg, 0, 0, "AaBbCcDdEeFfGgJjYy", bounds);
        return (bounds[3] - bounds[1]) / 2f;
    }

    public float getHeight(String text, float size) {
        if (text == null) return 0f;
        nvgFontFaceId(vg, font);
        nvgFontSize(vg, size);
        float[] bounds = new float[4];
        nvgTextBounds(vg, 0, 0, text, bounds);
        return (bounds[3] - bounds[1]) / 2f;
    }

    // Plain
    public void drawString(String text, float x, float y, float size, int align, Color color) {
        renderPlainString(text, x, y, size, align, color);
    }

    public void drawString(String text, float x, float y, int align, Color color) {
        renderPlainString(text, x, y, size, align, color);
    }

    public void drawString(String text, float x, float y, int align, Color color, boolean shadow) {
        drawString(text, x, y, size, align, color, shadow);
    }

    public void drawString(String text, float x, float y, float size, int align, Color color, boolean shadow) {
        int rgb = color.getRGB();

        if (shadow) {
            rgb = (rgb & 16579836) >> 2 | rgb & -16777216;
        }

        if (shadow) {
            renderPlainString(text, x + 0.5f, y + 0.5f, size, align, new Color(rgb));
        }
        renderPlainString(text, x, y, size, align, color);
    }

    public void drawString(String text, float x, float y, float size, Color color, boolean shadow) {
        int rgb = color.getRGB();

        if (shadow) {
            rgb = (rgb & 16579836) >> 2 | rgb & -16777216;
        }

        if (shadow) {
            renderPlainString(text, x + 0.5f, y + 0.5f, size, NVG_ALIGN_LEFT | NVG_ALIGN_TOP, new Color(rgb));
        }
        renderPlainString(text, x, y, size, NVG_ALIGN_LEFT | NVG_ALIGN_TOP, color);
    }

    public void drawString(String text, float x, float y, float size, Color color) {
        renderPlainString(text, x, y, size, NVG_ALIGN_LEFT | NVG_ALIGN_TOP, color);
    }

    public void drawString(String text, float x, float y, Color color) {
        renderPlainString(text, x, y, size, NVG_ALIGN_LEFT | NVG_ALIGN_TOP, color);
    }

    public void drawString(String text, float x, float y, Color col, boolean shadow) {
        drawString(text, x, y, size, NVG_ALIGN_LEFT | NVG_ALIGN_TOP, col, shadow);
    }

    // Glow
    public void drawGlowString(String text, float x, float y, Color color) {
        renderGlowString(text, x, y, size, 5f, NVG_ALIGN_LEFT | NVG_ALIGN_TOP, color, color);
    }

    public void drawGlowString(String text, float x, float y, Color color, boolean shadow) {
        int rgb = color.getRGB();

        if (shadow) {
            rgb = (rgb & 16579836) >> 2 | rgb & -16777216;
        }
        renderPlainString(text, x + 0.5f, y + 0.5f, size, NVG_ALIGN_LEFT | NVG_ALIGN_TOP, new Color(rgb));
        drawGlowString(text, x, y, color);
    }

    public void drawGlowString(String text, float x, float y, float size, Color color) {
        renderGlowString(text, x, y, size, 5f, NVG_ALIGN_LEFT | NVG_ALIGN_TOP, color, color);
    }

    public void drawGlowString(String text, float x, float y, float size, int align, Color color) {
        renderGlowString(text, x, y, size, 5f, align, color, color);
    }

    public void drawGlowString(String text, float x, float y, float size, float radius, int align, Color color) {
        renderGlowString(text, x, y, size, radius, align, color, color);
    }

    public void drawGlowString(String text, float x, float y, float size, int align, Color textColor, Color glowColor) {
        renderGlowString(text, x, y, size, 5f, align, textColor, glowColor);
    }

    public void drawGlowString(String text, float x, float y, float size, Color textColor, Color glowColor) {
        renderGlowString(text, x, y, size, 5f, NVG_ALIGN_LEFT | NVG_ALIGN_TOP, textColor, glowColor);
    }

    private void renderPlainString(String text, float x, float y, float size, int align, Color color) {
        nvgBeginPath(vg);
        nvgTextAlign(vg, align);

        float renderX = x * 2;
        float renderY = y * 2 + 2f;

        NanoUtil.fillColor(color);

        renderString(text, renderX, renderY, size);

        nvgClosePath(vg);
    }

    private void renderGlowString(String text, float x, float y, float size, float radius, int align, Color textColor, Color glowColor) {
        nvgBeginPath(vg);
        nvgTextAlign(vg, align);

        float renderX = x * 2;
        float renderY = y * 2 + 1f;

        NanoUtil.fillColor(glowColor);
        nvgFontBlur(vg, radius);
        renderString(text, renderX, renderY, size);

        NanoUtil.fillColor(textColor);
        nvgFontBlur(vg, 0f);
        renderString(text, renderX, renderY, size);

        nvgClosePath(vg);
    }


    private void renderString(String text, float x, float y, float size) {
        nvgFontFaceId(vg, font);
        nvgFontSize(vg, size);
        nvgText(vg, x, y, text);
    }

    /**
     * @param text 文本
     * @return 文本长度
     */
    public float getStringWidth(String text) {
        return getStringWidth(text, size);
    }

    /**
     * @param text 文本
     * @param size 字体大小
     * @return 文本长度
     */
    public float getStringWidth(String text, float size) {
        if (text == null) return 0f;
        nvgFontFaceId(vg, font);
        nvgFontSize(vg, size);
        float[] bounds = new float[4];
        nvgTextBounds(vg, 0, 0, text, bounds);
        return (bounds[2] - bounds[0]) / 2f;
    }

    public String trimStringToWidth(String text, float width, float size) {
        return this.trimStringToWidth(text, width, size, false, false);
    }

    public String trimStringToWidth(String text, float width) {
        return this.trimStringToWidth(text, width, size, false, false);
    }

    public String trimStringToWidth(String text, float width, boolean reverse) {
        return this.trimStringToWidth(text, width, size, reverse, false);
    }

    public String trimStringToWidth(String text, float width, float size, boolean reverse, boolean more) {
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
            String result = builder.toString();
            if (more && !text.equals(result)) {
                result = reverse ? "..." + result : result + "...";
            }
            return result;
        }
    }
}
