package cn.feng.untitled.value.impl;

import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoFontRenderer;

/**
 * @author ChengFeng
 * @since 2024/8/16
 **/
public class FontValue extends ModeValue {
    public FontValue(String name, NanoFontRenderer renderer) {
        super(name, renderer.getName(), NanoFontLoader.getRenderers());
        this.value = renderer.getName();
    }

    public NanoFontRenderer getValue() {
        for (NanoFontRenderer renderer : NanoFontLoader.renderers) {
            if (renderer.getName().equals(this.value)) return renderer;
        }
        return null;
    }
}
