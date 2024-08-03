package cn.feng.untitled.ui.font.nano;

import cn.feng.untitled.module.impl.client.PostProcessing;
import cn.feng.untitled.ui.font.awt.FontLoader;
import cn.feng.untitled.util.MinecraftInstance;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * @author ChengFeng
 * @since 2024/8/3
 **/
public class NanoFontRenderer extends MinecraftInstance {
    private final int font;
    private final String name;

    public NanoFontRenderer(String name, ResourceLocation resource) {
        this.name = name;
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
        if (containsEmoji(text) && !name.equals("Emoji")) {
            NanoFontLoader.emoji.drawString(text, x, y, size, align, color);
            return;
        }
        nvgBeginFrame(NanoLoader.vg, mc.displayWidth, mc.displayHeight, 1f);
        nvgBeginPath(NanoLoader.vg);
        nvgFontFaceId(NanoLoader.vg, font);
        nvgFontSize(NanoLoader.vg, size);
        NVGColor nvgColor = NVGColor.create();
        nvgRGBAf(color.getRed() / 255f, color.getRed() / 255f, color.getRed() / 255f, color.getRed() / 255f, nvgColor);
        nvgFillColor(NanoLoader.vg, nvgColor);
        nvgTextAlign(NanoLoader.vg, align);
        nvgText(NanoLoader.vg, x * 2f, y * 2f + size, text);
        nvgEndFrame(NanoLoader.vg);
    }

    private boolean containsEmoji(String text) {
        // Emoji 正则表达式
        String emojiPattern = "[\\uD83C-\\uDBFF\\uDC00-\\uDFFF\\u2700-\\u27BF\\uFE00-\\uFE0F\\u1F600-\\u1F64F\\u1F300-\\u1F5FF\\u1F1E6-\\u1F1FF\\u1F900-\\u1F9FF\\uD83C\\uDC00-\\uD83C\\uDC2F]";

        Pattern pattern = Pattern.compile(emojiPattern);
        Matcher matcher = pattern.matcher(text);

        return matcher.find();
    }
}
