package cn.feng.untitled.ui.widget.impl;

import cn.feng.untitled.Client;
import cn.feng.untitled.event.impl.ShaderEvent;
import cn.feng.untitled.music.api.player.MusicPlayer;
import cn.feng.untitled.music.ui.ThemeColor;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoFontRenderer;
import cn.feng.untitled.util.render.ColorUtil;
import cn.feng.untitled.util.render.nano.NanoUtil;
import cn.feng.untitled.ui.widget.Widget;
import cn.feng.untitled.value.impl.BoolValue;
import cn.feng.untitled.value.impl.NumberValue;
import net.minecraft.client.gui.Gui;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/8/16
 **/
public class MusicInfoWidget extends Widget {
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
    public void render() {
        MusicPlayer player = Client.instance.musicManager.screen.player;
        if (player.getMusic() == null) return;
        NanoFontRenderer fontRenderer = NanoFontLoader.misans.bold();
        width = height + Math.max(
                fontRenderer.getStringWidth(player.getMusic().getName(), fontSize.getValue().floatValue()),
                NanoFontLoader.misans.getStringWidth(player.getMusic().getArtist(), fontSize.getValue().floatValue() * 0.8f)
        ) + 4f;
        fontRenderer.drawString(player.getMusic().getName(), renderX + 2f + height, renderY + 2f, fontSize.getValue().floatValue(), Color.WHITE, fontShadow.getValue());
        NanoFontLoader.misans.drawString(player.getMusic().getArtist(), renderX + 2f + height, renderY + fontSize.getValue().floatValue() / 2f + 6f, fontSize.getValue().floatValue() * 0.8f, ThemeColor.greyColor, fontShadow.getValue());

        if (player.getMusic().getCoverTexture() == 0) {
            player.getMusic().setCoverTexture(NanoUtil.genImageId(player.getMusic().getCoverFile()));
        }
        height = heightValue.getValue().floatValue();
        NanoUtil.drawImageRect(player.getMusic().getCoverTexture(), renderX, renderY, height, height);
        NanoUtil.drawRoundedRect(renderX + 2f + height, renderY + height - 5f, width -  height - 2f, 2f, 1f, ColorUtil.applyOpacity(ThemeColor.greyColor, 0.4f));
        if (player.getMediaPlayer() == null) return;
        NanoUtil.drawRoundedRect(renderX + 2f + height, renderY + height - 5f, (float) ((width -  height - 2f) * (player.getCurrentTime() / player.getMusic().getDuration())), 2f, 1f, Color.WHITE);
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
