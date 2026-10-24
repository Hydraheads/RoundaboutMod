package net.hydra.jojomod.mixin.whitesnake.hallucination;

import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.whitesnake.HallucinationEffect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LivingEntity.class, Player.class})
public abstract class HallucinationDamageMixin {

    @Inject(method = "actuallyHurt", at = @At("HEAD"))
    private void roundaboutWhitesnake$reduceEffectDurations(DamageSource source, float amount,
                                                                  CallbackInfo cir) {
        if (amount < 2.0F || ((LivingEntity) (Object) this).level().isClientSide()) return;
        MobEffectInstance hallucination = ((LivingEntity) (Object) this).getEffect(ModEffects.HALLUCINATION);
        if (hallucination != null) {
            ((LivingEntity) (Object) this).removeEffect(ModEffects.HALLUCINATION);
            if (hallucination.getDuration() > 20) {
                ((LivingEntity) (Object) this).addEffect(HallucinationEffect.createInstance(
                        hallucination.getDuration() - 20, hallucination.getAmplifier()));
            }
        }
        MobEffectInstance sleeping = ((LivingEntity) (Object) this).getEffect(ModEffects.SLEEPING);
        if (sleeping != null) {
            ((LivingEntity) (Object) this).removeEffect(ModEffects.SLEEPING);
            if (sleeping.getDuration() > 20) {
                ((LivingEntity) (Object) this).addEffect(new MobEffectInstance(ModEffects.SLEEPING,
                        sleeping.getDuration() - 20, sleeping.getAmplifier(), sleeping.isAmbient(),
                        sleeping.isVisible(), sleeping.showIcon()));
            }
        }
    }
}
