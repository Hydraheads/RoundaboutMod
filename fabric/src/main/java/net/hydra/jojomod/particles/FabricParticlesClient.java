package net.hydra.jojomod.particles;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.*;

public class FabricParticlesClient {
    public static void registerClientParticles(){
        ParticleFactoryRegistry.getInstance().register(FabricParticles.HIT_IMPACT, ExplodeParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.PUNCH_MISS, PunchMissParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.PUNCH_IMPACT_A, PunchImpactParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.PUNCH_IMPACT_B, PunchImpactParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.PUNCH_IMPACT_C, PunchImpactParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.MELTING, BloodParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.BLOOD, BloodParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.BLUE_BLOOD, BloodParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.ENDER_BLOOD, BloodParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.POINTER, PointerParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.POINTER_SOFT, SmallPointerParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.AIR_CRACKLE, AirCrackleParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.STAR, StarParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.HEART_ATTACK_MINI, HeartAttackMini.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.ENERGY_DISTORTION, EnergyDistortionParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.PURPLE_STAR, StarParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.MENACING, MenacingParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.VACUUM, VacuumParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.ZAP, ZapParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.ORANGE_FLAME, StandFlameParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.BLUE_FLAME, StandFlameParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.PURPLE_FLAME, StandFlameParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.GREEN_FLAME, StandFlameParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.DREAD_FLAME, StandFlameParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.CREAM_FLAME, StandFlameParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.FOG_CHAIN, VacuumParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.BUBBLE_TRAIL, BubbleTrailParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.WARDEN_CLOCK, WardenClockParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.CLOCK, MandomClockParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.BLUE_CLOCK, MandomClockParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.GREEN_CLOCK, MandomClockParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.RED_CLOCK, MandomClockParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.ORANGE_CLOCK, MandomClockParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.TIME_EMBER, TimeEmberParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.CINDERELLA_GLOW, CinderellaGlowParticle.CinderellaGlowProvider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.D4C_LINES, D4CLinesParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.PINK_SMOKE, CinderellaSmokeParticle.CosyProvider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.BUBBLE_POP, SoftBubblePopParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.PLUNDER, PlunderParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.FRICTIONLESS, PlunderParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.EXCLAMATION, ExclamationParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.STITCH, StitchParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.MOLD_DUST, MoldDustParticle.CosyProvider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.MOLD, MoldParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.BABY_CRACKLE, BabyCrackleParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.MAGIC_DUST, MagicDustParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(FabricParticles.BRIEF_MAGIC_DUST, BriefMagicDustParticle.Provider::new);
    }
}
