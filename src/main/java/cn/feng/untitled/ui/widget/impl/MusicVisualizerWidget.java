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

    private final ColorValue rectFirst = new ColorValue("RectFirstColor", ThemeColor.redColor);
    private final ColorValue rectSecond = new ColorValue("RectSecondColor", ThemeColor.bgColor);
    private final ColorValue lineColor = new ColorValue("LineColor", Color.WHITE);
    private final NumberValue rectSpace = new NumberValue("RectSpace", 3f, 5f, 1f, 1f);
    private final NumberValue indexOffset = new NumberValue("IndexOffset", 6f, 20f, 0f, 1f);

    private final ModeValue modeValue = new ModeValue("Mode", "Polyline", new String[]{"Polyline", "Rect"});

    private float[] magnitudeInterp = null;

    @Override
    public void onRender() {
        GL11.glPushMatrix();
        GL11.glScalef(2f, 2f, 1f);
        width = fillValue.value? sr.getScaledWidth() : widthValue.value.floatValue();
        height = heightValue.value.floatValue();
        float renderX = sr.getScaledWidth() * x;
        float renderY = sr.getScaledHeight() * y;
        float step = width / bands.value.intValue();
        MediaPlayer mediaPlayer = Client.instance.musicManager.screen.player.getMediaPlayer();
        if (mediaPlayer != null && Client.instance.musicManager.screen.player.getMagnitudes() != null) {
            mediaPlayer.setAudioSpectrumNumBands(bands.value.intValue());
            float[] magnitudes = Client.instance.musicManager.screen.player.getMagnitudes();
            float[] vertex = new float[magnitudes.length * 2 + 2];

            if (magnitudeInterp == null) {
                magnitudeInterp = magnitudes;
            }

            for (int i = 0; i < magnitudeInterp.length; i++) {
                magnitudeInterp[i] = (float) MathUtil.lerp(magnitudeInterp[i], magnitudes[i], RenderUtil.frameTime * 0.5);
            }

            vertex[0] = renderX;
            vertex[1] = renderY + height;
            renderX += step;
            int vertexIndex = 2;
            int colorIndex = 0;
            for (float magnitude : magnitudeInterp) {
                float realY = renderY + heightValue.value.floatValue() - (1 - (-magnitude / 60f)) * 1.2f * heightValue.value.floatValue();
                vertex[vertexIndex] = renderX;
                vertex[vertexIndex + 1] = realY;

                if (modeValue.value.equals("Rect"))
                {
                    RoundedUtil.drawGradientVertical(renderX - step, realY + (renderY + heightValue.value.floatValue()) * (sr.getScaleFactor() * 0.5f - 1), step, Math.max(renderY + height - realY, 1), 2f, rectFirst.getColor(colorIndex), rectSecond.getColor(colorIndex));
                    colorIndex += indexOffset.value.intValue();
                }

                vertexIndex += 2;
                renderX += modeValue.value.equals("Rect")? step + rectSpace.value.floatValue() : step;
            }

            if (modeValue.value.equals("Polyline")) {
                NanoUtil.drawPolyline(vertex, lineColor.getColor().getRGB(), 0.8f);
            }
        }
        GL11.glPopMatrix();
    }
}
