package net.hydra.jojomod.particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.hydra.jojomod.RoundaboutMod;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {
    public static final DefaultParticleType HIT_IMPACT = FabricParticleTypes.simple();
    public static void registerParticles(){
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(RoundaboutMod.MOD_ID, "hit_impact"), HIT_IMPACT);
    }
}
