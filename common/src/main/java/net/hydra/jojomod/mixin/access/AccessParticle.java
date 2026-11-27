package net.hydra.jojomod.mixin.access;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface AccessParticle {
    @Accessor("xo")
    void sba$setPrevX(double value);
    @Accessor("yo")
    void sba$setPrevY(double value);
    @Accessor("zo")
    void sba$setPrevZ(double value);

    @Accessor("x")
    double sba$getX();
    @Accessor("y")
    double sba$getY();
    @Accessor("z")
    double sba$getZ();
}