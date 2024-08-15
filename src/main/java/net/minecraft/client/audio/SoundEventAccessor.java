package net.minecraft.client.audio;

import lombok.Getter;

public class SoundEventAccessor implements ISoundEventAccessor<SoundPoolEntry> {
    private final SoundPoolEntry entry;
    @Getter
    private final int weight;

    SoundEventAccessor(SoundPoolEntry entry, int weight) {
        this.entry = entry;
        this.weight = weight;
    }

    public SoundPoolEntry cloneEntry() {
        return new SoundPoolEntry(this.entry);
    }
}
