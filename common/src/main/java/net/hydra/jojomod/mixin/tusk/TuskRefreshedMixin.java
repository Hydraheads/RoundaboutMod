package net.hydra.jojomod.mixin.tusk;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersTusk;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffectInstance.class)
public abstract class TuskRefreshedMixin {

    @Shadow
    @Final
    private MobEffect effect;

    @Shadow
    protected abstract int tickDownDuration();

    @Inject(method = "tick",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;tickDownDuration()I"))
    private void roundabout$tickRefreshed(LivingEntity $$0, Runnable $$1, CallbackInfoReturnable<Boolean> cir) {
        if (this.effect.equals(ModEffects.SWITCH)) {
            StandUser SU = (StandUser) $$0;
            if (SU.roundabout$getStandPowers() instanceof PowersTusk PT && PT.getUsedNails() > 0) {
                final int bonus = 14;
                PT.tickNails(bonus);
                for(int i=0;i<bonus;i++) {this.tickDownDuration();}
            }
        }
    }
}
