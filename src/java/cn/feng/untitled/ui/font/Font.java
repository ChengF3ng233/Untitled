package cn.feng.untitled.ui.font;

/**
 * @author ChengFeng
 * @since 2024/7/29
 **/
public abstract class Font {
    public abstract int drawString(String text, double x, double y, int color, boolean shadow);

    public abstract int drawString(String text, double x, double y, int color);

    public abstract int drawStringWithShadow(String text, double x, double y, int color);

    public abstract int getStringWidth(String text);

    public abstract String trimStringToWidth(String text, int width, boolean reverse);

    public abstract String trimStringToWidth(String text, int width);

    public abstract int drawCenteredString(String text, double x, double y, int color);

    public abstract float height();

    public abstract void drawCenteredStringWithShadow(String text, float x, float y, int color);
}

