package net.hydra.jojomod.mixin.whitesnake.hallucination;

import net.hydra.jojomod.event.ModEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LivingEntity.class, Player.class})
public abstract class HallucinationDamageMixin {

    @Inject(method = "actuallyHurt", at = @At("HEAD"))
    private void roundaboutWhitesnake$reduceEffectDurations(DamageSource source, float amount,
                                                            CallbackInfo cir) {
        LivingEntity living = (LivingEntity) (Object) this;
        if (amount < 2.0F || living.level().isClientSide()) return;
        roundabout$reduceEffectDuration(living, living.getEffect(ModEffects.HALLUCINATION));
        roundabout$reduceEffectDuration(living, living.getEffect(ModEffects.SLEEPING));
    }

    @Unique
    private static void roundabout$reduceEffectDuration(LivingEntity living, MobEffectInstance effect) {
        if (effect == null || effect.isInfiniteDuration()) return;
        living.removeEffect(effect.getEffect());
        if (effect.getDuration() > 20) {
            living.addEffect(new MobEffectInstance(effect.getEffect(), effect.getDuration() - 20,
                    effect.getAmplifier(), effect.isAmbient(), effect.isVisible(), effect.showIcon()));
        }
    }
}
