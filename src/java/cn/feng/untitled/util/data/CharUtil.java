package cn.feng.untitled.util.data;

/**
 * @author ChengFeng
 * @since 2024/7/30
 **/
public class CharUtil {
    private static final StringBuilder chineseChar = new StringBuilder("、：,。!?《》【】");
    private static final String englishChar = " !\\\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{|}~";

    static {
        for (int i = '一'; i < '\u9FA5'; i++) {
            chineseChar.append((char) i);
        }
    }

    public static char[] getChineseCharArray() {
        return chineseChar.toString().toCharArray();
    }

    public static char[] getEnglishCharArray() {
        return englishChar.toCharArray();
    }
}
