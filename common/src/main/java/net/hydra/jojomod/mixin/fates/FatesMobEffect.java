package net.hydra.jojomod.mixin.fates;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffect.class)
public class FatesMobEffect {

    /**You cannot get hurt while transformed*/
    @Inject(method = "applyEffectTick", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$applyEffectTick(LivingEntity $$0, int $$1, CallbackInfo ci) {
        if (!$$0.level().isClientSide()) {
            if (FateTypes.isVampire($$0)) {
                if (((MobEffect) (Object) this) == MobEffects.HUNGER) {
                    $$0.removeEffect(MobEffects.HUNGER);
                }
            }
        }
    }
}
