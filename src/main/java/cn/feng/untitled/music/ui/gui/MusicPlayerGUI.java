package cn.feng.untitled.music.ui.gui;

import cn.feng.untitled.music.ui.MusicPlayerScreen;
import cn.feng.untitled.util.MinecraftInstance;
import cn.feng.untitled.util.animation.advanced.composed.CustomAnimation;
import cn.feng.untitled.util.animation.advanced.impl.SmoothStepAnimation;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.input.Mouse;

/**
 * @author ChengFeng
 * @since 2024/8/13
 **/
@Getter
@Setter
public abstract class MusicPlayerGUI extends MinecraftInstance {
    protected float posX, posY, width, height;
    protected boolean hovering, isBottom;
    public MusicPlayerGUI parent;

    public MusicPlayerGUI(MusicPlayerGUI parent) {
        this.parent = parent;
    }

    protected CustomAnimation scrollAnim = new CustomAnimation(SmoothStepAnimation.class, 100);

    public void handleScroll() {
        int wheel = Mouse.getDWheel();
        scrollAnim.setStartPoint(scrollAnim.getOutput());
        if (wheel > 0) {
            scrollAnim.setEndPoint(scrollAnim.getEndPoint() + 20f);
        } else if (wheel < 0) {
            scrollAnim.setEndPoint(scrollAnim.getEndPoint() - 20f);
        }
        if (scrollAnim.getEndPoint() > 0) scrollAnim.setEndPoint(0f);
        float maxScroll = height - (MusicPlayerScreen.height - MusicPlayerScreen.topWidth - MusicPlayerScreen.bottomWidth - 3f);
        if (-scrollAnim.getEndPoint() > maxScroll) {
            scrollAnim.setEndPoint(-maxScroll);
            isBottom = true;
        } else isBottom = false;
        scrollAnim.getAnimation().reset();
    }

    public boolean onNano(float x, float y, int mouseX, int mouseY, float cx, float cy, float scale) {
        return false;
    }

    public boolean onRender2D(float x, float y, int mouseX, int mouseY, float cx, float cy, float scale) {
        return false;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }
}
