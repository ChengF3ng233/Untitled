package cn.feng.untitled.util.data.compare;

import cn.feng.untitled.ui.font.awt.AWTFont;

import java.util.Comparator;

/**
 * @author ChengFeng
 * @since 2024/8/1
 **/
public class StringComparator implements Comparator<String> {
    private final AWTFont AWTFont;

    public StringComparator(AWTFont AWTFont) {
        this.AWTFont = AWTFont;
    }

    @Override
    public int compare(String o1, String o2) {
        return Integer.compare(AWTFont.getStringWidth(o1), AWTFont.getStringWidth(o2));
    }
}
