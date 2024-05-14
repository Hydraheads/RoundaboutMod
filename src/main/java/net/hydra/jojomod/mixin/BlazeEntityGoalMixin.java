package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.BlazeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/entity/mob/BlazeEntity$ShootFireballGoal")
public class BlazeEntityGoalMixin extends Goal {
    @Shadow
    private final BlazeEntity blaze;

    public BlazeEntityGoalMixin(BlazeEntity blaze) {
        this.blaze = blaze;
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutTick(CallbackInfo ci) {
        if (((StandUser)blaze).isDazed()) {
            super.tick();
            ci.cancel();
        }
    }

    @Override
    public boolean canStart() {
        LivingEntity livingEntity = this.blaze.getTarget();
        return livingEntity != null && livingEntity.isAlive() && this.blaze.canTarget(livingEntity);
    }
}
