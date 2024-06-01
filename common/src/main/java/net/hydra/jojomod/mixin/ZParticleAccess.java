package net.hydra.jojomod.mixin;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface ZParticleAccess {
    @Accessor("xo")
    void setPrevX(double value);
    @Accessor("yo")
    void setPrevY(double value);
    @Accessor("zo")
    void setPrevZ(double value);

    @Accessor("x")
    double getX();
    @Accessor("y")
    double getY();
    @Accessor("z")
    double getZ();
}