package cn.feng.untitled.ui.clickgui.window.component.impl;

import cn.feng.untitled.ui.clickgui.window.component.Component;
import cn.feng.untitled.ui.clickgui.window.gui.Icon;
import cn.feng.untitled.ui.font.Font;
import cn.feng.untitled.ui.font.FontLoader;
import cn.feng.untitled.util.data.compare.StringComparator;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.value.impl.ModeValue;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/8/1
 **/
public class ModeComponent extends Component<String> {
    private final Font font = FontLoader.greyCliff(16);
    private Icon left, right;

    public ModeComponent(ModeValue value) {
        super(value);
        List<String> modes = new ArrayList<>(List.of(value.values));
        modes.sort(new StringComparator(font));
        width = font.getStringWidth(modes.get(modes.size() - 1));
        height = font.height() + 4f;
        left = new Icon(new ResourceLocation("untitled/icon/arrow-left.png"), 12f);
        right = new Icon(new ResourceLocation("untitled/icon/arrow-right.png"), 12f);
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY) {
        this.x = x + panelWidth - 2 * xGap - width - 12f;
        this.y = y - 3f;

        left.draw(this.x - 9f, this.y, mouseX, mouseY);
        right.draw(this.x + width + 3f, this.y, mouseX, mouseY);
        font.drawString(value.value, this.x + 3f, this.y + 3.5f, Color.WHITE.getRGB());
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0) {
            if (RenderUtil.hovering(mouseX, mouseY, this.x - 9f, this.y, 12f, 12f)) {
                ((ModeValue) value).previous();
            } else if (RenderUtil.hovering(mouseX, mouseY, this.x + width + 2f, this.y, 12f, 12f)) {
                ((ModeValue) value).next();
            }
        }
    }
}
