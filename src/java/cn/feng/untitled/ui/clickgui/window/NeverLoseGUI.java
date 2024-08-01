package cn.feng.untitled.ui.clickgui.window;

import cn.feng.untitled.module.ModuleCategory;
import cn.feng.untitled.ui.clickgui.window.gui.IconButton;
import cn.feng.untitled.ui.clickgui.window.panel.impl.CategoryPanel;
import cn.feng.untitled.ui.clickgui.window.panel.impl.ModulePanel;
import cn.feng.untitled.ui.font.FontLoader;
import cn.feng.untitled.ui.font.FontRenderer;
import cn.feng.untitled.util.animation.advanced.Animation;
import cn.feng.untitled.util.animation.advanced.Direction;
import cn.feng.untitled.util.animation.advanced.composed.CustomAnimation;
import cn.feng.untitled.util.animation.advanced.impl.SmoothStepAnimation;
import cn.feng.untitled.util.animation.simple.SimpleAnimation;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import cn.feng.untitled.util.render.StencilUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class NeverLoseGUI extends GuiScreen {
    public static float width, height, leftWidth, topWidth, radius;
    private float x, y, resetX, resetY, resetWidth, resetHeight;
    private boolean dragging;
    private float dragX, dragY;

    private Animation windowAnim;
    private CustomAnimation panelAnim;
    private IconButton resetButton;

    private final List<CategoryPanel> categoryPanelList;
    private CategoryPanel currentPanel;

    public NeverLoseGUI() {
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

        panelAnim = new CustomAnimation(SmoothStepAnimation.class, 200,0, 0);
    }

    @Override
    public void initGui() {
        dragging = false;
        windowAnim = new SmoothStepAnimation(150, 1d);

        resetHeight = 15f;
        resetButton = new IconButton(FontLoader.rubik(16), resetHeight, "Reset Scroll", new ResourceLocation("untitled/icon/refresh.png"), 16, 1f);
        resetWidth = resetButton.width;

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
        FontRenderer font = FontLoader.rubik(28);
        float centerX = x + leftWidth / 2f;
        font.drawCenteredString("UNTITLED", centerX + 0.5f, y + 13.5f, ThemeColor.focusedColor.getRGB());
        font.drawCenteredString("UNTITLED", centerX, y + 13f, Color.WHITE.getRGB());

        // Category
        float categoryX = centerX - font.getStringWidth("UNTITLED") / 2f - 3f;
        float categoryY = y + font.height() + 40f;

        RoundedUtil.drawRound(categoryX - 2f, categoryY + panelAnim.getOutput().floatValue() - 5f, leftWidth - 14f, currentPanel.height + 6f, 4f, ThemeColor.barColor);

        for (CategoryType type : CategoryType.values()) {
            FontLoader.greyCliff(14).drawString(type.toString(), categoryX, categoryY - 15f, ThemeColor.grayColor.getRGB());
            for (CategoryPanel panel : categoryPanelList) {
                if (!CategoryType.getType(panel.category).equals(type)) continue;

                RenderUtil.drawImage(new ResourceLocation("untitled/icon/" + panel.category.name().toLowerCase(Locale.ROOT) + ".png"), categoryX, categoryY - 3f, 12, 12, ThemeColor.focusedColor);
                panel.draw(categoryX + 20f, categoryY, mouseX, mouseY);
                categoryY += panel.height + 10f;
            }
            categoryY += 20f;
        }

        // Module
        float scroll = currentPanel.scrollAnim.animate();

        float moduleX;
        float originalY = y + topWidth + 5f;
        float leftY = originalY + scroll, rightY = originalY + scroll;

        int panelIndex = 0;

        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(x + leftWidth + 5f, originalY, width - leftWidth - 10f, height - topWidth - 10f, 0f, Color.BLACK);
        StencilUtil.readStencilBuffer(1);
        for (ModulePanel panel : currentPanel.modulePanelList) {
            boolean isLeft = panelIndex % 2 == 0;
            moduleX = x + leftWidth + 10f + (isLeft ? 0 : panel.width + 10);
            panel.draw(moduleX, isLeft ? leftY : rightY, mouseX, mouseY);
            if (isLeft) leftY += panel.height + 20;
            else rightY += panel.height + 20;
            panelIndex++;
        }
        StencilUtil.uninitStencilBuffer();

        // Scroll
        currentPanel.handleScroll();

        resetX = x + width - 20f - resetWidth;
        resetY = y + topWidth / 2 - resetHeight / 2 - 2f;
        resetButton.draw(resetX, resetY, mouseX, mouseY);

        RenderUtil.scaleEnd();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            windowAnim.changeDirection();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (!RenderUtil.hovering(mouseX, mouseY, x, y, width, height)) return;

        if (RenderUtil.hovering(mouseX, mouseY, x, y, width, topWidth)) {

            if (RenderUtil.hovering(mouseX, mouseY, resetX, resetY, resetWidth, resetHeight)) {
                currentPanel.scrollAnim.target = 0f;
            } else {
                dragging = true;
                dragX = mouseX;
                dragY = mouseY;
            }
        }

        for (CategoryPanel panel : categoryPanelList) {
            if (RenderUtil.hovering(mouseX, mouseY, panel.x, panel.y - 2, panel.width, panel.height + 4) && mouseButton == 0 && panel != currentPanel) {
                float categoryY = y + FontLoader.rubik(28).height() + 40f;
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
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
