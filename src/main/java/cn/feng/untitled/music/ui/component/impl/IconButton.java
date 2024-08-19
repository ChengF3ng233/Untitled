package cn.feng.untitled.music.ui.component.impl;

import cn.feng.untitled.music.ui.ThemeColor;
import cn.feng.untitled.music.ui.component.Button;
import cn.feng.untitled.util.data.resource.ResourceType;
import cn.feng.untitled.util.data.resource.ResourceUtil;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

/**
 * @author ChengFeng
 * @since 2024/8/14
 **/
public class IconButton extends Button {
    protected ResourceLocation icon;
    @Setter
    private Runnable action;
    private final int size;
    @Setter
    private boolean bg;

    public IconButton(String icon, Runnable action, int size) {
        this.icon = ResourceUtil.getResource(icon, ResourceType.ICON);
        this.action = action;
        this.size = size;
        height = size;
        width = size;
    }

    public IconButton(String icon, int size) {
        this.size = size;
        this.icon = ResourceUtil.getResource(icon, ResourceType.ICON);
        height = size;
        width = size;
    }

    @Override
    public void onRender2D() {
        RenderUtil.scaleStart(posX + size / 2f, posY + size / 2f, hovering? (Mouse.isButtonDown(0)? 1.03f : 1.07f) : 1f);
        if (bg) {
            RoundedUtil.drawRound(posX - 2f, posY - 2f, size + 4f, size + 4f, 9.5f, ThemeColor.redColor);
        }
        RenderUtil.drawImage(icon, posX, posY, size, size);
        RenderUtil.scaleEnd();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (hovering && button == 0) {
            action.run();
        }
    }
}
