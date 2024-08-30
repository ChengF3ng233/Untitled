package cn.feng.untitled.music.ui.component;

import cn.feng.untitled.util.render.RenderUtil;

/**
 * @author ChengFeng
 * @since 2024/8/12
 **/
public abstract class Button {
    protected float posX, posY;
    public float width, height;
    protected String text;
    public boolean hovering;

    public void render() {

    }

    public void updateState(float x, float y, int mouseX, int mouseY) {
        posX = x;
        posY = y;
        hovering = RenderUtil.hovering(mouseX, mouseY, x, y, width, height);
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {

    }
}
