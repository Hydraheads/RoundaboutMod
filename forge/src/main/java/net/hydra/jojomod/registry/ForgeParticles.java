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
    public static final RegistryObject<SimpleParticleType> MELTING = PARTICLES.register("melting",
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
    public static final RegistryObject<SimpleParticleType> HEARTBEAT = PARTICLES.register(
            "heartbeat",
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
    public static final RegistryObject<SimpleParticleType> BLUE_SPARKLE = PARTICLES.register(
            "blue_sparkle",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> RED_SPARKLE = PARTICLES.register(
            "red_sparkle",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> ICE_SPARKLE = PARTICLES.register(
            "ice_sparkle",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> PUNCH_IMPACT_A = PARTICLES.register(
            "punch_impact_a",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> PUNCH_MISS = PARTICLES.register(
            "punch_miss",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> PUNCH_IMPACT_B = PARTICLES.register(
            "punch_impact_b",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> PUNCH_IMPACT_C = PARTICLES.register(
            "punch_impact_c",
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
    public static final RegistryObject<SimpleParticleType> CLOCK = PARTICLES.register(
            "clock",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> RED_CLOCK = PARTICLES.register(
            "red_clock",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> BLUE_CLOCK = PARTICLES.register(
            "blue_clock",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> GREEN_CLOCK = PARTICLES.register(
            "green_clock",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> ORANGE_CLOCK = PARTICLES.register(
            "orange_clock",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> TIME_EMBER = PARTICLES.register(
            "time_ember",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> ZAP = PARTICLES.register(
            "zap",
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
    public static final RegistryObject<SimpleParticleType> BLOOD_MIST = PARTICLES.register(
            "blood_mist",
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
    public static final RegistryObject<SimpleParticleType> BABY_CRACKLE = PARTICLES.register(
            "baby_crackle",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> MAGIC_DUST = PARTICLES.register(
            "magic_dust",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> BRIEF_MAGIC_DUST = PARTICLES.register(
            "brief_magic_dust",
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
    public static final RegistryObject<SimpleParticleType> HYPNO_SWIRL = PARTICLES.register(
            "hypno_swirl",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> MOLD_DUST = PARTICLES.register(
            "mold_dust",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> MOLD = PARTICLES.register(
            "mold",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> RAGING_LIGHT = PARTICLES.register(
            "raging_light",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> ALLURING_LIGHT = PARTICLES.register(
            "alluring_light",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> DUST_CRUMBLE = PARTICLES.register(
            "dust_crumble",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> FIRE_CRUMBLE = PARTICLES.register(
            "fire_crumble",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> SOUL_FIRE_CRUMBLE = PARTICLES.register(
            "soul_fire_crumble",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> ROAD_ROLLER_SCRAP = PARTICLES.register(
            "road_roller_scrap",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> ROAD_ROLLER_EXPLOSION = PARTICLES.register(
            "road_roller_explosion",
            () -> new SimpleParticleType(true)
    );
    public static final RegistryObject<SimpleParticleType> ROAD_ROLLER_SMOKE = PARTICLES.register(
            "road_roller_smoke",
            () -> new SimpleParticleType(true)
    );

    public static final RegistryObject<SimpleParticleType> METALLICA_FIELD_PNG = PARTICLES.register("metallica_field_png", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> METALLICA_NAIL = PARTICLES.register("nail",
            () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> METALLICA_RAZOR = PARTICLES.register("razor",
            () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> METALLICA_SCISSORS = PARTICLES.register("scissors",
            () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> METALLICA_A = PARTICLES.register(
            "metallica_a", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> METALLICA_B = PARTICLES.register(
            "metallica_b", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> METALLICA_C = PARTICLES.register(
            "metallica_c", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> METALLICA_D = PARTICLES.register(
            "metallica_d", () -> new SimpleParticleType(false));
}
