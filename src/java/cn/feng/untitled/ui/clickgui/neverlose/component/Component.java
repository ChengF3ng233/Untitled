package cn.feng.untitled.ui.clickgui.neverlose.component;

import cn.feng.untitled.ui.clickgui.neverlose.NeverLoseGUI;
import cn.feng.untitled.value.Value;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public abstract class Component<T> {
    public float posX, posY, width, height;
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
    public void onMouseRelease() {

    }
    public void onKeyTyped(char c, int keyCode) {

    }
}
