package net.hydra.jojomod.mixin.d4c;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TargetGoal.class, priority=100)
public class D4CTargetGoal {
    @Inject(method = "canContinueToUse", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$tick(CallbackInfoReturnable<Boolean> cir) {
        if (mob.getTarget() != null) {
            if (((StandUser) mob.getTarget()).roundabout$isParallelRunning()) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }

    @Inject(method = "canAttack", at = @At("HEAD"), cancellable = true)
    private void roundabout$stopAttack(LivingEntity entity, TargetingConditions conditions, CallbackInfoReturnable<Boolean> cir)
    {
        if (entity != null)
        {
            if (((StandUser)entity).roundabout$isParallelRunning())
            {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    @Shadow
    @Final
    protected Mob mob;
}
