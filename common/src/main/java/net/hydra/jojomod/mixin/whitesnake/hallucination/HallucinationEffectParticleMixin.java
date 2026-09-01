package net.hydra.jojomod.mixin.whitesnake.hallucination;

import net.hydra.jojomod.client.HallucinationRenderOffset;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class HallucinationEffectParticleMixin {
    private static final String ADD_PARTICLE = "Lnet/minecraft/world/level/Level;addParticle("
            + "Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V";

    @ModifyArg(method = "tickEffects", at = @At(value = "INVOKE", target = ADD_PARTICLE), index = 1)
    private double roundaboutWhitesnake$offsetEffectParticleX(double x) {
        return x + HallucinationRenderOffset.forEntity((LivingEntity) (Object) this).x;
    }

    @ModifyArg(method = "tickEffects", at = @At(value = "INVOKE", target = ADD_PARTICLE), index = 3)
    private double roundaboutWhitesnake$offsetEffectParticleZ(double z) {
        return z + HallucinationRenderOffset.forEntity((LivingEntity) (Object) this).z;
    }
}
