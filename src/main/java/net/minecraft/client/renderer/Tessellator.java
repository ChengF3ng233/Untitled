package net.minecraft.client.renderer;

import lombok.Getter;
import net.optifine.SmartAnimations;

public class Tessellator {
    /**
     * The static instance of the Tessellator.
     */
    @Getter
    private static final Tessellator instance = new Tessellator(2097152);
    @Getter
    private final WorldRenderer worldRenderer;
    private final WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();

    public Tessellator(int bufferSize) {
        this.worldRenderer = new WorldRenderer(bufferSize);
    }

    /**
     * Draws the data set up in this tessellator and resets the state to prepare for new drawing.
     */
    public void draw() {
        if (this.worldRenderer.animatedSprites != null) {
            SmartAnimations.spritesRendered(this.worldRenderer.animatedSprites);
        }

        this.worldRenderer.finishDrawing();
        this.vboUploader.draw(this.worldRenderer);
    }

}
