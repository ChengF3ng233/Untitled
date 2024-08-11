package cn.feng.untitled.ui.font.nano;

/**
 * @author ChengFeng
 * @since 2024/8/4
 **/
public class FontPair {
    public String text;
    public NanoFontRenderer renderer;

    public FontPair(String text, NanoFontRenderer renderer) {
        this.text = text;
        this.renderer = renderer;
    }
}
