package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.client.HallucinationRenderOffset;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class HallucinationEffectParticleMixin {
    //Redirect(method = "tickEffects", at = @At(value = "INVOKE",
    //        target = "Lnet/minecraft/world/level/Level;addParticle("
    //                + "Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private void roundaboutWhitesnake$offsetEffectParticle(Level level, ParticleOptions particle,
                                                            double x, double y, double z,
                                                            double red, double green, double blue) {
        Vec3 offset = HallucinationRenderOffset.forEntity((LivingEntity) (Object) this);
        level.addParticle(particle, x + offset.x, y + offset.y, z + offset.z, red, green, blue);
    }
}
