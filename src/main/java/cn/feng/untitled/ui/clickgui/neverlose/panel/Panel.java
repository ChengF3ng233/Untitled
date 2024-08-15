package cn.feng.untitled.ui.clickgui.neverlose.panel;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public abstract class Panel {
    public float x, y, width, height;
    public void init() {

    }
    public abstract void draw(float x, float y, int mouseX, int mouseY);
    public void onMouseClick(int mouseX, int mouseY, int button) {

    }
    public void onMouseRelease() {

    }
    public void onKeyTyped(char c, int keyCode) {

    }
}
