package cn.feng.untitled.util.data;

/**
 * @author ChengFeng, LangYa
 * @since 2024/7/30
 **/
public class CharUtil {
    private static final String englishChar = " !\\\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    private static char[] chineseCharacters;

    public static char[] getChineseCharacters() {
        int start = 0x4E00;
        int end = 0x9FFF;
        if (chineseCharacters == null) {
            chineseCharacters = new char[end - start + 1];
            for (int i = 0; i < chineseCharacters.length; i++) {
                chineseCharacters[i] = (char) (start + i);
            }
        }
        return chineseCharacters;
    }

    public static char[] getEnglishCharArray() {
        return englishChar.toCharArray();
    }
}
