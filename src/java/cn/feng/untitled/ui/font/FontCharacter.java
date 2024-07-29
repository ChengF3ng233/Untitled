package cn.feng.untitled.ui.font;

import lombok.Getter;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

/**
 * @author ChengFeng
 * @since 2024/7/29
 **/
@Getter
public class FontCharacter {
    private final int texture;
    private final float width;
    private final float height;

    public FontCharacter(int texture, float width, float height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
    }

    public void render(float x, float y) {
        GlStateManager.bindTexture(this.texture);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0F, 0.0F);
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(0.0F, 1.0F);
        GL11.glVertex2f(x, y + this.height);
        GL11.glTexCoord2f(1.0F, 1.0F);
        GL11.glVertex2f(x + this.width, y + this.height);
        GL11.glTexCoord2f(1.0F, 0.0F);
        GL11.glVertex2f(x + this.width, y);
        GL11.glEnd();
    }
}
