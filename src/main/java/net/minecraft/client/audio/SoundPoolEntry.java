package net.minecraft.client.audio;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;

public class SoundPoolEntry {
    private final ResourceLocation location;
    @Getter
    private final boolean streamingSound;
    @Getter
    @Setter
    private double pitch;
    @Getter
    @Setter
    private double volume;

    public SoundPoolEntry(ResourceLocation locationIn, double pitchIn, double volumeIn, boolean streamingSoundIn) {
        this.location = locationIn;
        this.pitch = pitchIn;
        this.volume = volumeIn;
        this.streamingSound = streamingSoundIn;
    }

    public SoundPoolEntry(SoundPoolEntry locationIn) {
        this.location = locationIn.location;
        this.pitch = locationIn.pitch;
        this.volume = locationIn.volume;
        this.streamingSound = locationIn.streamingSound;
    }

    public ResourceLocation getSoundPoolEntryLocation() {
        return this.location;
    }

}
