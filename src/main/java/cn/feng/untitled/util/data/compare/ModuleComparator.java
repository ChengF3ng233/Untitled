package cn.feng.untitled.util.data.compare;

import cn.feng.untitled.module.Module;
import cn.feng.untitled.ui.font.awt.AWTFont;
import cn.feng.untitled.ui.font.nano.NanoFontRenderer;

import java.util.Comparator;

/**
 * @author ChengFeng
 * @since 2024/7/31
 **/
public class ModuleComparator implements Comparator<Module> {
    private final CompareMode mode;
    private final AWTFont AWTFont;
    private final NanoFontRenderer nanoFont;

    public ModuleComparator(CompareMode mode, AWTFont AWTFont) {
        this.mode = mode;
        this.AWTFont = AWTFont;
        this.nanoFont = null;
    }

    public ModuleComparator(CompareMode mode, NanoFontRenderer font) {
        this.mode = mode;
        this.AWTFont = null;
        this.nanoFont = font;
    }

    @Override
    public int compare(Module o1, Module o2) {
        if (mode == CompareMode.Alphabet) {
            return o1.name.compareTo(o2.name);
        } else {
            if (nanoFont != null) {
                return Float.compare(nanoFont.getStringWidth(o2.name), nanoFont.getStringWidth(o1.name));
            } else if (AWTFont != null) {
                return Integer.compare(AWTFont.getStringWidth(o2.name), AWTFont.getStringWidth(o1.name));
            }

            return 0;
        }
    }
}
