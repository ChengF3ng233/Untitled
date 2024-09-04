package net.minecraft.client.gui;

import cn.feng.untitled.module.impl.render.GUI;
import cn.feng.untitled.ui.font.awt.AWTFontLoader;
import com.google.common.collect.Lists;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.List;

public class GuiNewChat extends Gui {
    private static final Logger logger = LogManager.getLogger();
    private final Minecraft mc;
    @Getter
    private final List<String> sentMessages = Lists.newArrayList();
    private final List<ChatLine> chatLines = Lists.newArrayList();
    private final List<ChatLine> drawnChatLines = Lists.newArrayList();
    private int scrollPos;
    private boolean isScrolled;

    public GuiNewChat(Minecraft mcIn) {
        this.mc = mcIn;
    }

    public static int calculateChatboxWidth(float scale) {
        int i = 320;
        int j = 40;
        return MathHelper.floor_float(scale * (float) (i - j) + (float) j);
    }

    public static int calculateChatboxHeight(float scale) {
        int i = 180;
        int j = 20;
        return MathHelper.floor_float(scale * (float) (i - j) + (float) j);
    }

    public void drawChat(int updateCounter) {
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int totalLines = this.getLineCount();
            boolean isChatOpen = false;
            int visibleLines = 0;
            int totalChatLines = this.drawnChatLines.size();
            float chatOpacity = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;

            if (totalChatLines > 0) {
                if (this.getChatOpen()) {
                    isChatOpen = true;
                }

                float chatScale = this.getChatScale();
                int chatWidth = MathHelper.ceiling_float_int((float) this.getChatWidth() / chatScale);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0F, 20.0F, 0.0F);
                GlStateManager.scale(chatScale, chatScale, 1.0F);

                for (int currentLineIndex = 0; currentLineIndex + this.scrollPos < totalChatLines && currentLineIndex < totalLines; ++currentLineIndex) {
                    ChatLine chatLine = this.drawnChatLines.get(currentLineIndex + this.scrollPos);

                    if (chatLine != null) {
                        int timeSinceUpdate = updateCounter - chatLine.getUpdatedCounter();

                        if (timeSinceUpdate < 200 || isChatOpen) {
                            double fadeFactor = (double) timeSinceUpdate / 200.0D;
                            fadeFactor = 1.0D - fadeFactor;
                            fadeFactor = fadeFactor * 10.0D;
                            fadeFactor = MathHelper.clamp_double(fadeFactor, 0.0D, 1.0D);
                            fadeFactor = fadeFactor * fadeFactor;
                            int alpha = (int) (255.0D * fadeFactor);

                            if (isChatOpen) {
                                alpha = 255;
                            }

                            alpha = (int) ((float) alpha * chatOpacity);
                            ++visibleLines;

                            if (alpha > 3) {
                                int startX = 0;
                                int startY = -currentLineIndex * 9;
                                if (GUI.renderChatRect.getValue()) {
                                    drawRect(startX, startY - 9, startX + chatWidth + 4, startY, alpha / 2 << 24);
                                }
                                String chatText = chatLine.getChatComponent().getFormattedText();
                                GlStateManager.enableBlend();
                                if (GUI.chatFancyFont.getValue()) {
                                    AWTFontLoader.miSans(17).drawString(chatText, (float) startX, (float) (startY - 8), 16777215 + (alpha << 24), true);
                                } else {
                                    this.mc.fontRendererObj.drawStringWithShadow(chatText, (float) startX, (float) (startY - 8), 16777215 + (alpha << 24));
                                }
                                GlStateManager.disableAlpha();
                                GlStateManager.disableBlend();
                            }
                        }
                    }
                }

                if (isChatOpen) {
                    int lineHeight = this.mc.fontRendererObj.FONT_HEIGHT;
                    GlStateManager.translate(-3.0F, 0.0F, 0.0F);
                    int totalHeight = totalChatLines * lineHeight + totalChatLines;
                    int visibleHeight = visibleLines * lineHeight + visibleLines;
                    int scrollPosition = this.scrollPos * visibleHeight / totalChatLines;
                    int scrollBarHeight = visibleHeight * visibleHeight / totalHeight;

                    if (totalHeight != visibleHeight) {
                        int scrollBarColor = scrollPosition > 0 ? 170 : 96;
                        int scrollBarBackground = this.isScrolled ? 13382451 : 3355562;
                        drawRect(0, -scrollPosition, 2, -scrollPosition - scrollBarHeight, scrollBarBackground + (scrollBarColor << 24));
                        drawRect(2, -scrollPosition, 1, -scrollPosition - scrollBarHeight, 13421772 + (scrollBarColor << 24));
                    }
                }

                GlStateManager.popMatrix();
            }
        }
    }


    /**
     * Clears the chat.
     */
    public void clearChatMessages() {
        this.drawnChatLines.clear();
        this.chatLines.clear();
        this.sentMessages.clear();
    }

    public void printChatMessage(IChatComponent chatComponent) {
        this.printChatMessageWithOptionalDeletion(chatComponent, 0);
    }

    /**
     * prints the ChatComponent to Chat. If the ID is not 0, deletes an existing Chat Line of that ID from the GUI
     */
    public void printChatMessageWithOptionalDeletion(IChatComponent chatComponent, int chatLineId) {
        this.setChatLine(chatComponent, chatLineId, this.mc.ingameGUI.getUpdateCounter(), false);
        logger.info("[CHAT] {}", chatComponent.getUnformattedText());
    }

    private void setChatLine(IChatComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly) {
        if (chatLineId != 0) {
            this.deleteChatLine(chatLineId);
        }

        int i = MathHelper.floor_float((float) this.getChatWidth() / this.getChatScale());
        List<IChatComponent> list = GuiUtilRenderComponents.splitText(chatComponent, i, this.mc.fontRendererObj, false, false);
        boolean flag = this.getChatOpen();

        for (IChatComponent ichatcomponent : list) {
            if (flag && this.scrollPos > 0) {
                this.isScrolled = true;
                this.scroll(1);
            }

            this.drawnChatLines.add(0, new ChatLine(updateCounter, ichatcomponent, chatLineId));
        }

        while (this.drawnChatLines.size() > 100) {
            this.drawnChatLines.remove(this.drawnChatLines.size() - 1);
        }

        if (!displayOnly) {
            this.chatLines.add(0, new ChatLine(updateCounter, chatComponent, chatLineId));

            if (!GUI.infinityChat.getValue()) {
                while (this.chatLines.size() > 100) {
                    this.chatLines.remove(this.chatLines.size() - 1);
                }
            }
        }
    }

    public void refreshChat() {
        this.drawnChatLines.clear();
        this.resetScroll();

        for (int i = this.chatLines.size() - 1; i >= 0; --i) {
            ChatLine chatline = this.chatLines.get(i);
            this.setChatLine(chatline.getChatComponent(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
        }
    }

    /**
     * Adds this string to the list of sent messages, for recall using the up/down arrow keys
     *
     * @param message The message to add in the sendMessage List
     */
    public void addToSentMessages(String message) {
        if (this.sentMessages.isEmpty() || !this.sentMessages.get(this.sentMessages.size() - 1).equals(message)) {
            this.sentMessages.add(message);
        }
    }

    /**
     * Resets the chat scroll (executed when the GUI is closed, among others)
     */
    public void resetScroll() {
        this.scrollPos = 0;
        this.isScrolled = false;
    }

    /**
     * Scrolls the chat by the given number of lines.
     *
     * @param amount The amount to scroll
     */
    public void scroll(int amount) {
        this.scrollPos += amount;
        int i = this.drawnChatLines.size();

        if (this.scrollPos > i - this.getLineCount()) {
            this.scrollPos = i - this.getLineCount();
        }

        if (this.scrollPos <= 0) {
            this.scrollPos = 0;
            this.isScrolled = false;
        }
    }

    /**
     * Gets the chat component under the mouse
     *
     * @param mouseX The x position of the mouse
     * @param mouseY The y position of the mouse
     */
    public IChatComponent getChatComponent(int mouseX, int mouseY) {
        if (!this.getChatOpen()) {
            return null;
        } else {
            ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            int i = scaledresolution.getScaleFactor();
            float f = this.getChatScale();
            int j = mouseX / i - 3;
            int k = mouseY / i - 27;
            j = MathHelper.floor_float((float) j / f);
            k = MathHelper.floor_float((float) k / f);

            if (j >= 0 && k >= 0) {
                int l = Math.min(this.getLineCount(), this.drawnChatLines.size());

                if (j <= MathHelper.floor_float((float) this.getChatWidth() / this.getChatScale()) && k < this.mc.fontRendererObj.FONT_HEIGHT * l + l) {
                    int i1 = k / this.mc.fontRendererObj.FONT_HEIGHT + this.scrollPos;

                    if (i1 >= 0 && i1 < this.drawnChatLines.size()) {
                        ChatLine chatline = this.drawnChatLines.get(i1);
                        int j1 = 0;

                        for (IChatComponent ichatcomponent : chatline.getChatComponent()) {
                            if (ichatcomponent instanceof ChatComponentText) {
                                j1 += this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText) ichatcomponent).getChatComponentText_TextValue(), false));

                                if (j1 > j) {
                                    return ichatcomponent;
                                }
                            }
                        }
                    }

                    return null;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    /**
     * Returns true if the chat GUI is open
     */
    public boolean getChatOpen() {
        return this.mc.currentScreen instanceof GuiChat;
    }

    /**
     * finds and deletes a Chat line by ID
     *
     * @param id The ChatLine's id to delete
     */
    public void deleteChatLine(int id) {
        Iterator<ChatLine> iterator = this.drawnChatLines.iterator();

        while (iterator.hasNext()) {
            ChatLine chatline = iterator.next();

            if (chatline.getChatLineID() == id) {
                iterator.remove();
            }
        }

        iterator = this.chatLines.iterator();

        while (iterator.hasNext()) {
            ChatLine chatline1 = iterator.next();

            if (chatline1.getChatLineID() == id) {
                iterator.remove();
                break;
            }
        }
    }

    public int getChatWidth() {
        return calculateChatboxWidth(this.mc.gameSettings.chatWidth);
    }

    public int getChatHeight() {
        return calculateChatboxHeight(this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
    }

    /**
     * Returns the chatscale from mc.gameSettings.chatScale
     */
    public float getChatScale() {
        return this.mc.gameSettings.chatScale;
    }

    public int getLineCount() {
        return this.getChatHeight() / 9;
    }
}
