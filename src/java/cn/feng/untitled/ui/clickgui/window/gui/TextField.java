package cn.feng.untitled.ui.clickgui.window.gui;

import cn.feng.untitled.ui.font.Font;
import cn.feng.untitled.util.animation.advanced.Animation;
import cn.feng.untitled.util.animation.advanced.Direction;
import cn.feng.untitled.util.animation.advanced.impl.DecelerateAnimation;
import cn.feng.untitled.util.misc.TimerUtil;
import cn.feng.untitled.util.render.ColorUtil;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/8/1
 **/
public class TextField extends Gui {
    @Setter
    public Font font;
    private float xPosition;
    private float yPosition;
    @Setter
    @Getter
    private float radius = 2.0F;
    @Setter
    @Getter
    private float alpha = 1.0F;
    @Setter
    private float width;
    @Setter
    @Getter
    private float height;
    @Setter
    private float textAlpha = 1.0F;
    @Setter
    @Getter
    private Color outline;
    @Setter
    @Getter
    private Color fill;
    @Setter
    @Getter
    private Color bctColor;
    private Color focusedTextColor;
    private Color unfocusedTextColor;
    @Getter
    private String text;
    @Setter
    private String backgroundText;
    @Getter
    private int maxStringLength;
    @Setter
    @Getter
    private boolean drawingBackground;
    @Setter
    private boolean canLoseFocus;
    private boolean isFocused;
    private int lineScrollOffset;
    @Getter
    private int cursorPosition;
    @Getter
    private int selectionEnd;
    private final Animation textColor;
    private final Animation cursorBlinkAnimation;
    private final TimerUtil timerUtil;
    @Setter
    private boolean visible;

    public TextField(Font font) {
        this.outline = Color.WHITE;
        this.fill = ColorUtil.tripleColor(32);
        this.bctColor = new Color(207, 211, 217);
        this.focusedTextColor = new Color(224, 224, 224);
        this.unfocusedTextColor = new Color(130, 130, 130);
        this.text = "";
        this.maxStringLength = 32;
        this.drawingBackground = true;
        this.canLoseFocus = true;
        this.textColor = new DecelerateAnimation(250, 1.0D);
        this.cursorBlinkAnimation = new DecelerateAnimation(750, 1.0D);
        this.timerUtil = new TimerUtil();
        this.visible = true;
        this.font = font;
    }

    public TextField(Font font, float x, float y, float par5Width, float par6Height) {
        this.outline = Color.WHITE;
        this.fill = ColorUtil.tripleColor(32);
        this.bctColor = new Color(207, 211, 217);
        this.focusedTextColor = new Color(224, 224, 224);
        this.unfocusedTextColor = new Color(130, 130, 130);
        this.text = "";
        this.maxStringLength = 32;
        this.drawingBackground = true;
        this.canLoseFocus = true;
        this.textColor = new DecelerateAnimation(250, 1.0D);
        this.cursorBlinkAnimation = new DecelerateAnimation(750, 1.0D);
        this.timerUtil = new TimerUtil();
        this.visible = true;
        this.font = font;
        this.xPosition = x;
        this.yPosition = y;
        this.width = par5Width;
        this.height = par6Height;
    }

    public void setText(String text) {
        if (text.length() > this.maxStringLength) {
            this.text = text.substring(0, this.maxStringLength);
        } else {
            this.text = text;
        }

        this.setCursorPositionZero();
    }

    public String getSelectedText() {
        int i = Math.min(this.cursorPosition, this.selectionEnd);
        int j = Math.max(this.cursorPosition, this.selectionEnd);
        return this.text.substring(i, j);
    }

    public void writeText(String text) {
        String s = "";
        String s1 = ChatAllowedCharacters.filterAllowedCharacters(text);
        int min = Math.min(this.cursorPosition, this.selectionEnd);
        int max = Math.max(this.cursorPosition, this.selectionEnd);
        int len = this.maxStringLength - this.text.length() - (min - max);
        if (!this.text.isEmpty()) {
            s = s + this.text.substring(0, min);
        }

        int l;
        if (len < s1.length()) {
            s = s + s1.substring(0, len);
            l = len;
        } else {
            s = s + s1;
            l = s1.length();
        }

        if (!this.text.isEmpty() && max < this.text.length()) {
            s = s + this.text.substring(max);
        }

        this.text = s;
        this.moveCursorBy(min - this.selectionEnd + l);
    }

    public void deleteWords(int num) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                this.deleteFromCursor(this.getNthWordFromCursor(num) - this.cursorPosition);
            }
        }

    }

    public void deleteFromCursor(int num) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                boolean negative = num < 0;
                int i = negative ? this.cursorPosition + num : this.cursorPosition;
                int j = negative ? this.cursorPosition : this.cursorPosition + num;
                String s = "";
                if (i >= 0) {
                    s = this.text.substring(0, i);
                }

                if (j < this.text.length()) {
                    s = s + this.text.substring(j);
                }

                this.text = s;
                if (negative) {
                    this.moveCursorBy(num);
                }
            }
        }

    }

    public int getNthWordFromCursor(int n) {
        return this.getNthWordFromPos(n, this.getCursorPosition());
    }

    public int getNthWordFromPos(int n, int pos) {
        return this.func_146197_a(n, pos);
    }

    public int func_146197_a(int n, int pos) {
        int i = pos;
        boolean negative = n < 0;
        int j = Math.abs(n);

        for (int k = 0; k < j; ++k) {
            if (!negative) {
                int l = this.text.length();
                i = this.text.indexOf(32, i);
                if (i == -1) {
                    i = l;
                } else {
                    while (i < l && this.text.charAt(i) == ' ') {
                        ++i;
                    }
                }
            } else {
                while (i > 0 && this.text.charAt(i - 1) == ' ') {
                    --i;
                }

                while (i > 0 && this.text.charAt(i - 1) != ' ') {
                    --i;
                }
            }
        }

        return i;
    }

    public void moveCursorBy(int p_146182_1_) {
        this.setCursorPosition(this.selectionEnd + p_146182_1_);
    }

    public void setCursorPosition(int p_146190_1_) {
        this.cursorPosition = p_146190_1_;
        int i = this.text.length();
        this.cursorPosition = MathHelper.clamp_int(this.cursorPosition, 0, i);
        this.setSelectionPos(this.cursorPosition);
    }

    public void setCursorPositionZero() {
        this.setCursorPosition(0);
    }

    public void setCursorPositionEnd() {
        this.setCursorPosition(this.text.length());
    }

    public boolean keyTyped(char cha, int keyCode) {
        if (!this.isFocused) {
            return false;
        } else {
            this.timerUtil.reset();
            if (GuiScreen.isKeyComboCtrlA(keyCode)) {
                this.setCursorPositionEnd();
                this.setSelectionPos(0);
                return true;
            } else if (GuiScreen.isKeyComboCtrlC(keyCode)) {
                GuiScreen.setClipboardString(this.getSelectedText());
                return true;
            } else if (GuiScreen.isKeyComboCtrlV(keyCode)) {
                this.writeText(GuiScreen.getClipboardString());
                return true;
            } else if (GuiScreen.isKeyComboCtrlX(keyCode)) {
                GuiScreen.setClipboardString(this.getSelectedText());
                this.writeText("");
                return true;
            } else {
                switch (keyCode) {
                    case 14:
                        if (GuiScreen.isCtrlKeyDown()) {
                            this.deleteWords(-1);
                        } else {
                            this.deleteFromCursor(-1);
                        }

                        return true;
                    case 199:
                        if (GuiScreen.isShiftKeyDown()) {
                            this.setSelectionPos(0);
                        } else {
                            this.setCursorPositionZero();
                        }

                        return true;
                    case 203:
                        if (GuiScreen.isShiftKeyDown()) {
                            if (GuiScreen.isCtrlKeyDown()) {
                                this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                            } else {
                                this.setSelectionPos(this.getSelectionEnd() - 1);
                            }
                        } else if (GuiScreen.isCtrlKeyDown()) {
                            this.setCursorPosition(this.getNthWordFromCursor(-1));
                        } else {
                            this.moveCursorBy(-1);
                        }

                        return true;
                    case 205:
                        if (GuiScreen.isShiftKeyDown()) {
                            if (GuiScreen.isCtrlKeyDown()) {
                                this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                            } else {
                                this.setSelectionPos(this.getSelectionEnd() + 1);
                            }
                        } else if (GuiScreen.isCtrlKeyDown()) {
                            this.setCursorPosition(this.getNthWordFromCursor(1));
                        } else {
                            this.moveCursorBy(1);
                        }

                        return true;
                    case 207:
                        if (GuiScreen.isShiftKeyDown()) {
                            this.setSelectionPos(this.text.length());
                        } else {
                            this.setCursorPositionEnd();
                        }

                        return true;
                    case 211:
                        if (GuiScreen.isCtrlKeyDown()) {
                            this.deleteWords(1);
                        } else {
                            this.deleteFromCursor(1);
                        }

                        return true;
                    default:
                        if (ChatAllowedCharacters.isAllowedCharacter(cha)) {
                            this.writeText(Character.toString(cha));
                            return true;
                        } else {
                            return false;
                        }
                }
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        boolean flag = RenderUtil.hovering(this.xPosition, this.yPosition, this.width, this.height, mouseX, mouseY);
        if (this.canLoseFocus) {
            this.setFocused(flag);
        }

        if (this.isFocused && flag && mouseButton == 0) {
            float xPos = this.xPosition;
            if (this.backgroundText != null && this.backgroundText.equals("Search")) {
                xPos += 13.0F;
            }

            float i = (float) mouseX - xPos;
            String s = this.font.trimStringToWidth(this.text.substring(this.lineScrollOffset), (int) this.getWidth());
            this.setCursorPosition(this.font.trimStringToWidth(s, (int) i).length() + this.lineScrollOffset);
        }

    }

    public void drawTextBox() {
        if (this.getVisible()) {
            if (this.isFocused()) {
                Keyboard.enableRepeatEvents(true);
            }

            Color textColorWithAlpha = this.focusedTextColor;
            if (this.textAlpha != 1.0F) {
                textColorWithAlpha = ColorUtil.applyOpacity(this.focusedTextColor, this.textAlpha);
            }

            float xPos = this.xPosition + 3.0F;
            float yPos = this.yPosition + this.height / 2.0F - this.font.height() / 2.0F + 2.0F;
            if (this.isDrawingBackground()) {
                if (this.outline != null) {
                    RoundedUtil.drawRoundOutline(this.xPosition - 1.0F, this.yPosition - 1.0F, this.width + 2.0F, this.height + 2.0F, this.radius + 1.0F, 0.2F, ColorUtil.applyOpacity(this.fill, this.alpha), this.outline);
                } else {
                    RoundedUtil.drawRound(this.xPosition, this.yPosition, this.width, this.height, this.radius, ColorUtil.applyOpacity(this.fill, this.alpha));
                }
            } else {
                float rectHeight = 1.0F;
                Gui.drawRect2(this.xPosition, this.yPosition + this.height - rectHeight, this.width, rectHeight, (new Color(255, 255, 255)).getRGB());
            }

            this.textColor.setDirection(this.isFocused() ? Direction.BACKWARDS : Direction.FORWARDS);
            if (this.backgroundText != null) {
                Color backgroundTextColor = this.bctColor;
                if (this.backgroundText.equals("Search")) {
                    RenderUtil.drawImage(new ResourceLocation("untitled/icon/search.png"), xPos + 1.5F, this.yPosition + 16f, 16, 16, backgroundTextColor);
                    xPos += 13.0F;
                }

                if (this.text.isEmpty() && !this.textColor.finished(Direction.BACKWARDS)) {
                    this.font.drawString(this.backgroundText, xPos, yPos, backgroundTextColor.getRGB());
                }
            }

            int cursorPos = this.cursorPosition - this.lineScrollOffset;
            int selEnd = this.selectionEnd - this.lineScrollOffset;
            String text = this.font.trimStringToWidth(this.text.substring(this.lineScrollOffset), (int) this.getWidth());
            boolean cursorInBounds = cursorPos >= 0 && cursorPos <= text.length();
            boolean canShowCursor = this.isFocused && cursorInBounds;
            float j1 = xPos;
            if (selEnd > text.length()) {
                selEnd = text.length();
            }

            if (!text.isEmpty()) {
                String s1 = cursorInBounds ? text.substring(0, cursorPos) : text;
                j1 = (float) (this.font.drawStringWithShadow(s1, xPos, yPos, textColorWithAlpha.getRGB()) - this.font.getStringWidth(s1));
            }

            boolean cursorEndPos = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            float k1 = j1;
            if (!cursorInBounds) {
                k1 = cursorPos > 0 ? xPos + this.width : xPos;
            } else if (cursorEndPos) {
                k1 = j1--;
            }

            boolean cursorBlink = this.timerUtil.hasTimeElapsed(2000L) || cursorEndPos;
            if (canShowCursor) {
                if (cursorBlink) {
                    if (this.cursorBlinkAnimation.isDone()) {
                        this.cursorBlinkAnimation.changeDirection();
                    }
                } else {
                    this.cursorBlinkAnimation.setDirection(Direction.FORWARDS);
                }

                Gui.drawRect2(k1 + (text.isEmpty() ? 0.0F : 5.0F), yPos - 2.0F, 0.5D, this.font.height(), ColorUtil.applyOpacity(textColorWithAlpha, this.cursorBlinkAnimation.getOutput().floatValue()).getRGB());
            }

            if (selEnd != cursorPos) {
                int l1 = (int) (xPos + (float) this.font.getStringWidth(text.substring(0, selEnd)));
                int offset = selEnd > cursorPos ? 2 : 0;
                float widthOffset = selEnd > cursorPos ? 0.5F : 0.0F;
                this.drawSelectionBox(k1 + (float) offset, yPos - 1.0F, (float) l1 + widthOffset, yPos + 1.0F + this.font.height());
            }
        }

    }

    private void drawSelectionBox(float x, float y, float width, float height) {
        float j;
        if (x < width) {
            j = x;
            x = width;
            width = j;
        }

        if (y < height) {
            j = y;
            y = height;
            height = j;
        }

        if (width > this.xPosition + this.width) {
            width = this.xPosition + this.width;
        }

        if (x > this.xPosition + this.width) {
            x = this.xPosition + this.width;
        }

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(5387);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x, height, 0.0D).endVertex();
        worldrenderer.pos(width, height, 0.0D).endVertex();
        worldrenderer.pos(width, y, 0.0D).endVertex();
        worldrenderer.pos(x, y, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }

    public void setMaxStringLength(int len) {
        this.maxStringLength = len;
        if (this.text.length() > len) {
            this.text = this.text.substring(0, len);
        }

    }

    public void setTextColor(Color color) {
        this.focusedTextColor = color;
    }

    public void setDisabledTextColour(Color color) {
        this.unfocusedTextColor = color;
    }

    public float getWidth() {
        boolean flag = this.backgroundText != null && this.backgroundText.equals("Search");
        return this.isDrawingBackground() ? this.width - (float) (flag ? 17 : 4) : this.width;
    }

    public float getRealWidth() {
        return this.isDrawingBackground() ? this.width - 4.0F : this.width;
    }

    public void setSelectionPos(int selectionPos) {
        int i = this.text.length();
        if (selectionPos > i) {
            selectionPos = i;
        }

        if (selectionPos < 0) {
            selectionPos = 0;
        }

        this.selectionEnd = selectionPos;
        if (this.font != null) {
            if (this.lineScrollOffset > i) {
                this.lineScrollOffset = i;
            }

            float j = this.getWidth();
            String s = this.font.trimStringToWidth(this.text.substring(this.lineScrollOffset), (int) j);
            int k = s.length() + this.lineScrollOffset;
            if (selectionPos == this.lineScrollOffset) {
                this.lineScrollOffset -= this.font.trimStringToWidth(this.text, (int) j, true).length();
            }

            if (selectionPos > k) {
                this.lineScrollOffset += selectionPos - k;
            } else if (selectionPos <= this.lineScrollOffset) {
                this.lineScrollOffset -= this.lineScrollOffset - selectionPos;
            }

            this.lineScrollOffset = MathHelper.clamp_int(this.lineScrollOffset, 0, i);
        }

    }

    public boolean getVisible() {
        return this.visible;
    }

    public float getXPosition() {
        return this.xPosition;
    }

    public float getYPosition() {
        return this.yPosition;
    }

    public void setXPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    public void setYPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    public void setFocused(boolean isFocused) {
        this.isFocused = isFocused;
    }

    public boolean isFocused() {
        return this.isFocused;
    }
}

