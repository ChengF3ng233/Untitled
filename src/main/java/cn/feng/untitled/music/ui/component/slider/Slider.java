package cn.feng.untitled.music.ui.component.slider;

/**
 * @author ChengFeng
 * @since 2024/8/31
 **/
public abstract class Slider {
    protected boolean dragging, hovering, cursorRestored = false;
    protected float dragDelta;

    public abstract void render(float x, float y, int mouseX, int mouseY);

    public void mouseClicked(int mouseButton) {
        if (hovering && mouseButton == 0 && !dragging) {
            dragging = true;
        }
    }

    public void mouseReleased() {
        if (dragging) {
            dragging = false;
        }
    }
}
