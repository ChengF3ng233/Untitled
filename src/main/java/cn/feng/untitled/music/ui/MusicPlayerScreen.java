package cn.feng.untitled.music.ui;

import cn.feng.untitled.music.api.Music;
import cn.feng.untitled.music.api.MusicPlayer;
import cn.feng.untitled.music.ui.component.Button;
import cn.feng.untitled.music.ui.component.slider.PlayerSlider;
import cn.feng.untitled.music.ui.component.impl.CategoryButton;
import cn.feng.untitled.music.ui.component.impl.IconButton;
import cn.feng.untitled.music.ui.component.impl.UserButton;
import cn.feng.untitled.music.ui.component.slider.VolumeSlider;
import cn.feng.untitled.music.ui.gui.MusicPlayerGUI;
import cn.feng.untitled.music.ui.gui.impl.PlayListGUI;
import cn.feng.untitled.music.ui.gui.impl.PlayListListGUI;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoUtil;
import cn.feng.untitled.util.animation.advanced.Animation;
import cn.feng.untitled.util.animation.advanced.Direction;
import cn.feng.untitled.util.animation.advanced.impl.DecelerateAnimation;
import cn.feng.untitled.util.data.resource.ResourceType;
import cn.feng.untitled.util.data.resource.ResourceUtil;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import lombok.Getter;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/8/12
 **/
public class MusicPlayerScreen extends GuiScreen {
    // Coords
    private float x;
    private float y;
    public static float width, height, topWidth, leftWidth, bottomWidth;
    private boolean dragging;
    private float dragX, dragY;

    // Animation
    private Animation windowAnim;

    // Current Page
    public final List<CategoryButton> categoryButtons = new ArrayList<>();
    private final UserButton userButton = new UserButton();
    @Getter
    private MusicPlayerGUI currentGUI;

    // Player
    public final MusicPlayer player = new MusicPlayer();

    private final IconButton playBtn = new IconButton("play.png", player::play);
    private final IconButton pauseBtn = new IconButton("pause.png", player::pause);
    private final IconButton preBtn = new IconButton("previous.png", player::previous);
    private final IconButton nextBtn = new IconButton("next.png", player::next);
    private final PlayerSlider playerSlider = new PlayerSlider();
    private final VolumeSlider volumeSlider = new VolumeSlider();

    public MusicPlayerScreen() {
        x = 10f;
        y = 10f;
        width = 470f;
        height = 310f;
        topWidth = 30f;
        leftWidth = 100f;
        bottomWidth = 35f;

        categoryButtons.add(new CategoryButton("为我推荐", MusicCategory.RECOMMENDED, new PlayListListGUI()));
        categoryButtons.add(new CategoryButton("我喜欢的音乐", MusicCategory.LIKED, new PlayListGUI(categoryButtons.get(0).getGui())));
        categoryButtons.add(new CategoryButton("最近播放", MusicCategory.RECENT, new PlayListGUI(categoryButtons.get(0).getGui())));

        currentGUI = categoryButtons.get(0).getGui();
        categoryButtons.get(0).setSelected(true);
    }

    public void setCurrentGUI(MusicPlayerGUI newGUI) {
        currentGUI.freeMemory();
        currentGUI = newGUI;
    }

    @Override
    public void initGui() {
        windowAnim = new DecelerateAnimation(100, 1d);
    }

    float coverAngle = 0f;

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

        // 碰撞检测
        ScaledResolution sr = new ScaledResolution(mc);
        if (x < 10) x = 10;
        if (y < 10) y = 10;
        if (x + width > sr.getScaledWidth() - 10) x = sr.getScaledWidth() - 10 - width;
        if (y + height > sr.getScaledHeight() - 10) y = sr.getScaledHeight() - 10 - height;

        RenderUtil.scaleStart(x + width / 2, y + height / 2, windowAnim.getOutput().floatValue());
        RoundedUtil.drawRoundOutline(x, y, width, height, 3f, 0.2f, ThemeColor.bgColor, ThemeColor.outlineColor);
        RoundedUtil.drawRound(x + 2f, y + 2f, leftWidth - 2f, height - 4f, 2.6f, ThemeColor.categoryColor);

        NanoUtil.beginFrame();
        NanoUtil.scaleStart(0, 0, sr.getScaleFactor() * 0.5f);
        NanoUtil.scaleStart(x + width / 2, y + height / 2, windowAnim.getOutput().floatValue());

        RenderUtil.drawImage(new ResourceLocation("untitled/icon/netease.png"), x + 12f, y + 10f, 16, 16);
        NanoFontLoader.misans.drawGlowString("网易云音乐", x + 32f, y + 12f, 20, Color.WHITE);

        float btnX = x + leftWidth / 2f - categoryButtons.get(0).width / 2f;
        float btnY = y + 40f;
        for (Button btn : categoryButtons) {
            btn.updateState(btnX, btnY, mouseX, mouseY);
            btn.draw();
            btnY += btn.height + 8f;
        }

        userButton.updateState(x + width - userButton.width - 10f, y + 10f - userButton.height / 2f, mouseX, mouseY);
        userButton.draw();

        RoundedUtil.drawRound(x + 2f, y + height - bottomWidth - 2f, width - 4f, bottomWidth, 2.6f, ThemeColor.playerColor);

        currentGUI.setWidth(width - leftWidth);
        currentGUI.setHeight(height - topWidth - bottomWidth);
        currentGUI.draw(x + leftWidth, y + topWidth, mouseX, mouseY, x + width / 2f, y + width / 2f, windowAnim.getOutput().floatValue());

        if (RenderUtil.hovering(mouseX, mouseY, x + leftWidth, y + topWidth, width - leftWidth, height - topWidth - bottomWidth)) {
            currentGUI.handleScroll();
        }

        if (currentGUI.parent != null) {
            RenderUtil.drawImage(ResourceUtil.getResource("arrow-left.png", ResourceType.ICON), x + leftWidth + 5f, y + 7f, 16f, 16f,
                    RenderUtil.hovering(mouseX, mouseY, x + leftWidth + 5f, y + 7f, 16f, 16f) ? Color.WHITE : ThemeColor.greyColor);
        }

        Music music = player.getMusic();
        if (music != null && player.getMediaPlayer() != null) {
            if (music.getTexture() == null) {
                try {
                    music.setTexture(new DynamicTexture(ImageIO.read(music.getCoverImage())));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            float playerY = y + (height - bottomWidth);
            RoundedUtil.drawRound(x + 19f, playerY + 6f, 22f, 22f, 10f, ThemeColor.bgColor);
            if (!player.isPaused()) {
                coverAngle += (float) (0.5 * RenderUtil.frameTime);
            }
            GL11.glPushMatrix();
            GlStateManager.bindTexture(music.getTexture().getGlTextureId());
            GL11.glTranslatef(x + 30f, playerY + 17f, 0f);
            GL11.glRotatef(coverAngle, 0f, 0f, 1f);
            GL11.glTranslatef(-10f, -10f, 0f);
            RoundedUtil.drawRoundTextured(0f, 0f, 20f, 20f, 10f, 1f);
            GL11.glPopMatrix();
            NanoFontLoader.misans.drawString(music.getName(), x + 45f, playerY + 7f, 15f, Color.WHITE);
            NanoFontLoader.misans.drawString(music.getArtist(), x + 45f, playerY + 17f, 12f, ThemeColor.greyColor);

            if (player.isPaused()) {
                playBtn.setSize(16);
                playBtn.setBg(true);
                playBtn.setZoom(true);
                playBtn.updateState(x + width / 2f - 10f, playerY + 3f, mouseX, mouseY);
                playBtn.draw();
            } else {
                pauseBtn.setSize(16);
                pauseBtn.setZoom(true);
                pauseBtn.setBg(true);
                pauseBtn.updateState(x + width / 2f - 10f, playerY + 3f, mouseX, mouseY);
                pauseBtn.draw();
            }

            preBtn.setSize(15);
            preBtn.updateState(x + width / 2f - 35f, playerY + 3f, mouseX, mouseY);
            preBtn.draw();

            nextBtn.setSize(15);
            nextBtn.updateState(x + width / 2f + 16f, playerY + 3f, mouseX, mouseY);
            nextBtn.draw();

            playerSlider.draw(x + width / 2f - 100f, playerY + 25f, mouseX, mouseY);
            volumeSlider.draw(x + width - 120f, playerY + 25f, mouseX, mouseY);
        }

        NanoUtil.scaleEnd();
        NanoUtil.endFrame();

        RenderUtil.scaleEnd();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE && windowAnim.getDirection() == Direction.FORWARDS) {
            windowAnim.changeDirection();
            Keyboard.enableRepeatEvents(false);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (RenderUtil.hovering(mouseX, mouseY, x, y, width, topWidth) && mouseButton == 0) {
            dragging = true;
            dragX = mouseX;
            dragY = mouseY;
        }

        if (RenderUtil.hovering(mouseX, mouseY, x + leftWidth + 5f, y + 7f, 16f, 16f) && mouseButton == 0 && currentGUI.parent != null) {
            setCurrentGUI(currentGUI.parent);
        }

        if (RenderUtil.hovering(mouseX, mouseY, x, y, leftWidth, height)) {
            CategoryButton selectedBtn = null;
            for (CategoryButton btn : categoryButtons) {
                if (btn.hovering && mouseButton == 0) {
                    btn.setSelected(true);
                    setCurrentGUI(btn.getGui());
                    selectedBtn = btn;
                }
            }

            // 如果点了空白地方就不要改了
            if (selectedBtn != null) {
                for (CategoryButton button : categoryButtons) {
                    if (!button.hovering) button.setSelected(false);
                }
            }
        }

        userButton.mouseClicked(mouseX, mouseY, mouseButton);

        if (RenderUtil.hovering(mouseX, mouseY, x + leftWidth, y + topWidth, width - leftWidth, height - topWidth - bottomWidth - 3f)) {
            currentGUI.mouseClicked(mouseX, mouseY, mouseButton);
        }

        if (RenderUtil.hovering(mouseX, mouseY, x, y + (height - bottomWidth), width, bottomWidth) && player.getMusic() != null) {
            if (player.isPaused()) {
                playBtn.mouseClicked(mouseX, mouseY, mouseButton);
            } else pauseBtn.mouseClicked(mouseX, mouseY, mouseButton);
            preBtn.mouseClicked(mouseX, mouseY, mouseButton);
            nextBtn.mouseClicked(mouseX, mouseY, mouseButton);
            playerSlider.mouseClicked(mouseX, mouseY, mouseButton);
            volumeSlider.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
        if (player.getMusic() != null) {
            playerSlider.mouseReleased();
            volumeSlider.mouseReleased();
        }
    }
}
