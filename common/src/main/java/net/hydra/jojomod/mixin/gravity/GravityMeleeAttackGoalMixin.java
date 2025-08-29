package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MeleeAttackGoal.class)
public abstract class GravityMeleeAttackGoalMixin {
    @Shadow protected abstract double getAttackReachSqr(LivingEntity livingEntity);

    @Shadow private int ticksUntilNextAttack;

    @Shadow protected abstract void resetAttackCooldown();

    @Shadow @Final protected PathfinderMob mob;

    @Inject(
            method = "checkAndPerformAttack(Lnet/minecraft/world/entity/LivingEntity;D)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void rdbt$checkAndPerformAttack(LivingEntity $$0, double $$1, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection($$0);
        Direction gravityDirection2 = GravityAPI.getGravityDirection(this.mob);
        if (gravityDirection == gravityDirection2)
            return;
        ci.cancel();

        double $$2 = this.getAttackReachSqr($$0);
        if (Math.sqrt($$1) <= (Math.sqrt($$2)+2) && this.ticksUntilNextAttack <= 0) {
            this.resetAttackCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget($$0);
        }
    }
}
