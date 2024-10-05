package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SwellGoal.class)
public class ZCreeperIgnite extends Goal {
    /**Minor code for stopping creepers in a barrage*/
    @Shadow
    private final Creeper creeper;

    public ZCreeperIgnite(Creeper creeper) {
        this.creeper = creeper;
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutTick(CallbackInfo ci) {
        if (((StandUser)this.creeper).roundabout$isDazed() ||
                (!((StandUser)this.creeper).roundabout$getStandDisc().isEmpty() &&
                        ((StandUser)this.creeper).roundabout$getStandPowers().disableMobAiAttack())){
           this.creeper.setSwellDir(-1);
           ci.cancel();
        }
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.creeper.getTarget();
        return this.creeper.getSwellDir() > 0 || livingEntity != null && this.creeper.distanceToSqr(livingEntity) < 9.0;
    }
}
