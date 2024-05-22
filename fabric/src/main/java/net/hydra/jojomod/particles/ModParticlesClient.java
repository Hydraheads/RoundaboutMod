package net.hydra.jojomod.particles;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.ExplodeParticle;

public class ModParticlesClient {
    public static void registerClientParticles(){
        ParticleFactoryRegistry.getInstance().register(ModParticles.HIT_IMPACT, ExplodeParticle.Provider::new);
    }
}
