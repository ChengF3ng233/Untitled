package cn.feng.untitled.ui.font.nano;

import cn.feng.untitled.util.MinecraftInstance;
import cn.feng.untitled.util.render.ColorUtil;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.nanovg.NanoVG.*;
import static cn.feng.untitled.ui.font.nano.NanoLoader.vg;

/**
 * @author ChengFeng
 * @since 2024/8/4
 **/
public class NanoUtil extends MinecraftInstance {
    private static final Map<String, List<FontPair>> fontPairs = new HashMap<>();

    public static void reloadPairs() {
        fontPairs.clear();
    }

    protected static boolean isEmoji(int codePoint) {
        return (codePoint >= 0x1F600 && codePoint <= 0x1F64F) || // Emoticons
                (codePoint >= 0x1F300 && codePoint <= 0x1F5FF) || // Miscellaneous Symbols and Pictographs
                (codePoint >= 0x1F680 && codePoint <= 0x1F6FF) || // Transport and Map Symbols
                (codePoint >= 0x1F900 && codePoint <= 0x1F9FF) || // Supplemental Symbols and Pictographs
                (codePoint >= 0x2600 && codePoint <= 0x26FF)   || // Miscellaneous Symbols
                (codePoint >= 0x2700 && codePoint <= 0x27BF)   || // Dingbats
                (codePoint >= 0x1F1E6 && codePoint <= 0x1F1FF) || // Regional Indicator Symbols
                (codePoint >= 0x1F700 && codePoint <= 0x1F77F);   // Alchemical Symbols
    }

    protected static boolean isEnglish(int codePoint) {
        return (codePoint >= 0x0020 && codePoint <= 0x007E) || // Basic Latin (including punctuation)
                (codePoint >= 0x00A0 && codePoint <= 0x00FF) || // Latin-1 Supplement (additional symbols)
                (codePoint >= 0x2000 && codePoint <= 0x206F);   // General Punctuation
    }

    protected static List<FontPair> generateFontPair(String text, NanoFontRenderer defaultRenderer) {
       // if (fontPairs.containsKey(text)) return fontPairs.get(text);

        List<FontPair> renderList = new ArrayList<>();
        int[] codePoints = text.codePoints().toArray();

        StringBuilder english = new StringBuilder();
        StringBuilder emoji = new StringBuilder();
        StringBuilder international = new StringBuilder();

        NanoFontRenderer emojiRenderer = NanoFontLoader.emoji;
        NanoFontRenderer internationalRenderer = NanoFontLoader.misans;

        for (int codePoint : codePoints) {
            String str = new String(Character.toChars(codePoint));

            // 确定字符类型并更新对应的 StringBuilder
            if (isEnglish(codePoint)) {
                if (!emoji.isEmpty() && english.isEmpty()) {
                    renderList.add(new FontPair(emoji.toString(), emojiRenderer));
                    emoji = new StringBuilder();
                }
                if (!international.isEmpty() && english.isEmpty()) {
                    renderList.add(new FontPair(international.toString(), internationalRenderer));
                    international = new StringBuilder();
                }
                english.append(str);
            } else if (NanoUtil.isEmoji(codePoint)) {
                if (!international.isEmpty() && emoji.isEmpty()) {
                    renderList.add(new FontPair(international.toString(), internationalRenderer));
                    international = new StringBuilder();
                }
                if (!english.isEmpty() && emoji.isEmpty()) {
                    renderList.add(new FontPair(english.toString(), defaultRenderer));
                    english = new StringBuilder();
                }
                emoji.append(str);
            } else if (str.equals("\\n")) {
                renderList.add(new FontPair("ENTER", null));
            } else {
                if (!english.isEmpty() && international.isEmpty()) {
                    renderList.add(new FontPair(english.toString(), defaultRenderer));
                    english = new StringBuilder();
                }
                if (!emoji.isEmpty() && international.isEmpty()) {
                    renderList.add(new FontPair(emoji.toString(), emojiRenderer));
                    emoji = new StringBuilder();
                }
                international.append(str);
            }
        }

        // 处理剩余的文本
        if (!english.isEmpty()) {
            renderList.add(new FontPair(english.toString(), defaultRenderer));
        }
        if (!emoji.isEmpty()) {
            renderList.add(new FontPair(emoji.toString(), emojiRenderer));
        }
        if (!international.isEmpty()) {
            renderList.add(new FontPair(international.toString(), internationalRenderer));
        }

      //  fontPairs.put(text, renderList);
        return renderList;
    }

    public static void beginFrame() {
        nvgBeginFrame(vg, mc.displayWidth, mc.displayHeight, 1f);
    }

    public static void scaleStart(float centerX, float centerY, float scale) {
        nvgSave(vg);
        nvgTranslate(vg, centerX * 2, centerY * 2);
        nvgScale(vg, scale, scale);
        nvgTranslate(vg, -centerX * 2, -centerY * 2);
    }

    public static void scaleEnd() {
        nvgRestore(vg);
    }

    /**
     * 绘制折线（Polyline）
     *
     * @param vg NanoVG 上下文
     * @param points 折线顶点数组，每两个浮点数为一个点 (x, y)
     * @param color 线条颜色
     * @param lineWidth 线条宽度
     */
    public static void drawPolyline(float[] points, int color, float lineWidth) {
        if (points.length < 4) {
            throw new IllegalArgumentException("At least two points are required to draw a line.");
        }

        // 开始绘制路径
        nvgBeginPath(vg);

        // 设置线条颜色和宽度
        nvgStrokeColor(new Color(color));
        nvgStrokeWidth(vg, lineWidth * 2f);

        // 移动到第一个点
        nvgMoveTo(vg, points[0] * 2f, points[1] * 2f);

        // 添加折线的每个点
        for (int i = 2; i < points.length; i += 2) {
            nvgLineTo(vg, points[i] * 2f, points[i + 1] * 2f);
        }

        // 描边路径
        nvgStroke(vg);
    }

    public static void drawInterpolatedLine(float[] points, int segments) {
        if (points.length < 4) {
            throw new IllegalArgumentException("At least two points are required to draw a line.");
        }

        // 开始绘制路径
        nvgBeginPath(vg);

        // 移动到第一个点
        nvgMoveTo(vg, points[0] * 2f, points[1] * 2f);

        // 线性插值
        for (int i = 2; i < points.length; i += 2) {
            float x1 = points[i - 2];
            float y1 = points[i - 1];
            float x2 = points[i];
            float y2 = points[i + 1];

            // 插值点数
            for (int j = 0; j <= segments; j++) {
                float t = j / (float) segments;
                float x = x1 + t * (x2 - x1);
                float y = y1 + t * (y2 - y1);
                if (j == 0 && i == 2) {
                    nvgMoveTo(vg, x * 2f, y * 2f); // 起始点
                } else {
                    nvgLineTo(vg, x * 2f, y * 2f); // 中间点
                }
            }
        }

        // 描边路径
        nvgStroke(vg);
    }

    public static void rotateStart(float centerX, float centerY, float angle) {
        // 保存当前状态
        nvgSave(vg);

        // 移动到绘制区域的中心点
        nvgTranslate(vg, centerX * 2f, centerY * 2f);

        nvgRotate(vg, NVG_PI / angle); // NVG_PI / 6 = 30度
    }

    public static void rotateEnd() {
        nvgRestore(vg);
    }

    public static void transformStart(float x, float y) {
        nvgSave(vg);
        nvgTranslate(vg, x * 2f, y * 2f);
    }

    public static void transformEnd() {
        nvgRestore(vg);
    }
    
    public static void scissorStart(float x, float y, float width, float height) {
        nvgSave(vg);
        nvgScissor(vg, x * 2f, y * 2f, width * 2f, height * 2f);
    }
    
    public static void scissorEnd() {
        nvgRestore(vg);
    }

    public static void nvgColor(Color color) {
        NVGColor nvgColor = NVGColor.calloc();
        nvgColor.r(color.getRed() / 255f).g(color.getGreen() / 255f).b(color.getBlue() / 255f).a(color.getAlpha() / 255f);
        nvgFillColor(NanoLoader.vg, nvgColor);
        nvgColor.free();
    }

    public static void nvgStrokeColor(Color color) {
        NVGColor nvgColor = NVGColor.calloc();
        nvgColor.r(color.getRed() / 255f).g(color.getGreen() / 255f).b(color.getBlue() / 255f).a(color.getAlpha() / 255f);
        NanoVG.nvgStrokeColor(vg, nvgColor);
        nvgColor.free();
    }

    public static void endFrame() {
        nvgEndFrame(vg);
    }
}
