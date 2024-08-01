package cn.feng.untitled.ui.clickgui.window.component;

import cn.feng.untitled.ui.clickgui.window.NeverLoseGUI;
import cn.feng.untitled.value.Value;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public abstract class Component<T> {
    public float x, y, width, height;
    public Value<T> value;

    protected float panelWidth = (NeverLoseGUI.width - NeverLoseGUI.leftWidth - 30f) / 2f;
    protected float xGap = 3f;

    public Component(Value<T> value) {
        this.value = value;
    }
    public void init() {

    }
    public abstract void draw(float x, float y, int mouseX, int mouseY);
    public void onMouseClick(int mouseX, int mouseY, int button) {

    }
}
