package cn.feng.untitled.ui.font.nano;

import cn.feng.untitled.util.MinecraftInstance;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;

import static cn.feng.untitled.ui.font.nano.NanoLoader.vg;
import static org.lwjgl.nanovg.NanoVG.*;

/**
 * @author ChengFeng
 * @since 2024/8/4
 **/
public class NanoUtil extends MinecraftInstance {
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
     * @param points    折线顶点数组，每两个浮点数为一个点 (x, y)
     * @param color     线条颜色
     * @param lineWidth 线条宽度
     */
    public static void drawPolyline(float[] points, int color, float lineWidth) {
        if (points.length < 4) {
            throw new IllegalArgumentException("At least two points are required to render a line.");
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
