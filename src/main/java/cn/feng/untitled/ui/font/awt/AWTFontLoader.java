package cn.feng.untitled.ui.font.awt;

import cn.feng.untitled.util.misc.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ChengFeng
 * @since 2024/7/29
 **/
public class AWTFontLoader {
    private static final Map<Integer, AWTAWTFontRenderer> greyCliff = new HashMap<>();
    private static final Map<Integer, AWTAWTFontRenderer> greyCliff_bold = new HashMap<>();
    private static final Map<Integer, AWTAWTFontRenderer> rubik = new HashMap<>();
    private static final Map<Integer, AWTAWTFontRenderer> rubik_bold = new HashMap<>();
    private static final Map<Integer, AWTAWTFontRenderer> miSans = new HashMap<>();
    private static final Map<Integer, AWTAWTFontRenderer> icon = new HashMap<>();

    //These are for the icon font for ease of access
    public final static String
            BUG = "a",
            LIST = "b",
            BOMB = "c",
            EYE = "d",
            PERSON = "e",
            WHEELCHAIR = "f",
            SCRIPT = "g",
            SKIP_LEFT = "h",
            PAUSE = "i",
            PLAY = "j",
            SKIP_RIGHT = "k",
            SHUFFLE = "l",
            INFO = "m",
            SETTINGS = "n",
            CHECKMARK = "o",
            XMARK = "p",
            TRASH = "q",
            WARNING = "r",
            FOLDER = "s",
            LOAD = "t",
            SAVE = "u",
            UPVOTE_OUTLINE = "v",
            UPVOTE = "w",
            DOWNVOTE_OUTLINE = "x",
            DOWNVOTE = "y",
            DROPDOWN_ARROW = "z",
            PIN = "s",
            EDIT = "A",
            SEARCH = "B",
            UPLOAD = "C",
            REFRESH = "D",
            ADD_FILE = "E",
            STAR_OUTLINE = "F",
            STAR = "G";

    /**
     * Register international font MiSans
     */
    public static void registerFonts() {
        miSans(15);
        miSans(17);
        greyCliff(14);
        greyCliff(15);
        greyCliff(16);
        greyCliff(17);
        greyCliff(18);
        rubik(13);
        rubik(15);
        rubik(16);
        rubik(17);
        rubik(18);
        rubik_bold(28);
    }

    public static AWTAWTFontRenderer icon(int size) {
        return get(icon, size, "icon", false);
    }

    public static AWTAWTFontRenderer greyCliff(int size) {
        return get(greyCliff, size, "greycliff", false);
    }

    public static AWTAWTFontRenderer greyCliff_bold(int size) {
        return get(greyCliff_bold, size, "greycliff-bold", false);
    }

    public static AWTAWTFontRenderer rubik(int size) {
        return get(rubik, size, "rubik", false);
    }

    public static AWTAWTFontRenderer rubik_bold(int size) {
        return get(rubik_bold, size, "rubik-bold", false);
    }

    public static AWTAWTFontRenderer miSans(int size) {
        return get(miSans, size, "misans", true);
    }

    private static AWTAWTFontRenderer get(Map<Integer, AWTAWTFontRenderer> map, int size, String name, boolean chinese) {
        if (!map.containsKey(size)) {
            Logger.info("Registering font " + name + (chinese ? " including Chinese." : ".") + " Size: " + size);
            java.awt.Font font = FontUtil.getResource("untitled/font/" + name + ".ttf", size);
            if (font != null) {
                map.put(size, new AWTAWTFontRenderer(font, chinese));
            }
        }

        return map.get(size);
    }
}
