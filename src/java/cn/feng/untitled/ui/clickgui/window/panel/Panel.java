package cn.feng.untitled.ui.clickgui.window.panel;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public abstract class Panel {
    public float x, y, width, height;
    public abstract void draw(float x, float y, int mouseX, int mouseY);
}
