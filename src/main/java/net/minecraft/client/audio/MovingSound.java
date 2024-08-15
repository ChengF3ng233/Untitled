package net.minecraft.client.audio;

import lombok.Getter;
import net.minecraft.util.ResourceLocation;

@Getter
public abstract class MovingSound extends PositionedSound implements ITickableSound {
    protected boolean donePlaying = false;

    protected MovingSound(ResourceLocation location) {
        super(location);
    }

}
