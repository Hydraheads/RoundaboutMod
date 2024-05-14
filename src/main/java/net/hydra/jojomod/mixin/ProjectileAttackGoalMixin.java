package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileAttackGoal.class)
public class ProjectileAttackGoalMixin {
    /**This Should prevent repeated crossbow charging on barrage*/
    @Shadow
    private final MobEntity mob;

    public ProjectileAttackGoalMixin(MobEntity mob) {
        this.mob = mob;
    }


    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutTick(CallbackInfo ci) {
        if (((StandUser)mob).isDazed()) {
            ci.cancel();
        }
    }
}
