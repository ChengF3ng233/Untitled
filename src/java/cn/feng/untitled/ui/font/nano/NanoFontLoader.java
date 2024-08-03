package cn.feng.untitled.ui.font.nano;

import net.minecraft.util.ResourceLocation;

/**
 * @author ChengFeng
 * @since 2024/8/3
 **/
public class NanoFontLoader {
    public static NanoFontRenderer misans;
    public static NanoFontRenderer emoji;

    public static void registerFonts() {
        misans = new NanoFontRenderer("MiSans", new ResourceLocation("untitled/font/misans.ttf"));
        emoji = new NanoFontRenderer("Emoji", new ResourceLocation("untitled/font/emoji.ttf"));
    }
}
