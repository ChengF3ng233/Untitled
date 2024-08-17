package cn.feng.untitled.ui.widget.impl;

import cn.feng.untitled.Client;
import cn.feng.untitled.music.ui.ThemeColor;
import cn.feng.untitled.ui.font.nano.NanoUtil;
import cn.feng.untitled.ui.widget.Widget;
import cn.feng.untitled.util.data.MathUtil;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import cn.feng.untitled.value.impl.BoolValue;
import cn.feng.untitled.value.impl.ColorValue;
import cn.feng.untitled.value.impl.ModeValue;
import cn.feng.untitled.value.impl.NumberValue;
import javafx.scene.media.MediaPlayer;
import org.lwjgl.opengl.GL11;

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
    private final NumberValue rectSpace = new NumberValue("RectSpace", 2f, 5f, 1f, 0.01f);
    private final NumberValue rectRadius = new NumberValue("RectRadius", 3f, 5f, 0f, 0.1f);

    private final ColorValue rectFirst = new ColorValue("RectFirstColor", ThemeColor.redColor);
    private final ColorValue rectSecond = new ColorValue("RectSecondColor", ThemeColor.bgColor);
    private final ColorValue lineColor = new ColorValue("LineColor", Color.WHITE);
    private final NumberValue indexOffset = new NumberValue("IndexOffset", 6f, 20f, 0f, 1f);

    private final ModeValue modeValue = new ModeValue("Mode", "Polyline", new String[]{"Polyline", "Rect"});

    private float[] magnitudeInterp = null;

    @Override
    public void onRender2D() {
        GL11.glPushMatrix();
        width = fillValue.getValue() ? sr.getScaledWidth() : widthValue.getValue().floatValue();
        height = heightValue.getValue().floatValue();
        float renderX = sr.getScaledWidth() * x;
        float renderY = sr.getScaledHeight() * y;
        float step = width / bands.getValue().intValue();
        MediaPlayer mediaPlayer = Client.instance.musicManager.screen.player.getMediaPlayer();
        if (mediaPlayer != null && Client.instance.musicManager.screen.player.getMagnitudes() != null) {
            mediaPlayer.setAudioSpectrumNumBands(bands.getValue().intValue());
            float[] magnitudes = Client.instance.musicManager.screen.player.getMagnitudes();
            float[] vertex = new float[magnitudeInterp == null? magnitudes.length * 2 : magnitudeInterp.length * 2 + 2];

            if (magnitudeInterp == null) {
                magnitudeInterp = magnitudes;
            }

            for (int i = 0; i < magnitudeInterp.length && i < magnitudes.length; i++) {
                magnitudeInterp[i] = (float) MathUtil.lerp(magnitudeInterp[i], magnitudes[i], RenderUtil.frameTime * 0.5);
            }

            vertex[0] = renderX;
            vertex[1] = renderY + height;
            renderX += step;

            int vertexIndex = 2;
            int colorIndex = 0;
            for (float magnitude : magnitudeInterp) {
                float realY = renderY + heightValue.getValue().floatValue() - (1 - (-magnitude / 60f)) * 1.2f * heightValue.getValue().floatValue();
                vertex[vertexIndex] = renderX;
                vertex[vertexIndex + 1] = realY;

                if (modeValue.getValue().equals("Rect"))
                {
                    RoundedUtil.drawGradientVertical(renderX - step, realY + (renderY + heightValue.getValue().floatValue()) * (sr.getScaleFactor() * 0.5f - 1), step, Math.max(renderY + height - realY, 1), rectRadius.getValue().floatValue(), rectFirst.getValue(colorIndex), rectSecond.getValue(colorIndex));
                    colorIndex += indexOffset.getValue().intValue();
                }

                vertexIndex += 2;
                renderX += modeValue.getValue().equals("Rect")? step + rectSpace.getValue().floatValue() : step;
            }

            if (modeValue.getValue().equals("Polyline")) {
                NanoUtil.drawPolyline(vertex, lineColor.getValue().getRGB(), 0.8f);
            }
        }
        GL11.glPopMatrix();
    }
}
