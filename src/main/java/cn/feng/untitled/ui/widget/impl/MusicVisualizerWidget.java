package cn.feng.untitled.ui.widget.impl;

import cn.feng.untitled.Client;
import cn.feng.untitled.music.ui.ThemeColor;
import cn.feng.untitled.util.render.nano.NanoUtil;
import cn.feng.untitled.ui.widget.Widget;
import cn.feng.untitled.util.data.MathUtil;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.value.impl.BoolValue;
import cn.feng.untitled.value.impl.ColorValue;
import cn.feng.untitled.value.impl.ModeValue;
import cn.feng.untitled.value.impl.NumberValue;
import javafx.scene.media.MediaPlayer;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/8/14
 **/
public class MusicVisualizerWidget extends Widget {
    public MusicVisualizerWidget() {
        super("MusicVisualizer", true);
        x = 0.5f;
        y = 0.5f;
    }

    private final NumberValue bands = new NumberValue("Bands", 128, 256, 16, 1);
    private final NumberValue heightValue = new NumberValue("Height", 50, 100, 30, 1);
    private final NumberValue widthValue = new NumberValue("Width", 100, 500, 50, 1);
    private final BoolValue fillValue = new BoolValue("Fill", true);
    private final NumberValue rectSpace = new NumberValue("RectSpace", 0f, 5f, 0f, 0.01f);
    private final NumberValue rectRadius = new NumberValue("RectRadius", 3f, 5f, 0f, 0.1f);
    private final ColorValue colorValue = new ColorValue("Color", ThemeColor.redColor);
    private final NumberValue indexOffset = new NumberValue("IndexOffset", 6f, 20f, 0f, 1f);

    private final ModeValue modeValue = new ModeValue("Mode", "Polyline", new String[]{"Polyline", "Rect"});

    private float[] magnitudeInterp = null;

    @Override
    public void render() {
        // 使用本地变量而不是直接修改 renderX 和 renderY
        float renderPosX = renderX;
        float renderPosY = renderY;

        // 更新宽度和高度
        width = fillValue.getValue() ? sr.getScaledWidth() : widthValue.getValue().floatValue();
        height = heightValue.getValue().floatValue();
        float step = width / bands.getValue().intValue();

        MediaPlayer mediaPlayer = Client.instance.musicManager.screen.player.getMediaPlayer();
        if (mediaPlayer != null && Client.instance.musicManager.screen.player.getMagnitudes() != null) {
            mediaPlayer.setAudioSpectrumNumBands(bands.getValue().intValue());
            float[] magnitudes = Client.instance.musicManager.screen.player.getMagnitudes();

            float[] vertex = new float[magnitudeInterp == null ? magnitudes.length * 2 : magnitudeInterp.length * 2 + 2];

            if (magnitudeInterp == null) {
                magnitudeInterp = magnitudes;
            }

            for (int i = 0; i < magnitudeInterp.length && i < magnitudes.length; i++) {
                magnitudeInterp[i] = (float) MathUtil.lerp(magnitudeInterp[i], magnitudes[i], RenderUtil.frameTime * 0.5);
            }

            NanoUtil.scissorStart(renderPosX, renderPosY, width, height);

            vertex[0] = renderPosX;
            vertex[1] = renderPosY + height;
            renderPosX += step;

            int vertexIndex = 2;
            int colorIndex = 0;

            for (float magnitude : magnitudeInterp) {
                if (vertexIndex >= vertex.length - 1) break;
                float realY = renderPosY + heightValue.getValue().floatValue() - (1 - (-magnitude / 60f)) * 1.2f * heightValue.getValue().floatValue();
                vertex[vertexIndex] = renderPosX;
                vertex[vertexIndex + 1] = realY;

                if (modeValue.getValue().equals("Rect") && realY != renderPosY + height) {
                    if (rectRadius.getValue() == 0d) {
                        NanoUtil.drawRect(renderPosX - step, realY, step, renderPosY + height - realY, colorValue.getValue());
                    } else {
                        NanoUtil.drawRoundedRect(renderPosX - step, realY, step, renderPosY + height - realY, rectRadius.getValue().floatValue(), rectRadius.getValue().floatValue(), 0, 0, colorValue.getValue(colorIndex));
                    }
                    colorIndex += indexOffset.getValue().intValue();
                }

                vertexIndex += 2;
                renderPosX += modeValue.getValue().equals("Rect") ? step + rectSpace.getValue().floatValue() : step;
            }

            if (modeValue.getValue().equals("Polyline")) {
                NanoUtil.drawPolyline(vertex, colorValue.getValue().getRGB(), 0.8f);
            }

            NanoUtil.scissorEnd();
        }
    }

}
