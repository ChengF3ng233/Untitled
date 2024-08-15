package cn.feng.untitled.ui.font.nano;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/8/3
 **/
public class NanoFontLoader {
    public static List<NanoFontRenderer> renderers = new ArrayList<>();
    public static NanoFontRenderer misans;
    public static NanoFontRenderer emoji;
    public static NanoFontRenderer script;
    public static NanoFontRenderer rubik;
    public static NanoFontRenderer rubikBold;
    public static NanoFontRenderer greyCliff;
    public static NanoFontRenderer greyCliffBold;
    public static NanoFontRenderer monster;

    public static void registerFonts() {
        misans = new NanoFontRenderer("MiSans", new ResourceLocation("untitled/font/misans.ttf"));
        emoji = new NanoFontRenderer("Emoji", new ResourceLocation("untitled/font/emoji.ttf"));
        script = new NanoFontRenderer("Script", new ResourceLocation("untitled/font/script.ttf"));
        rubik = new NanoFontRenderer("Rubik", new ResourceLocation("untitled/font/rubik.ttf"));
        rubikBold = new NanoFontRenderer("RubikBold", new ResourceLocation("untitled/font/rubik-bold.ttf"));
        greyCliff = new NanoFontRenderer("Greycliff", new ResourceLocation("untitled/font/greycliff.ttf"));
        greyCliffBold = new NanoFontRenderer("GreycliffBold", new ResourceLocation("untitled/font/greycliff-bold.ttf"));
        monster = new NanoFontRenderer("Monster", new ResourceLocation("untitled/font/monster.ttf"));

        renderers.add(misans);
        renderers.add(emoji);
        renderers.add(script);
        renderers.add(rubik);
        renderers.add(rubikBold);
        renderers.add(greyCliff);
        renderers.add(greyCliffBold);
        renderers.add(monster);
    }

    public static String[] getRenderers() {
        String[] values = new String[renderers.size()];
        for (int i = 0; i < renderers.size(); i++) {
            NanoFontRenderer fontRenderer = renderers.get(i);
            values[i] = fontRenderer.getName();
        }
        return values;
    }
}
