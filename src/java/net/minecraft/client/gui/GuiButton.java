package net.minecraft.client.gui;

import cn.feng.untitled.ui.font.FontLoader;
import cn.feng.untitled.util.animation.Direction;
import cn.feng.untitled.util.animation.composed.ColorAnimation;
import cn.feng.untitled.util.animation.composed.CustomAnimation;
import cn.feng.untitled.util.animation.impl.SmoothStepAnimation;
import cn.feng.untitled.util.render.RoundedUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiButton extends Gui
{
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");

    /** Button width in pixels */
    protected int width;

    /** Button height in pixels */
    protected int height;

    /** The x position of this control. */
    public int xPosition;

    /** The y position of this control. */
    public int yPosition;

    /** The string displayed on this control. */
    public String displayString;
    public int id;

    /** True if this control is enabled, false to disable. */
    public boolean enabled;

    /** Hides the button completely if false. */
    public boolean visible;
    protected boolean hovered;

    /** Outline color animation. */
    private ColorAnimation colorAnim;

    public GuiButton(int buttonId, int x, int y, String buttonText)
    {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
    {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
        this.colorAnim = new ColorAnimation(new Color(255, 255, 255, 70), Color.WHITE, 100);
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver)
    {
        int i = 1;

        if (!this.enabled)
        {
            i = 0;
        }
        else if (mouseOver)
        {
            i = 2;
        }

        return i;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            if (hovered) {
                if (colorAnim.getDirection() == Direction.BACKWARDS) colorAnim.changeDirection();
            } else if (colorAnim.getDirection() == Direction.FORWARDS) colorAnim.changeDirection();

            this.mouseDragged(mc, mouseX, mouseY);
            RoundedUtil.drawRoundOutline(this.xPosition, this.yPosition, this.width, this.height, 2f, 0.1f, new Color(0, 0, 0, 100), enabled? colorAnim.getOutput() : new Color(50, 50, 50, 255));
            FontLoader.miSans(18).drawCenteredString(displayString, this.xPosition + this.width / 2f, this.yPosition + this.height / 2f - 3f, Color.WHITE.getRGB());
        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY)
    {
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    public boolean isMouseOver()
    {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY)
    {
    }

    public void playPressSound(SoundHandler soundHandlerIn)
    {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth()
    {
        return this.width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }
}
