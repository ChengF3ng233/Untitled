package cn.feng.untitled.ui.clickgui.window.component.impl;

import cn.feng.untitled.module.Module;
import cn.feng.untitled.ui.clickgui.window.ThemeColor;
import cn.feng.untitled.ui.clickgui.window.component.Component;
import cn.feng.untitled.util.animation.composed.ColorAnimation;
import cn.feng.untitled.util.animation.composed.CustomAnimation;
import cn.feng.untitled.util.animation.impl.SmoothStepAnimation;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import cn.feng.untitled.value.impl.BoolValue;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/7/31
 **/
public class ButtonComponent extends Component<Boolean> {
    public ButtonComponent(BoolValue value) {
        super(value);
        moduleMode = false;
    }

    public ButtonComponent(Module module) {
        super(null);
        this.module = module;
        moduleMode = true;
    }

    private Module module;
    private boolean moduleMode;

    public ColorAnimation textColAnim;
    public ColorAnimation bgColAnim;
    public ColorAnimation circColAnim;
    public CustomAnimation circXAnim;

    @Override
    public void init() {
        textColAnim = new ColorAnimation(isEnabled()? ThemeColor.grayColor : Color.WHITE, isEnabled()? Color.WHITE : ThemeColor.grayColor, 200);
        bgColAnim = new ColorAnimation(isEnabled()? Color.BLACK : ThemeColor.barBgColor, isEnabled()? ThemeColor.barBgColor : Color.BLACK, 200);
        circColAnim = new ColorAnimation(isEnabled()? ThemeColor.grayColor : ThemeColor.focusedColor, isEnabled()? ThemeColor.focusedColor : ThemeColor.grayColor, 200);
        circXAnim = new CustomAnimation(SmoothStepAnimation.class, 200, 0f, 10f);
        width = 14f;
        height = 12f;
        if (!isEnabled()) circXAnim.changeDirection();
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY) {
        float bgWidth = 10f;
        float bgHeight = 5f;
        float circRadius = 6f;
        float radius = 4f;
        this.x = x + width - 10f - bgWidth;
        this.y = y;
        RoundedUtil.drawRound(this.x, this.y, bgWidth, bgHeight, radius, bgColAnim.getOutput());
        RoundedUtil.drawRound(this.x - circRadius + circXAnim.getOutput().floatValue(), this.y - circRadius, circRadius, circRadius, radius, circColAnim.getOutput());
    }

    private void toggle() {
        if (moduleMode) {
            module.toggle();
        } else value.value = !value.value;
        textColAnim.changeDirection();
        bgColAnim.changeDirection();
        circXAnim.changeDirection();
        circColAnim.changeDirection();
    }

    private boolean isEnabled() {
        return moduleMode? module.enabled : value.value;
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (RenderUtil.hovering(mouseX, mouseY, x, y, width, height) && button == 0) {
            toggle();
        }
    }
}
