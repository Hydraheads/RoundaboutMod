package net.hydra.jojomod.particles;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.client.particle.ExplodeParticle;
import net.minecraft.client.particle.FlameParticle;

public class FabricParticlesClient {
    public static void registerClientParticles(){
        ParticleFactoryRegistry.getInstance().register(FabricParticles.HIT_IMPACT, ExplodeParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.BLOOD, BloodParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.BLUE_BLOOD, BloodParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.ENDER_BLOOD, BloodParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.POINTER, PointerParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.AIR_CRACKLE, AirCrackleParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.MENACING, MenacingParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.VACUUM, VacuumParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.ORANGE_FLAME, StandFlameParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.BLUE_FLAME, StandFlameParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.PURPLE_FLAME, StandFlameParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.GREEN_FLAME, StandFlameParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.DREAD_FLAME, StandFlameParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.CREAM_FLAME, StandFlameParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.FOG_CHAIN, VacuumParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.WARDEN_CLOCK, WardenClockParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.CINDERELLA_GLOW, CinderellaGlowParticle.CinderellaGlowProvider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.D4C_LINES, D4CLinesParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.PINK_SMOKE, CinderellaSmokeParticle.CosyProvider::new);
    }
}
