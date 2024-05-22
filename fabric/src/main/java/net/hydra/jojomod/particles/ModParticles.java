package net.hydra.jojomod.particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.hydra.jojomod.Roundabout;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class ModParticles {
    public static final SimpleParticleType HIT_IMPACT = FabricParticleTypes.simple();
    public static void registerParticles(){
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, new ResourceLocation(Roundabout.MOD_ID, "hit_impact"), HIT_IMPACT);
    }
}
