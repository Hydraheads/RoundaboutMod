package net.hydra.jojomod.mixin;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface ZParticleAccess {
    @Accessor("xo")
    void roundabout$setPrevX(double value);
    @Accessor("yo")
    void roundabout$setPrevY(double value);
    @Accessor("zo")
    void roundabout$setPrevZ(double value);

    @Accessor("x")
    double roundabout$getX();
    @Accessor("y")
    double roundabout$getY();
    @Accessor("z")
    double roundabout$getZ();
}