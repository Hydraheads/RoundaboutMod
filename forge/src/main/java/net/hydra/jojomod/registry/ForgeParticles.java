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
    public static final RegistryObject<SimpleParticleType> POINTER_SOFT = PARTICLES.register("pointer_soft",
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
    public static final RegistryObject<SimpleParticleType> BLUE_FLAME = PARTICLES.register(
            "blue_flame",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> PURPLE_FLAME = PARTICLES.register(
            "purple_flame",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> GREEN_FLAME = PARTICLES.register(
            "green_flame",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> DREAD_FLAME = PARTICLES.register(
            "dread_flame",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> CREAM_FLAME = PARTICLES.register(
            "cream_flame",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> FOG_CHAIN = PARTICLES.register(
            "fog_chain",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> STAR = PARTICLES.register(
            "star",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> HEART_ATTACK_MINI = PARTICLES.register(
            "heart_attack_mini",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> ENERGY_DISTORTION = PARTICLES.register(
            "energy_distortion",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> PURPLE_STAR = PARTICLES.register(
            "purple_star",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> BUBBLE_TRAIL = PARTICLES.register(
            "bubble_trail",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> WARDEN_CLOCK = PARTICLES.register(
            "warden_clock",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> CINDERELLA_GLOW = PARTICLES.register(
            "cinderella_glow",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> PINK_SMOKE = PARTICLES.register(
            "pink_smoke",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> BUBBLE_POP = PARTICLES.register(
            "soft_bubble_pop",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> PLUNDER = PARTICLES.register(
            "plunder",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> FRICTIONLESS = PARTICLES.register(
            "frictionless",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> EXCLAMATION = PARTICLES.register(
            "exclamation",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> D4C_LINES = PARTICLES.register(
            "d4c_lines",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> STITCH = PARTICLES.register(
            "stitch",
            () -> new SimpleParticleType(true)
    );
}
