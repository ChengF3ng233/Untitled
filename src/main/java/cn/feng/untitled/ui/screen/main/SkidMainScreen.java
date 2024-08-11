package cn.feng.untitled.ui.screen.main;

import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoUtil;
import cn.feng.untitled.ui.screen.component.SkidButton;
import cn.feng.untitled.ui.screen.component.UntitledButton;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.blur.BlurUtil;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.nanovg.NanoVG;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author ChengFeng
 * @since 2024/8/11
 **/
public class SkidMainScreen extends GuiScreen {
    private final List<SkidButton> buttonList = new ArrayList<>();

    public SkidMainScreen() {
        buttonList.add(new SkidButton("Single Player", () -> mc.displayGuiScreen(new GuiSelectWorld(this))));
        buttonList.add(new SkidButton("Multi Player", () -> mc.displayGuiScreen(new GuiMultiplayer(this))));
        buttonList.add(new SkidButton("Alt Manager", () -> JOptionPane.showMessageDialog(null, "我几把没写呢")));
        buttonList.add(new SkidButton("Options", () -> mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings))));
        buttonList.add(new SkidButton("Exit", () -> mc.shutdown()));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        int scaledWidth = sr.getScaledWidth();
        int scaledHeight = sr.getScaledHeight();

        RenderUtil.drawImage(new ResourceLocation("untitled/image/background2.png"), 0, 0, scaledWidth, scaledHeight);
        BlurUtil.processStart();
        RenderUtil.drawQuads(
                new float[]{0, 0},
                new float[]{0, height},
                new float[]{scaledWidth * 0.57f, 0},
                new float[]{scaledWidth * 0.43f, scaledHeight},
                Color.BLACK,
                Color.BLACK
        );
        BlurUtil.blurEnd(2, 3);
        BlurUtil.processStart();
        RenderUtil.drawQuads(
                new float[]{0, 0},
                new float[]{0, height},
                new float[]{scaledWidth * 0.57f, 0},
                new float[]{scaledWidth * 0.43f, scaledHeight},
                Color.BLACK,
                Color.BLACK
        );
        BlurUtil.bloomEnd(2, 1);


        RenderUtil.drawQuads(
                new float[]{scaledWidth * 0.57f - scaledWidth * 0.11f, 0},
                new float[]{scaledWidth * 0.43f - scaledWidth * 0.11f, scaledHeight},
                new float[]{scaledWidth * 0.57f, 0},
                new float[]{scaledWidth * 0.43f, scaledHeight},
                new Color(0, 0, 0, 150),
                new Color(0, 0, 0, 130)
        );
        RenderUtil.drawQuads(
                new float[]{scaledWidth * 0.57f - scaledWidth * 0.22f, 0},
                new float[]{scaledWidth * 0.43f - scaledWidth * 0.22f, scaledHeight},
                new float[]{scaledWidth * 0.57f - scaledWidth * 0.11f, 0},
                new float[]{scaledWidth * 0.43f - scaledWidth * 0.11f, scaledHeight},
                new Color(0, 0, 0, 200),
                new Color(0, 0, 0, 180)
        );
        RenderUtil.drawQuads(
                new float[]{0, 0},
                new float[]{0, height},
                new float[]{scaledWidth * 0.57f - scaledWidth * 0.22f, 0},
                new float[]{scaledWidth * 0.43f - scaledWidth * 0.22f, scaledHeight},
                new Color(0, 0, 0, 220),
                new Color(0, 0, 0, 210)
        );

        NanoUtil.beginFrame();

        NanoFontLoader.rubikBold.drawGlowString("Hello Minecraft", 30f, 20f, 40f, Color.WHITE);
        NanoFontLoader.greyCliffBold.drawGlowString("--- Untitled client, an experimental project", 35f, 50f, 18f, Color.GRAY);

        float buttonX = 30f;
        float buttonY = scaledHeight * 0.3f;
        for (SkidButton button : buttonList) {
            button.draw(buttonX, buttonY, mouseX, mouseY);
            buttonY += button.height + 10f;
        }

        NanoFontLoader.greyCliffBold.drawString("Minecraft Client - Modified Version", 30f, scaledHeight - 55f, 16f, Color.GRAY);
        NanoFontLoader.greyCliffBold.drawString("Love From ChengFeng", 30f, scaledHeight - 55f - 10f, 16f, Color.GRAY);
        NanoFontLoader.greyCliffBold.drawString("TRACEABILITY", 30f, scaledHeight - 55f - 20f, 16f, Color.GRAY);
        NanoFontLoader.greyCliffBold.drawString("2024", 30f, scaledHeight - 55f - 30f, 16f, Color.GRAY);

        Gui.drawNewRect(30f, scaledHeight - 38f, NanoFontLoader.greyCliffBold.getStringWidth("Minecraft Client - Modified Version", 16f), 0.5f, Color.GRAY.getRGB());

        Color textColor = Color.WHITE;
        Color color = new Color(0, 109, 255);

        NanoFontLoader.rubikBold.drawGlowString("OPTIFINE", scaledWidth - 30f, 30f, 28f, NanoVG.NVG_ALIGN_RIGHT, textColor);
        Gui.drawNewRect(width - 170f, 50f, 150f, 0.5f, textColor.getRGB());
        NanoFontLoader.greyCliffBold.drawString("Hello#Untitled", width - 165f, 52f, 20f, textColor);

        LocalDate today = LocalDate.now();

        DateTimeFormatter formatterYear = DateTimeFormatter.ofPattern("yyyy");
        DateTimeFormatter formatterMonth = DateTimeFormatter.ofPattern("MM");
        DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("dd");

        String year = today.format(formatterYear);
        String month = today.format(formatterMonth);
        String day = today.format(formatterDay);


        NanoFontLoader.rubikBold.drawGlowString(year, scaledWidth - 30f, 70f, 35f, NanoVG.NVG_ALIGN_RIGHT, textColor);
        NanoFontLoader.rubikBold.drawGlowString(month, scaledWidth - 34f - NanoFontLoader.rubikBold.getStringWidth(year) * 2, 84f, 60f, textColor);
        NanoFontLoader.rubikBold.drawGlowString(day, scaledWidth - 34f - NanoFontLoader.rubikBold.getStringWidth(year) * 2, 113f, 40f, textColor);

        NanoUtil.rotateStart(scaledWidth - 30f, 120f, 2f);
        NanoFontLoader.rubikBold.drawString("TIME /", 0f, 0f, 25f, textColor);
        NanoFontLoader.rubikBold.drawString("CALENDAR", 0f, 13f, 25f, textColor);
        NanoUtil.rotateEnd();

        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = now.format(formatter);

        NanoFontLoader.greyCliffBold.drawGlowString(formattedTime, scaledWidth - 150f, scaledHeight - 55f, 20f, textColor);
        Gui.drawNewRect(scaledWidth - 150f, scaledHeight - 38f, 100f, 0.5f, textColor.getRGB());
        Gui.drawNewRect(scaledWidth - 40f, scaledHeight - 38f, 10f, 0.5f, textColor.getRGB());
        Gui.drawNewRect(scaledWidth - 40f + 5f - 0.25f, scaledHeight - 38f - 5f - 0.25f, 0.5f, 10f, color.getRGB());
        Gui.drawNewRect(scaledWidth - 40f + 5f - 0.25f, scaledHeight - 150f, 0.5f, 100f, color.getRGB());

        NanoUtil.rotateStart(scaledWidth - 55f, scaledHeight - 150f, -2);
        NanoFontLoader.greyCliffBold.drawGlowString("#" + String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()).toUpperCase(Locale.ROOT), 0f, 0f, 20f, NanoVG.NVG_ALIGN_RIGHT, color);
        NanoUtil.rotateEnd();

        NanoUtil.endFrame();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (SkidButton skidButton : buttonList) {
            skidButton.onMouseClicked(mouseX, mouseY, mouseButton);
        }
    }
}
