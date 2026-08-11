package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.event.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class HallucinationJumpMixin {
    @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$disableHallucinationJump(CallbackInfo ci) {
        LivingEntity living = (LivingEntity) (Object) this;
        MobEffectInstance effect = living.getEffect(ModEffects.HALLUCINATION);
        if (effect != null && effect.getAmplifier() >= 2) ci.cancel();
    }
}
