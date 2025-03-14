package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForgeParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Roundabout.MOD_ID);

    public static final RegistryObject<SimpleParticleType> HIT_IMPACT = PARTICLES.register("hit_impact",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> BLOOD = PARTICLES.register("blood",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> BLUE_BLOOD = PARTICLES.register("blue_blood",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> ENDER_BLOOD = PARTICLES.register("ender_blood",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> POINTER = PARTICLES.register("pointer",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> AIR_CRACKLE = PARTICLES.register(
            "air_crackle",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> MENACING = PARTICLES.register(
            "menacing",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> VACUUM = PARTICLES.register(
            "vacuum",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> ORANGE_FLAME = PARTICLES.register(
            "orange_flame",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> FOG_CHAIN = PARTICLES.register(
            "fog_chain",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> WARDEN_CLOCK = PARTICLES.register(
            "warden_clock",
            () -> new SimpleParticleType(true)
    );
}
