package cn.feng.untitled.music.ui;

import cn.feng.untitled.event.api.SubscribeEvent;
import cn.feng.untitled.event.impl.NanoEvent;
import cn.feng.untitled.module.impl.client.PostProcessing;
import cn.feng.untitled.music.api.base.Music;
import cn.feng.untitled.music.api.player.MusicPlayer;
import cn.feng.untitled.music.api.player.PlayMode;
import cn.feng.untitled.music.thread.SearchMusicThread;
import cn.feng.untitled.music.ui.component.NanoTextField;
import cn.feng.untitled.music.ui.component.button.Button;
import cn.feng.untitled.music.ui.component.button.impl.*;
import cn.feng.untitled.music.ui.component.slider.impl.PlayerSlider;
import cn.feng.untitled.music.ui.component.slider.impl.VolumeSlider;
import cn.feng.untitled.music.ui.gui.MusicPlayerGUI;
import cn.feng.untitled.music.ui.gui.impl.PlayListGUI;
import cn.feng.untitled.music.ui.gui.impl.PlayListListGUI;
import cn.feng.untitled.ui.clickgui.gui.TextField;
import cn.feng.untitled.ui.font.awt.FontLoader;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoUtil;
import cn.feng.untitled.util.animation.advanced.Animation;
import cn.feng.untitled.util.animation.advanced.Direction;
import cn.feng.untitled.util.animation.advanced.impl.SmoothStepAnimation;
import cn.feng.untitled.util.data.resource.ResourceType;
import cn.feng.untitled.util.data.resource.ResourceUtil;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.blur.BlurUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private float coverAngle = 0f;
    private int mouseX, mouseY;

    // Animation
    private Animation windowAnim;

    // Current Page
    public final List<CategoryButton> categoryButtons = new ArrayList<>();
    private final UserButton userButton = new UserButton();
    @Getter
    private final NanoTextField searchField = new NanoTextField(150, 10, NanoFontLoader.misans, ThemeColor.bgColor, ThemeColor.outlineColor);
    @Setter
    @Getter
    private MusicPlayerGUI currentGUI;

    // Thread
    private SearchMusicThread searchThread;

    // Player
    public final MusicPlayer player = new MusicPlayer();

    private final IconButton playBtn = new IconButton("play.png", player::play, 15);
    private final IconButton pauseBtn = new IconButton("pause.png", player::pause, 15);
    private final IconButton preBtn = new IconButton("previous.png", () -> player.previous(true), 14);
    private final IconButton nextBtn = new IconButton("next.png", () -> player.next(true), 14);
    private final SwitchIconButton orderBtn = new SwitchIconButton("loop.png", Arrays.stream(new String[]{"loop.png", "ordered.png", "shuffle.png", "recycle.png"}).toList(), 13);
    private final QualityButton qualityBtn = new QualityButton();
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

        currentGUI = categoryButtons.get(0).getGui();
        categoryButtons.get(0).setSelected(true);

        orderBtn.setAction(() -> {
            if (player.getMusic() == null || player.getMediaPlayer() == null) return;
            player.setPlayMode(
                    switch (orderBtn.getFileName()) {
                        case "loop.png" -> PlayMode.LOOP;
                        case "shuffle.png" -> PlayMode.SHUFFLED;
                        case "recycle.png" -> PlayMode.RECYCLED;
                        default -> PlayMode.LISTED;
                    }, false
            );
        });
    }

    @Override
    public void initGui() {
        windowAnim = new SmoothStepAnimation(150, 1d);
    }

    // onRender2D
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        if (PostProcessing.blur.getValue()) {
            BlurUtil.processStart();
            GuiScreen.drawNewRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), Color.BLACK.getRGB());
            BlurUtil.blurEnd();
        } else {
            GuiScreen.drawNewRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, 100).getRGB());
        }
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @SubscribeEvent
    private void onNano(NanoEvent e) {
        if (Minecraft.getMinecraft().currentScreen != this) return;
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

        NanoUtil.scaleStart(0, 0, sr.getScaleFactor() * 0.5f);
        NanoUtil.scaleStart(x + width / 2, y + height / 2, windowAnim.getOutput().floatValue());

        NanoUtil.drawRoundedOutlineRect(x, y, width, height, 3f, 1f, ThemeColor.bgColor, ThemeColor.outlineColor);
        NanoUtil.drawRoundedRect(x + 1f, y + 1f, leftWidth - 2f, height - 2f, 3f, 0f, 0f, 3f, ThemeColor.categoryColor);

        NanoUtil.drawImageRect("netease", ResourceUtil.getResourceAsStream("netease.png", ResourceType.ICON), x + 12f, y + 8f, 16, 16);
        NanoFontLoader.misans.drawGlowString("网易云音乐", x + 32f, y + 12f, 20, Color.WHITE);


        float btnX = x + leftWidth / 2f - categoryButtons.get(0).width / 2f;
        float btnY = y + 40f;

        for (Button btn : categoryButtons) {
            btn.updateState(btnX, btnY, mouseX, mouseY);
            btn.render();
            btnY += btn.height + 8f;
        }

        userButton.updateState(x + width - userButton.width - 10f, y + 10f - userButton.height / 2f, mouseX, mouseY);
        userButton.render();

        searchField.height = 15f;
        searchField.draw(x + leftWidth + 30f, y + 7f, mouseX, mouseY);

        currentGUI.setWidth(width - leftWidth);
        currentGUI.setHeight(height - topWidth - bottomWidth);
        currentGUI.render(x + leftWidth, y + topWidth, mouseX, mouseY, x + width / 2f, y + width / 2f, windowAnim.getOutput().floatValue());

        if (RenderUtil.hovering(mouseX, mouseY, x + leftWidth, y + topWidth, width - leftWidth, height - topWidth - bottomWidth)) {
            currentGUI.handleScroll();
        }

        if (currentGUI.parent != null) {
            NanoUtil.drawImageRect(ResourceUtil.getResource("arrow-left.png", ResourceType.ICON), x + leftWidth + 5f, y + 7f, 16f, 16f,
                    RenderUtil.hovering(mouseX, mouseY, x + leftWidth + 5f, y + 7f, 16f, 16f) ? Color.WHITE : ThemeColor.greyColor);
        }

        // Player bottom
        Music music = player.getMusic();
        NanoUtil.drawRoundedRect(x + 1f, y + height - bottomWidth - 1f, width - 2f, bottomWidth, 0f, 0f, 3f, 3f, ThemeColor.playerColor);
        if (music != null && player.getMediaPlayer() != null) {
            if (music.getCoverTexture() == 0) {
                music.setCoverTexture(NanoUtil.genImageId(music.getCoverFile()));
            }
            if (!player.isPaused()) {
                coverAngle += (float) (0.5 * RenderUtil.frameTime);
            }
            float playerY = y + (height - bottomWidth);

            NanoFontLoader.misans.drawString(music.getName(), x + 45f, playerY + 7f, 15f, Color.WHITE);
            NanoFontLoader.misans.drawString(music.getArtist(), x + 45f, playerY + 17f, 12f, ThemeColor.greyColor);

            NanoUtil.drawCircle(x + 30f, playerY + 17f, 11f, ThemeColor.barColor);
            NanoUtil.drawImageCircle(music.getCoverTexture(), x + 30f, playerY + 17f, 10f);

            if (player.isPaused()) {
                playBtn.setBg(true);
                playBtn.updateState(x + width / 2f - 10f, playerY + 4f, mouseX, mouseY);
                playBtn.render();
            } else {
                pauseBtn.setBg(true);
                pauseBtn.updateState(x + width / 2f - 10f, playerY + 4f, mouseX, mouseY);
                pauseBtn.render();
            }

            preBtn.updateState(x + width / 2f - 35f, playerY + 4f, mouseX, mouseY);
            preBtn.render();

            nextBtn.updateState(x + width / 2f + 16f, playerY + 4f, mouseX, mouseY);
            nextBtn.render();

            orderBtn.updateState(x + width / 2f + 50f, playerY + 5f, mouseX, mouseY);
            orderBtn.render();

            qualityBtn.updateState(x + width / 2f - 55f - qualityBtn.width, playerY + 6f, mouseX, mouseY);
            qualityBtn.render();

            playerSlider.render(x + width / 2f - 100f, playerY + 27f, mouseX, mouseY);
            volumeSlider.render(x + width - 120f, playerY + 27f, mouseX, mouseY);
        }

        NanoUtil.scaleEnd();

        if (searchThread != null && !searchThread.isAlive()) {
            setCurrentGUI(searchThread.getGui());
            searchField.text = "";
            searchField.focused = false;
            searchThread = null;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE && windowAnim.getDirection() == Direction.FORWARDS) {
            windowAnim.changeDirection();
            Keyboard.enableRepeatEvents(false);
        }

        if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER && !searchField.text.isEmpty() && (searchThread == null || !searchThread.isAlive())) {
            searchThread = new SearchMusicThread(this);
            searchThread.start();
        }

        searchField.keyTyped(typedChar, keyCode);
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
        searchField.mouseClicked(mouseX, mouseY, mouseButton);

        if (RenderUtil.hovering(mouseX, mouseY, x + leftWidth, y + topWidth, width - leftWidth, height - topWidth - bottomWidth - 3f)) {
            currentGUI.mouseClicked(mouseX, mouseY, mouseButton);
        }

        if (RenderUtil.hovering(mouseX, mouseY, x, y + (height - bottomWidth), width, bottomWidth) && player.getMusic() != null) {
            if (player.isPaused()) {
                playBtn.mouseClicked(mouseX, mouseY, mouseButton);
            } else pauseBtn.mouseClicked(mouseX, mouseY, mouseButton);
            preBtn.mouseClicked(mouseX, mouseY, mouseButton);
            nextBtn.mouseClicked(mouseX, mouseY, mouseButton);
            orderBtn.mouseClicked(mouseX, mouseY, mouseButton);
            qualityBtn.mouseClicked(mouseX, mouseY, mouseButton);
            playerSlider.mouseClicked(mouseButton);
            volumeSlider.mouseClicked(mouseButton);
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
