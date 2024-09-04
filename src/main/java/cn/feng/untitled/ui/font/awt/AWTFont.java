package cn.feng.untitled.ui.font.awt;

/**
 * @author ChengFeng
 * @since 2024/7/29
 **/
public abstract class AWTFont {
    public abstract int drawString(String text, double x, double y, int color, boolean shadow);

    public abstract int drawString(String text, double x, double y, int color);
    public abstract int drawCenteredString(String text, double x, double y, int color, CenterType type);

    public abstract int drawCenteredString(String text, double x, double y, int color, CenterType type, boolean shadow);

    public abstract int getStringWidth(String text);

    public abstract String trimStringToWidth(String text, float width, boolean reverse);

    public abstract String trimStringToWidth(String text, float width);

    public abstract float getFontHeight();
}

