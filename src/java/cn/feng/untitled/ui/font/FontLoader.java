package cn.feng.untitled.ui.font;

import cn.feng.untitled.util.misc.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ChengFeng
 * @since 2024/7/29
 **/
public class FontLoader {
    private static final Map<Integer, FontRenderer> greyCliff = new HashMap<>();
    private static final Map<Integer, FontRenderer> rubik = new HashMap<>();
    public static Map<Integer, FontRenderer> miSans = new HashMap<>();

    /**
     * Register international font MiSans
     */
    public static void registerFonts() {
        miSans(18);
    }

    public static FontRenderer greyCliff(int size) {
        return get(greyCliff, size, "greycliff", false);
    }

    public static FontRenderer rubik(int size) {
        return get(rubik, size, "rubik", false);
    }

    public static FontRenderer miSans(int size) {
        return get(miSans, size, "misans", true);
    }

    private static FontRenderer get(Map<Integer, FontRenderer> map, int size, String name, boolean chinese) {
        if (!map.containsKey(size)) {
            Logger.info("Registering font: " + name + (chinese ? " including Chinese." : "."));
            java.awt.Font font = FontUtil.getResource("untitled/font/" + name + ".ttf", size);
            if (font != null) {
                map.put(size, new FontRenderer(font, chinese));
            }
        }

        return map.get(size);
    }
}
