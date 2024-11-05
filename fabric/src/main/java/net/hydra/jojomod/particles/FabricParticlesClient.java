package net.hydra.jojomod.particles;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.client.particle.ExplodeParticle;

public class FabricParticlesClient {
    public static void registerClientParticles(){
        ParticleFactoryRegistry.getInstance().register(FabricParticles.HIT_IMPACT, ExplodeParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.BLOOD, BloodParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.BLUE_BLOOD, BloodParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.ENDER_BLOOD, BloodParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.AIR_CRACKLE, AirCrackleParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.MENACING, MenacingParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.VACUUM, VacuumParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.FOG_CHAIN, VacuumParticle.Provider::new);
    }
}
