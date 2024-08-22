package cn.feng.untitled.ui.clickgui;

import cn.feng.untitled.module.ModuleCategory;
import cn.feng.untitled.module.impl.client.PostProcessing;
import cn.feng.untitled.ui.clickgui.gui.TextField;
import cn.feng.untitled.ui.clickgui.panel.Panel;
import cn.feng.untitled.ui.clickgui.panel.impl.CategoryPanel;
import cn.feng.untitled.ui.clickgui.panel.impl.ModulePanel;
import cn.feng.untitled.ui.font.awt.CenterType;
import cn.feng.untitled.ui.font.awt.FontLoader;
import cn.feng.untitled.ui.font.awt.FontRenderer;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoUtil;
import cn.feng.untitled.util.animation.advanced.Animation;
import cn.feng.untitled.util.animation.advanced.Direction;
import cn.feng.untitled.util.animation.advanced.composed.ColorAnimation;
import cn.feng.untitled.util.animation.advanced.composed.CustomAnimation;
import cn.feng.untitled.util.animation.advanced.impl.DecelerateAnimation;
import cn.feng.untitled.util.animation.advanced.impl.SmoothStepAnimation;
import cn.feng.untitled.util.render.ColorUtil;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import cn.feng.untitled.util.render.StencilUtil;
import cn.feng.untitled.util.render.blur.BlurUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class ClickGUI extends GuiScreen {
    public static float width, height, leftWidth, topWidth, radius;
    private float x, y;
    private boolean dragging;
    private float dragX, dragY;

    private Animation windowAnim;
    private final CustomAnimation panelAnim;
    private final Animation topOpacityAnim;
    private final ColorAnimation topColorAnim;
    private ColorAnimation iconColorAnim;

    private final List<CategoryPanel> categoryPanelList;
    private CategoryPanel currentPanel;
    private final TextField searchField;

    public ClickGUI() {
        width = 420f;
        height = 310f;
        leftWidth = 90f;
        topWidth = 35f;
        radius = 4f;
        x = 10f;
        y = 10f;

        categoryPanelList = new ArrayList<>();
        for (ModuleCategory value : ModuleCategory.values()) {
            categoryPanelList.add(new CategoryPanel(value));
        }
        currentPanel = categoryPanelList.get(0);

        float iconSize = 14f;
        searchField = new TextField(60f, 20f, FontLoader.miSans(16), ThemeColor.titleColor, ThemeColor.grayColor);
        searchField.radius = 5f;
        searchField.offsetX = iconSize + 2f;

        panelAnim = new CustomAnimation(SmoothStepAnimation.class, 200, 0, 0);
        topOpacityAnim = new SmoothStepAnimation(100, 0.8d);
        topOpacityAnim.changeDirection();
        topColorAnim = new ColorAnimation(ThemeColor.barColor, ThemeColor.barBgColor, 100);
    }

    @Override
    public void initGui() {
        dragging = false;
        windowAnim = new DecelerateAnimation(150, 1d);
        iconColorAnim = new ColorAnimation(Color.WHITE, ThemeColor.grayColor, 100);

        currentPanel.modulePanelList.forEach(ModulePanel::init);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (windowAnim.finished(Direction.BACKWARDS)) mc.displayGuiScreen(null);

        // Drag
        if (dragging) {
            x += mouseX - dragX;
            y += mouseY - dragY;
            dragX = mouseX;
            dragY = mouseY;
        }

        ScaledResolution sr = new ScaledResolution(mc);
        if (x < 10) x = 10;
        if (y < 10) y = 10;
        if (x + width > sr.getScaledWidth() - 10) x = sr.getScaledWidth() - 10 - width;
        if (y + height > sr.getScaledHeight() - 10) y = sr.getScaledHeight() - 10 - height;

        if (PostProcessing.blur.getValue()) {
            BlurUtil.processStart();
            Gui.drawNewRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), Color.BLACK.getRGB());
            BlurUtil.blurEnd();
        }

        RenderUtil.scaleStart(x + width / 2, y + height / 2, windowAnim.getOutput().floatValue());

        // Window BG
        RoundedUtil.drawRound(x, y, width, height, radius, ThemeColor.windowColor);

        // Category BG
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(x - 1f, y + 1f, leftWidth + radius, height + 1f, radius, ThemeColor.categoryColor);
        StencilUtil.readStencilBuffer(1);
        RenderUtil.scissorStart(x, y, leftWidth, height);
        RoundedUtil.drawRoundOutline(x - radius, y - radius, leftWidth + radius + 0.4f, height + radius * 2, radius, 0.2f, ThemeColor.categoryColor, ThemeColor.outlineColor);
        RenderUtil.scissorEnd();
        StencilUtil.uninitStencilBuffer();

        // Top BG
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(x + leftWidth, y, width - leftWidth, topWidth, radius, Color.BLACK);
        StencilUtil.readStencilBuffer(1);
        RenderUtil.scissorStart(x + leftWidth, y, width - leftWidth, topWidth);
        RoundedUtil.drawRoundOutline(x + leftWidth - radius, y - radius, width - leftWidth + radius * 2, topWidth + radius, radius, 0.2f, ThemeColor.titleColor, ThemeColor.outlineColor);
        RenderUtil.scissorEnd();
        StencilUtil.uninitStencilBuffer();

        // Title
        FontRenderer font = FontLoader.rubik_bold(28);
        float centerX = x + leftWidth / 2f;
        if (PostProcessing.bloom.getValue()) {
            BlurUtil.processStart();
            font.drawCenteredString("UNTITLED", centerX + 0.5f, y + 13.5f, ThemeColor.focusedColor.getRGB(), CenterType.Horizontal);
            font.drawCenteredString("UNTITLED", centerX, y + 13f, Color.WHITE.getRGB(), CenterType.Horizontal);
            BlurUtil.bloomEnd();
        }
        font.drawCenteredString("UNTITLED", centerX + 0.5f, y + 13.5f, ThemeColor.focusedColor.getRGB(), CenterType.Horizontal);
        font.drawCenteredString("UNTITLED", centerX, y + 13f, Color.WHITE.getRGB(), CenterType.Horizontal);

        // Category
        float categoryX = centerX - font.getStringWidth("UNTITLED") / 2f - 3f;
        float categoryY = y + font.getFontHeight() + 50f;

        if (PostProcessing.bloom.getValue()) {
            BlurUtil.processStart();
            RoundedUtil.drawRound(categoryX - 2f, categoryY + panelAnim.getOutput().floatValue() - 5f, leftWidth - 14f, currentPanel.height + 10f, 4f, ThemeColor.barColor);
            BlurUtil.bloomEnd();
        }

        RoundedUtil.drawRound(categoryX - 2f, categoryY + panelAnim.getOutput().floatValue() - 5f, leftWidth - 14f, currentPanel.height + 10f, 4f, ThemeColor.barColor);

        for (CategoryType type : CategoryType.values()) {
            FontLoader.greyCliff(14).drawString(type.toString(), categoryX, categoryY - 15f, ThemeColor.grayColor.getRGB());
            for (CategoryPanel panel : categoryPanelList) {
                if (!CategoryType.getType(panel.category).equals(type)) continue;

                RenderUtil.drawImage(new ResourceLocation("untitled/icon/" + panel.category.name().toLowerCase(Locale.ROOT) + ".png"), categoryX, categoryY - 3f, 12, 12, ThemeColor.focusedColor);
                panel.draw(categoryX + 20f, categoryY, mouseX, mouseY);
                categoryY += panel.height + 15f;
            }
            categoryY += 20f;
        }

        // Module
        float scroll = (float) currentPanel.scrollAnim.animate();

        float moduleX;
        float originalY = y + topWidth + 8f;
        float leftY = originalY + scroll, rightY = originalY + scroll;

        int panelIndex = 0;

        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(x + leftWidth + 5f, originalY - 3f, width - leftWidth - 10f, height - topWidth - 10f, 1f, Color.BLACK);
        StencilUtil.readStencilBuffer(1);
        RenderUtil.scissorStart(x + leftWidth + 5f, originalY - 3f, width - leftWidth - 10f, height - topWidth - 10f);
        for (ModulePanel panel : currentPanel.modulePanelList) {
            if (!searchField.text.isEmpty()) {
                if (!panel.module.name.toLowerCase(Locale.ROOT).contains(searchField.text.toLowerCase(Locale.ROOT)))
                    continue;
            }
            boolean isLeft = panelIndex % 2 == 0;
            moduleX = x + leftWidth + 10f + (isLeft ? 0 : panel.width + 10);
            panel.draw(moduleX, isLeft ? leftY : rightY, mouseX, mouseY);
            if (isLeft) leftY += panel.height + 20;
            else rightY += panel.height + 20;
            panelIndex++;
        }
        RenderUtil.scissorEnd();
        StencilUtil.uninitStencilBuffer();

        // Scroll
        currentPanel.handleScroll();

        if (currentPanel.scrollAnim.target < 0) {
            if (topOpacityAnim.getDirection() == Direction.BACKWARDS) topOpacityAnim.changeDirection();
        } else if (topOpacityAnim.getDirection() == Direction.FORWARDS) topOpacityAnim.changeDirection();

        float opacity = topOpacityAnim.getOutput().floatValue();
        if (opacity != 0) {
            float size = 20f;
            float gap = 5f;
            float roundX = x + width - gap - size;
            float roundY = y + height - gap - size;
            boolean hovering = RenderUtil.hovering(mouseX, mouseY, roundX, roundY, size, size);

            if (hovering && topColorAnim.getDirection() == Direction.FORWARDS) {
                topColorAnim.changeDirection();
            } else if (!hovering && topColorAnim.getDirection() == Direction.BACKWARDS) topColorAnim.changeDirection();

            if (hovering && Mouse.isButtonDown(0)) currentPanel.scrollAnim.target = 0f;

            if (PostProcessing.bloom.getValue()) {
                BlurUtil.processStart();
                RoundedUtil.drawRound(roundX, roundY, size, size, 10f, ColorUtil.applyOpacity(topColorAnim.getOutput(), opacity));
                RenderUtil.drawImage(new ResourceLocation("untitled/icon/top.png"), roundX + 2.6f, roundY + 3.1f, 14f, 14f, ColorUtil.applyOpacity(Color.WHITE, opacity));
                BlurUtil.bloomEnd();
            }

            RoundedUtil.drawRound(roundX, roundY, size, size, 10f, ColorUtil.applyOpacity(topColorAnim.getOutput(), opacity));
            RenderUtil.drawImage(new ResourceLocation("untitled/icon/top.png"), roundX + 2.6f, roundY + 3.1f, 14f, 14f, ColorUtil.applyOpacity(Color.WHITE, opacity));
        }

        // Search
        float searchX = x + leftWidth + 10f;
        float searchY = y + topWidth / 2 - 11f;
        float iconSize = 14f;

        searchField.outlineColor = iconColorAnim.getOutput();
        searchField.draw(searchX, searchY, mouseX, mouseY);
        RenderUtil.drawImage(new ResourceLocation("untitled/icon/search.png"), searchX + 3f, searchY + 3f, iconSize, iconSize, iconColorAnim.getOutput());

        boolean selected = searchField.focused || RenderUtil.hovering(mouseX, mouseY, searchX, searchY, iconSize + searchField.width + 5f, iconSize);
        if (selected && iconColorAnim.getDirection() == Direction.FORWARDS) {
            iconColorAnim.changeDirection();
        } else if (!selected && iconColorAnim.getDirection() == Direction.BACKWARDS) {
            iconColorAnim.changeDirection();
        }

        RenderUtil.scaleEnd();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        boolean close = true;

        for (ModulePanel panel : currentPanel.modulePanelList) {
            panel.onKeyTyped(typedChar, keyCode);
            if (panel.listening) {
                panel.listening = false;
                close = false;
            }
        }

        if (keyCode == Keyboard.KEY_ESCAPE && windowAnim.getDirection() == Direction.FORWARDS && close) {
            windowAnim.changeDirection();
            Keyboard.enableRepeatEvents(false);
        }

        searchField.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (!RenderUtil.hovering(mouseX, mouseY, x, y, width, height)) return;

        if (RenderUtil.hovering(mouseX, mouseY, x, y, width, topWidth)) {
            dragging = true;
            dragX = mouseX;
            dragY = mouseY;
        }

        for (CategoryPanel panel : categoryPanelList) {
            if (RenderUtil.hovering(mouseX, mouseY, panel.x, panel.y - 2, panel.width, panel.height + 4) && mouseButton == 0 && panel != currentPanel) {
                float categoryY = y + FontLoader.rubik_bold(28).getFontHeight() + 50f;
                panelAnim.setStartPoint(currentPanel.y - categoryY);
                currentPanel = panel;
                panelAnim.setEndPoint(panel.y - categoryY);
                panelAnim.getAnimation().reset();

                currentPanel.modulePanelList.forEach(ModulePanel::init);
                break;
            }
        }

        for (ModulePanel panel : currentPanel.modulePanelList) {
            panel.onMouseClick(mouseX, mouseY, mouseButton);
        }

        searchField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
        currentPanel.modulePanelList.forEach(Panel::onMouseRelease);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
