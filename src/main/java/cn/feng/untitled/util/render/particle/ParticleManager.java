package cn.feng.untitled.util.render.particle;

import cn.feng.untitled.util.MinecraftInstance;
import cn.feng.untitled.util.misc.TimerUtil;
import lombok.Setter;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author ChengFeng
 * @since 2024/8/22
 **/
public class ParticleManager extends MinecraftInstance {
    private final List<Particle> particleList = new ArrayList<>();
    private final Random random = new Random();
    private final TimerUtil timer = new TimerUtil();
    @Setter
    private int amount = 0;

    public void renderParticles() {
        if (particleList.size() <= amount && timer.hasTimeElapsed(random.nextLong(1000, 3000))) {
            ScaledResolution sr = new ScaledResolution(mc);
            float startX = random.nextFloat(0, (float) sr.getScaledWidth_double());
            particleList.add(new Particle(startX, random.nextFloat(2.5f, 3f)));
            timer.reset();
        }
        if (particleList.size() > amount) {
            particleList.remove(particleList.size() - 1);
        }
        particleList.forEach(Particle::render);
    }
}
