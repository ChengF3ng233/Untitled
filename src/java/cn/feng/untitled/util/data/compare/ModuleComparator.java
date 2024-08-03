package cn.feng.untitled.util.data.compare;

import cn.feng.untitled.module.Module;
import cn.feng.untitled.ui.font.awt.Font;

import java.util.Comparator;

/**
 * @author ChengFeng
 * @since 2024/7/31
 **/
public class ModuleComparator implements Comparator<Module> {
    private final CompareMode mode;
    private final Font font;

    public ModuleComparator(CompareMode mode, Font font) {
        this.mode = mode;
        this.font = font;
    }

    @Override
    public int compare(Module o1, Module o2) {
        if (mode == CompareMode.Alphabet) {
            return o1.name.compareTo(o2.name);
        } else {
            return Integer.compare(font.getStringWidth(o2.name), font.getStringWidth(o1.name));
        }
    }
}
