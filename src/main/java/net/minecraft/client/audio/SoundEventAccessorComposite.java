package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Random;

public class SoundEventAccessorComposite implements ISoundEventAccessor<SoundPoolEntry> {
    private final List<ISoundEventAccessor<SoundPoolEntry>> soundPool = Lists.newArrayList();
    private final Random rnd = new Random();
    private final ResourceLocation soundLocation;
    private final SoundCategory category;
    private final double eventPitch;
    private final double eventVolume;

    public SoundEventAccessorComposite(ResourceLocation soundLocation, double pitch, double volume, SoundCategory category) {
        this.soundLocation = soundLocation;
        this.eventVolume = volume;
        this.eventPitch = pitch;
        this.category = category;
    }

    public int getWeight() {
        int i = 0;

        for (ISoundEventAccessor<SoundPoolEntry> isoundeventaccessor : this.soundPool) {
            i += isoundeventaccessor.getWeight();
        }

        return i;
    }

    public SoundPoolEntry cloneEntry() {
        int i = this.getWeight();

        if (!this.soundPool.isEmpty() && i != 0) {
            int j = this.rnd.nextInt(i);

            for (ISoundEventAccessor<SoundPoolEntry> isoundeventaccessor : this.soundPool) {
                j -= isoundeventaccessor.getWeight();

                if (j < 0) {
                    SoundPoolEntry soundpoolentry = isoundeventaccessor.cloneEntry();
                    soundpoolentry.setPitch(soundpoolentry.getPitch() * this.eventPitch);
                    soundpoolentry.setVolume(soundpoolentry.getVolume() * this.eventVolume);
                    return soundpoolentry;
                }
            }

            return SoundHandler.missing_sound;
        } else {
            return SoundHandler.missing_sound;
        }
    }

    public void addSoundToEventPool(ISoundEventAccessor<SoundPoolEntry> sound) {
        this.soundPool.add(sound);
    }

    public ResourceLocation getSoundEventLocation() {
        return this.soundLocation;
    }

    public SoundCategory getSoundCategory() {
        return this.category;
    }
}
