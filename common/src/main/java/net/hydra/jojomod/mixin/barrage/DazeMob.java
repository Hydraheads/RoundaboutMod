package net.hydra.jojomod.mixin.barrage;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value= Mob.class, priority = 100)
public class DazeMob {
    /**Minor code, mobs in a barrage should not be attacking*/
    @Inject(method = "doHurtTarget", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$TryAttack(Entity $$0, CallbackInfoReturnable<Boolean> ci) {
        if (((StandUser) this).roundabout$isDazed() ||
                (((StandUser) this).roundabout$hasAStand() &&
                        ((StandUser) this).roundabout$getStandPowers().disableMobAiAttack()) || ((StandUser) this).roundabout$isRestrained()) {
            ci.setReturnValue(false);
        }
    }
}
