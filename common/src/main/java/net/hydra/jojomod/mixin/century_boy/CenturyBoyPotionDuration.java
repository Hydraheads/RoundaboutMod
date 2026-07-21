package net.hydra.jojomod.mixin.century_boy;

import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.Powers20thCenturyBoy;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffectInstance.class)
public abstract class CenturyBoyPotionDuration{

    @Shadow public abstract MobEffect getEffect();

    @Shadow @Final private MobEffect effect;

    @Inject(method = "tick", at=@At("HEAD"), cancellable = true)
    private void pauseDuration(LivingEntity entity, Runnable run, CallbackInfoReturnable<Boolean> cir){
        if (entity instanceof StandUser user && user.roundabout$getStandPowers() instanceof Powers20thCenturyBoy pCB){
            if (pCB.invincibleState){
                if (this.getEffect() != MobEffects.WITHER){
                    cir.setReturnValue(true);
                }
            }
        }
    }
    
}
