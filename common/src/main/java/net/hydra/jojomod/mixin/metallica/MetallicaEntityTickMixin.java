package net.hydra.jojomod.mixin.metallica;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MetallicaEntityTickMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void roundabout$metallicaVictimParticles(CallbackInfo ci) {
    }
}