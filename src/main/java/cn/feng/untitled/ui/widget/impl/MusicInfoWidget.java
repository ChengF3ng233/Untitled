package cn.feng.untitled.ui.widget.impl;

import cn.feng.untitled.Client;
import cn.feng.untitled.event.impl.ShaderEvent;
import cn.feng.untitled.music.api.player.MusicPlayer;
import cn.feng.untitled.music.ui.ThemeColor;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.widget.Widget;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.value.impl.BoolValue;
import cn.feng.untitled.value.impl.NumberValue;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.DynamicTexture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * @author ChengFeng
 * @since 2024/8/16
 **/
public class MusicInfoWidget extends Widget {
    private DynamicTexture coverTexture;
    public MusicInfoWidget() {
        super("MusicInfo", true);
        x = 0;
        y = 1;
    }

    private final BoolValue imgBloom = new BoolValue("ImageBloom", true);
    private final BoolValue bgBlur = new BoolValue("BackgroundBlur", true);
    private final NumberValue heightValue = new NumberValue("Height", 50, 100, 50, 0.1);
    private final NumberValue fontSize = new NumberValue("FontSize", 20, 30, 15, 0.1);
    private final BoolValue fontShadow = new BoolValue("FontShadow", true);


    @Override
    public void onRender2D() {
        MusicPlayer player = Client.instance.musicManager.screen.player;
        if (player.getMusic() == null) return;
        if (coverTexture == null) {
            try {
                coverTexture = new DynamicTexture(ImageIO.read(player.getMusic().getCoverFile()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        height = heightValue.getValue().floatValue();
        RenderUtil.drawImage(coverTexture, x * sr.getScaledWidth(), y * sr.getScaledHeight(), height, height);
    }

    public void reset() {
        coverTexture = null;
    }

    @Override
    public void onNano() {
        MusicPlayer player = Client.instance.musicManager.screen.player;
        if (player.getMusic() == null) return;
        width = height + Math.max(
                NanoFontLoader.misans.getStringWidth(player.getMusic().getName(), fontSize.getValue().floatValue()),
                NanoFontLoader.misans.getStringWidth(player.getMusic().getArtist(), fontSize.getValue().floatValue() * 0.8f)
        ) + 4f;
        NanoFontLoader.misans.drawString(player.getMusic().getName(), x * sr.getScaledWidth() + 2f + height, y * sr.getScaledHeight() + 2f, fontSize.getValue().floatValue(), Color.WHITE, fontShadow.getValue());
        NanoFontLoader.misans.drawString(player.getMusic().getArtist(), x * sr.getScaledWidth() + 2f + height, y * sr.getScaledHeight() + fontSize.getValue().floatValue() / 2f + 6f, fontSize.getValue().floatValue() * 0.8f, ThemeColor.greyColor, fontShadow.getValue());
    }

    @Override
    public void onShader(ShaderEvent event) {
        MusicPlayer player = Client.instance.musicManager.screen.player;
        if (player.getMusic() == null) return;
        if (imgBloom.getValue() && event.bloom) {
            Gui.drawNewRect(x * sr.getScaledWidth(), y * sr.getScaledHeight(), height, height, Color.BLACK.getRGB());
        }
        if (bgBlur.getValue() && !event.bloom) {
            Gui.drawNewRect(x * sr.getScaledWidth(), y * sr.getScaledHeight(), width, height, Color.BLACK.getRGB());
        }
    }
}
