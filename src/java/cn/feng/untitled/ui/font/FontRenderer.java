package cn.feng.untitled.ui.font;

import cn.feng.untitled.util.misc.Logger;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;


/**
 * @author ChengFeng
 * @since 2024/7/29
 **/
public class FontRenderer extends Font {
    private static final String ALPHABET = "ABCDEFGHOKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String COLOR_CODE_CHARACTERS = "0123456789abcdefklmnor";
    private static final Color TRANSPARENT_COLOR = new Color(255, 255, 255, 0);
    private static final float SCALE = 0.5F;
    private static final float SCALE_INVERSE = 2.0F;
    private static final char COLOR_INVOKER = '\u00a7';
    private static final int[] COLOR_CODES = new int[32];
    private static final int LATIN_MAX_AMOUNT = 256;
    private static final int INTERNATIONAL_MAX_AMOUNT = 65535;
    private static final int MARGIN_WIDTH = 4;
    private static final int MASK = 255;

    static {
        calculateColorCodes();
    }

    private final java.awt.Font font;
    private final boolean fractionalMetrics;
    private final float fontHeight;
    private final FontCharacter[] defaultCharacters = new FontCharacter[256];
    private final FontCharacter[] internationalCharacters = new FontCharacter['\uffff'];
    private final FontCharacter[] boldCharacters = new FontCharacter[256];
    private boolean antialiasing = true;
    private boolean international = false;

    public FontRenderer(java.awt.Font font, boolean fractionalMetrics, boolean antialiasing, boolean international) {
        this.antialiasing = antialiasing;
        this.font = font;
        this.fractionalMetrics = fractionalMetrics;
        this.fontHeight = (float) (font.getStringBounds("ABCDEFGHOKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new FontRenderContext(new AffineTransform(), antialiasing, fractionalMetrics)).getHeight() / 2.0D);
        this.fillCharacters(this.defaultCharacters, 0);
        this.fillCharacters(this.boldCharacters, 1);
        this.international = international;
        if (this.international) {
            this.fillCharacters(this.internationalCharacters, 0);
        }
    }

    public FontRenderer(java.awt.Font font, boolean fractionalMetrics, boolean antialiasing) {
        this.antialiasing = antialiasing;
        this.font = font;
        this.fractionalMetrics = fractionalMetrics;
        this.fontHeight = (float) (font.getStringBounds("ABCDEFGHOKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new FontRenderContext(new AffineTransform(), antialiasing, fractionalMetrics)).getHeight() / 2.0D);
        this.fillCharacters(this.defaultCharacters, 0);
        this.fillCharacters(this.boldCharacters, 1);
    }

    public FontRenderer(java.awt.Font font, boolean fractionalMetrics) {
        this.font = font;
        this.fractionalMetrics = fractionalMetrics;
        this.fontHeight = (float) (font.getStringBounds("ABCDEFGHOKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new FontRenderContext(new AffineTransform(), true, fractionalMetrics)).getHeight() / 2.0D);
        this.fillCharacters(this.defaultCharacters, 0);
        this.fillCharacters(this.boldCharacters, 1);
    }

    private static void calculateColorCodes() {
        for (int i = 0; i < 32; ++i) {
            int amplifier = (i >> 3 & 1) * 85;
            int red = (i >> 2 & 1) * 170 + amplifier;
            int green = (i >> 1 & 1) * 170 + amplifier;
            int blue = (i & 1) * 170 + amplifier;
            if (i == 6) {
                red += 85;
            }

            if (i >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }

            COLOR_CODES[i] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
        }

    }

    private void fillCharacters(FontCharacter[] characters, int style) {
        java.awt.Font font = this.font.deriveFont(style);
        BufferedImage fontImage = new BufferedImage(1, 1, 2);
        Graphics2D fontGraphics = (Graphics2D) fontImage.getGraphics();
        FontMetrics fontMetrics = fontGraphics.getFontMetrics(font);

        for (int i = 0; i < characters.length; ++i) {
            char character = (char) i;
            Rectangle2D charRectangle = fontMetrics.getStringBounds(character + "", fontGraphics);
            BufferedImage charImage = new BufferedImage(MathHelper.ceiling_float_int((float) charRectangle.getWidth()) + 8, MathHelper.ceiling_float_int((float) charRectangle.getHeight()), 2);
            Graphics2D charGraphics = (Graphics2D) charImage.getGraphics();
            charGraphics.setFont(font);
            int width = charImage.getWidth();
            int height = charImage.getHeight();
            charGraphics.setColor(TRANSPARENT_COLOR);
            charGraphics.fillRect(0, 0, width, height);
            this.setRenderHints(charGraphics);
            charGraphics.drawString(character + "", 4, font.getSize());
            int charTexture = GL11.glGenTextures();
            this.uploadTexture(charTexture, charImage, width, height);
            characters[i] = new FontCharacter(charTexture, (float) width, (float) height);
        }
    }

    private void setRenderHints(Graphics2D graphics) {
        graphics.setColor(Color.WHITE);
        if (this.antialiasing) {
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
    }

    private float getCharWidthFloat(char c) {
        if (c == 167) {
            return -1.0F;
        } else if (c == ' ') {
            return 2.0F;
        } else {
            int var2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c);
            if (c > 0 && var2 != -1) {
                return this.defaultCharacters[var2].getWidth() / 2.0F - 4.0F;
            } else if (c < this.defaultCharacters.length && this.defaultCharacters[c].getWidth() / 2.0F - 4.0F != 0.0F) {
                int var3 = (int) (this.defaultCharacters[c].getWidth() / 2.0F - 4.0F) >>> 4;
                int var4 = (int) (this.defaultCharacters[c].getWidth() / 2.0F - 4.0F) & 15;
                var3 &= 15;
                ++var4;
                return (float) ((var4 - var3) / 2 + 1);
            } else {
                return 0.0F;
            }
        }
    }

    public String trimStringToWidth(String text, int width) {
        return this.trimStringToWidth(text, width, false);
    }

    public String trimStringToWidth(String text, int width, boolean reverse) {
        if (text == null) {
            return "";
        } else {
            StringBuilder buffer = new StringBuilder();
            float lineWidth = 0.0F;
            int offset = reverse ? text.length() - 1 : 0;
            int increment = reverse ? -1 : 1;
            boolean var8 = false;
            boolean var9 = false;

            for (int index = offset; index >= 0 && index < text.length() && lineWidth < (float) width; index += increment) {
                char character = text.charAt(index);
                float charWidth = this.getCharWidthFloat(character);
                if (var8) {
                    var8 = false;
                    if (character != 'l' && character != 'L') {
                        if (character == 'r' || character == 'R') {
                            var9 = false;
                        }
                    } else {
                        var9 = true;
                    }
                } else if (charWidth < 0.0F) {
                    var8 = true;
                } else {
                    lineWidth += charWidth;
                    if (var9) {
                        ++lineWidth;
                    }
                }

                if (lineWidth > (float) width) {
                    break;
                }

                if (reverse) {
                    buffer.insert(0, character);
                } else {
                    buffer.append(character);
                }
            }

            return buffer.toString();
        }
    }

    private void uploadTexture(int texture, BufferedImage image, int width, int height) {
        int[] pixels = image.getRGB(0, 0, width, height, new int[width * height], 0, width);
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(width * height * 4);

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int pixel = pixels[x + y * width];
                byteBuffer.put((byte) (pixel >> 16 & 255));
                byteBuffer.put((byte) (pixel >> 8 & 255));
                byteBuffer.put((byte) (pixel & 255));
                byteBuffer.put((byte) (pixel >> 24 & 255));
            }
        }

        byteBuffer.flip();
        GlStateManager.bindTexture(texture);
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexImage2D(3553, 0, 6408, width, height, 0, 6408, 5121, byteBuffer);
    }

    public int drawString(String text, double x, double y, int color) {
        return this.renderString(text, x, y, color, false);
    }

    public int drawCenteredString(String text, double x, double y, int color) {
        return this.drawString(text, x - (double) (this.getStringWidth(text) >> 1), y, color);
    }

    public int drawRightString(String text, double x, double y, int color) {
        return this.renderString(text, x - (double) this.getStringWidth(text), y, color, false);
    }

    public int drawStringWithShadow(String text, double x, double y, int color) {
        this.renderString(text, x + 0.25D, y + 0.25D, color, true);
        return this.renderString(text, x, y, color, false);
    }

    public void drawCenteredStringWithShadow(String text, float x, float y, int color) {
        this.renderString(text, (double) (x - (float) (this.getStringWidth(text) >> 1)) + 0.25D, (double) y + 0.25D, (new Color(color, true)).getRGB(), true);
        this.renderString(text, x - (float) (this.getStringWidth(text) >> 1), y, color, false);
    }

    public int drawString(String text, double x, double y, int color, boolean shadow) {
        this.renderString(text, x + 0.5, y + 0.5, color, true);
        return this.renderString(text, x, y, color, false);
    }

    private int renderString(String text, double x, double y, int color, boolean shadow) {
        if (!this.international && this.isInternational(text)) {
            return FontLoader.miSans(this.font.getSize()).drawString(text, x, y, color);
        } else {
            FontCharacter[] characterSet = this.international ? this.internationalCharacters : this.defaultCharacters;
            double givenX = x;
            GL11.glPushMatrix();
            GL11.glPushAttrib(1048575);
            GL11.glEnable(3553);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            x -= 2.0D;
            y -= 2.0D;
            x *= 2.0D;
            y *= 2.0D;
            y -= this.fontHeight / 5.0F;
            double startX = x;
            int length = text.length();

            // Change zero alpha to 255
            if ((color & -67108864) == 0) {
                color |= -16777216;
            }

            if (shadow) {
                color = 50;
            }

            float red = (float) (color >> 16 & 255) / 255.0F;
            float blue = (float) (color >> 8 & 255) / 255.0F;
            float green = (float) (color & 255) / 255.0F;
            float alpha = (float) (color >> 24 & 255) / 255.0F;

            GlStateManager.color(red, blue, green, alpha);

            for (int i = 0; i < length; ++i) {
                char character = text.charAt(i);

                try {
                    if (character == '\n') {
                        x = startX;
                        y += this.height() * 2.0F;
                    } else if (character == 167) {
                        int colorIndex = 21;

                        try {
                            colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                        } catch (Exception var19) {
                            var19.printStackTrace();
                        }

                        if (colorIndex < 16) {
                            characterSet = this.international ? this.internationalCharacters : this.defaultCharacters;
                            if (colorIndex < 0) {
                                colorIndex = 15;
                            }

                            if (shadow) {
                                colorIndex += 16;
                            }

                            int colorcode = COLOR_CODES[colorIndex];
                            GlStateManager.color((float) (colorcode >> 16 & 255) / 255.0F, (float) (colorcode >> 8 & 255) / 255.0F, (float) (colorcode & 255) / 255.0F, alpha);
                        } else if (colorIndex == 17) {
                            characterSet = this.international ? this.internationalCharacters : this.boldCharacters;
                        } else {
                            GlStateManager.color((float) (color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F, alpha);
                            characterSet = this.international ? this.internationalCharacters : this.defaultCharacters;
                        }

                        ++i;
                    } else {
                        FontCharacter fontCharacter = characterSet[character];
                        fontCharacter.render((float) x, (float) y);
                        x += fontCharacter.getWidth() - 8.0F;
                    }
                } catch (Exception var20) {
                    Logger.error("Character \"" + character + "\" was out of bounds (" + character + " out of bounds for " + characterSet.length + ")");
                    var20.printStackTrace();
                }
            }

            GL11.glDisable(3042);
            GL11.glDisable(3553);
            GlStateManager.bindTexture(0);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
            return (int) (x - givenX);
        }
    }

    public int getStringWidth(String text) {
        if (!this.international && this.isInternational(text)) {
            return FontLoader.miSans(this.font.getSize()).getStringWidth(text);
        } else {
            FontCharacter[] characterSet = this.international ? this.internationalCharacters : this.defaultCharacters;
            int length = text.length();
            char previousCharacter = '.';
            int width = 0;

            for (int i = 0; i < length; ++i) {
                char character = text.charAt(i);
                if (previousCharacter != 167) {
                    if (character == 167) {
                        int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase().charAt(i + 1));
                        if (index >= 16 && index != 21) {
                            if (index == 17) {
                                characterSet = this.boldCharacters;
                            }
                        } else {
                            characterSet = this.international ? this.internationalCharacters : this.defaultCharacters;
                        }
                    } else if (characterSet.length > character) {
                        width = (int) ((float) width + (characterSet[character].getWidth() - 8.0F));
                    }
                }

                previousCharacter = character;
            }

            return width / 2;
        }
    }

    public float height() {
        return this.fontHeight;
    }

    private boolean isInternational(String text) {
        int highest = 0;

        for (int i = 0; i < text.length(); ++i) {
            if (text.charAt(i) > highest) {
                highest = text.charAt(i);
            }
        }

        return highest >= 256;
    }
}
