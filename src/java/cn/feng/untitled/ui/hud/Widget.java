package cn.feng.untitled.ui.hud;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public abstract class Widget {
    public String name;
    public float x, y, width, height;
    public final boolean defaultOn;

    public Widget(String name, boolean defaultOn) {
        this.name = name;
        this.defaultOn = defaultOn;
        this.x = 0f;
        this.y = 0f;
        this.width = 100f;
        this.height = 100f;
    }

    public abstract void render();
}
