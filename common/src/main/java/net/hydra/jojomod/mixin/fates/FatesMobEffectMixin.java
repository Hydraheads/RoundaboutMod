package net.hydra.jojomod.mixin.fates;

import net.hydra.jojomod.event.index.FateTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEffect.class)
public class FatesMobEffectMixin {

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
