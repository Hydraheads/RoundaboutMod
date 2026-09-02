package net.hydra.jojomod.mixin.barrage;


import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeapAtTargetGoal.class)
public class DazeLeapAtTargetGoal {
    @Shadow
    @Final
    private Mob mob;

    /**Minor code, mobs in a barrage should not be attacking*/
    @Inject(method = "start()V", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$startLAT(CallbackInfo ci) {
        if (((StandUser) this.mob).roundabout$isDazed() ||
                (((StandUser) this.mob).roundabout$hasAStand() &&
                        ((StandUser) this.mob).roundabout$getStandPowers().disableMobAiAttack()) || ((StandUser) this.mob).roundabout$isRestrained()) {
            ci.cancel();
        }
    }
}
