package net.hydra.jojomod.mixin.century_boy;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.Powers20thCenturyBoy;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffect.class)
public abstract class CenturyBoyPotionHeal {

    @Shadow public abstract boolean isDurationEffectTick(int $$0, int $$1);

    @Inject(method = "applyEffectTick", at=@At("HEAD"), cancellable = true)
    private void cancelRegen(LivingEntity entity, int $$1, CallbackInfo ci){
        MobEffect effect = (MobEffect) (Object) this;

        if (entity instanceof StandUser user && user.roundabout$getStandPowers() instanceof Powers20thCenturyBoy pcb) {
            if (pcb.invincibleState) {
                if (effect != MobEffects.WITHER)
                    ci.cancel();
            }
        }
    }
}

