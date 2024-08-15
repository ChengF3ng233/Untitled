package net.minecraft.client.resources.data;

import lombok.Getter;

@Getter
public class AnimationFrame {
    private final int frameIndex;
    private final int frameTime;

    public AnimationFrame(int p_i1307_1_) {
        this(p_i1307_1_, -1);
    }

    public AnimationFrame(int p_i1308_1_, int p_i1308_2_) {
        this.frameIndex = p_i1308_1_;
        this.frameTime = p_i1308_2_;
    }

    public boolean hasNoTime() {
        return this.frameTime == -1;
    }

}
