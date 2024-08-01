package cn.feng.untitled.util.data.compare;

import cn.feng.untitled.ui.font.Font;

import java.util.Comparator;

/**
 * @author ChengFeng
 * @since 2024/8/1
 **/
public class StringComparator implements Comparator<String> {
    private final Font font;

    public StringComparator(Font font) {
        this.font = font;
    }

    @Override
    public int compare(String o1, String o2) {
        return Integer.compare(font.getStringWidth(o1), font.getStringWidth(o2));
    }
}
