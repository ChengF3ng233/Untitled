package cn.feng.untitled.util.render.particle;

import cn.feng.untitled.util.MinecraftInstance;
import cn.feng.untitled.util.misc.TimerUtil;

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

    public void renderParticles(long minAppearInterval, long maxAppearInterval, int amount) {
        if (particleList.size() <= amount && timer.hasTimeElapsed(random.nextLong(minAppearInterval, maxAppearInterval))) {
            particleList.add(new Particle());
            timer.reset();
        }

        if (particleList.size() > amount) {
            particleList.remove(particleList.size() - 1);
        }

        particleList.forEach(Particle::render);
    }
}
