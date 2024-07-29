package cn.feng.untitled.ui.font;

import cn.feng.untitled.util.misc.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ChengFeng
 * @since 2024/7/29
 **/
public class FontLoader {
    private static final Map<Integer, Font> greyCliff = new HashMap<>();
    private static final Map<Integer, Font> rubik = new HashMap<>();
    private static final Map<Integer, Font> miSans = new HashMap<>();

    /**
     * Register international font MiSans
     */
    public static void registerFonts() {
        miSans(18);
    }

    public static Font greyCliff(int size) {
        return get(greyCliff, size, "greycliff", false);
    }

    public static Font rubik(int size) {
        return get(rubik, size, "rubik", false);
    }

    public static Font miSans(int size) {
        return get(miSans, size, "misans", true);
    }

    private static Font get(Map<Integer, Font> map, int size, String name, boolean international) {
        if (!map.containsKey(size)) {
            Logger.info("Registering font: " + name + "." + (international? " This may take much time because the number of the characters is large." : ""));
            java.awt.Font font = FontUtil.getResource("untitled/font/" + name + ".ttf", size);
            if (font != null) {
                map.put(size, new FontRenderer(font, true, true, international));
            }
        }

        return map.get(size);
    }
}
