package cn.feng.untitled.util.data.compare;

import cn.feng.untitled.module.Module;

import java.util.Comparator;

/**
 * @author ChengFeng
 * @since 2024/7/31
 **/
public class ModuleComparator implements Comparator<Module> {
    private final CompareMode mode;

    public ModuleComparator(CompareMode mode) {
        this.mode = mode;
    }

    @Override
    public int compare(Module o1, Module o2) {
        if (mode == CompareMode.Alphabet) {
            return o1.name.compareTo(o2.name);
        } else {
            return Integer.compare(o1.name.length(), o2.name.length());
        }
    }
}
