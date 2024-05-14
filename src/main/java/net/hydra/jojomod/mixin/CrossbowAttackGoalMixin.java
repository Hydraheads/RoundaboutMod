package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.ai.goal.CrossbowAttackGoal;
import net.minecraft.entity.mob.HostileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowAttackGoal.class)
public class CrossbowAttackGoalMixin<T extends HostileEntity & CrossbowUser> {
    /**This Should prevent repeated crossbow charging on barrage*/
    @Shadow
    private final T actor;

    public CrossbowAttackGoalMixin(T actor) {
        this.actor = actor;
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutTick(CallbackInfo ci) {
        if (((StandUser)this.actor).isDazed()) {
            ci.cancel();
        }
    }
}
