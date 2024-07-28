package cn.feng.untitled.util.misc;

import cn.feng.untitled.util.exception.MemberNotFoundException;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class StringUtil {
    public static boolean arrayContains(String[] array, String target, boolean ignoreCase) {
        for (String s : array) {
            if (s.equals(target) || (ignoreCase && s.equalsIgnoreCase(target))) return true;
        }

        return false;
    }

    public static String[] subArray(String[] array, int startIndex) {
        String[] newArray = new String[array.length - startIndex];
        for (int i = startIndex; i < array.length; i++) {
            newArray[i - startIndex] = array[startIndex];
        }
        return newArray;
    }

    public static int arrayIndex(String[] array, String target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(target)) return i;
        }

        throw new MemberNotFoundException("ARRAY: " + target);
    }
}
