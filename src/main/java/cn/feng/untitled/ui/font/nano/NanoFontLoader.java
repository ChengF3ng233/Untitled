package cn.feng.untitled.ui.font.nano;

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
    public static NanoFontRenderer greyCliff;
    public static NanoFontRenderer monster;

    public static void registerFonts() {
        misans = new NanoFontRenderer("MiSans", "misans");
        emoji = new NanoFontRenderer("Emoji", "emoji");
        script = new NanoFontRenderer("Script", "script");
        rubik = new NanoFontRenderer("Rubik", "rubik");
        greyCliff = new NanoFontRenderer("Greycliff", "greycliff");
        monster = new NanoFontRenderer("Monster", "monster");

        renderers.add(misans);
        renderers.add(emoji);
        renderers.add(script);
        renderers.add(rubik);
        renderers.add(greyCliff);
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
