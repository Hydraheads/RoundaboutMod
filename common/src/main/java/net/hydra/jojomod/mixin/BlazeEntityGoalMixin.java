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
public class BlazeEntityGoalMixin extends Goal {

    @Shadow
    private final Blaze blaze;

    public BlazeEntityGoalMixin(Blaze blaze) {
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
    public boolean canUse() {
        LivingEntity livingEntity = this.blaze.getTarget();
        return livingEntity != null && livingEntity.isAlive() && this.blaze.canAttack(livingEntity);
    }
}
