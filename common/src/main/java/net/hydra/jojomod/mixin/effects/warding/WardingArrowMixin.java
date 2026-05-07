package net.hydra.jojomod.mixin.effects.warding;

import net.hydra.jojomod.event.ModEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Arrow.class)
public class WardingArrowMixin {
    /**Warding negates potion effects from arrows specifically*/
    @Inject(method = "doPostHurtEffects(Lnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$doPostHurtEffects(LivingEntity $$0, CallbackInfo ci) {
        if ($$0.hasEffect(ModEffects.WARDING)) {
            ci.cancel();
            return;
        }
    }

}