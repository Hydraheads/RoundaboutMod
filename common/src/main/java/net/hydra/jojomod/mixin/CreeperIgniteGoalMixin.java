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
public class CreeperIgniteGoalMixin extends Goal {

    @Shadow
    private final Creeper creeper;

    public CreeperIgniteGoalMixin(Creeper creeper) {
        this.creeper = creeper;
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutTick(CallbackInfo ci) {
        if (((StandUser)this.creeper).isDazed()) {
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
