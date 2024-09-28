package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RangedAttackGoal.class)
public class ZProjectileAtk {
    /**Minor code, this Should prevent repeated crossbow charging on barrage*/
    @Shadow
    private final Mob mob;

    public ZProjectileAtk(Mob mob) {
        this.mob = mob;
    }


    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutTick(CallbackInfo ci) {
        if (((StandUser)mob).isDazed() ||
                (!((StandUser)mob).roundabout$getStandDisc().isEmpty() &&
                ((StandUser)mob).getStandPowers().disableMobAiAttack())
    ) {
            ci.cancel();
        }
    }
}
