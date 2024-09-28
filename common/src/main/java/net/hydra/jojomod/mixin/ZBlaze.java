package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Blaze;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/world/entity/monster/Blaze$BlazeAttackGoal")
public abstract class ZBlaze extends Goal {
    /**Minor code for blazes to stop shooting in a barrage*/
    @Shadow
    private final Blaze blaze;

    public ZBlaze(Blaze blaze) {
        this.blaze = blaze;
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutTick(CallbackInfo ci) {
        if (((StandUser)blaze).isDazed() ||
                (!((StandUser)blaze).roundabout$getStandDisc().isEmpty() &&
                        ((StandUser)blaze).getStandPowers().disableMobAiAttack())) {
            super.tick();
            ci.cancel();
        }
    }

}
