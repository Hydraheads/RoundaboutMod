package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.ITargetGoal;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TargetGoal.class)

public class ZTargetGoal implements ITargetGoal {
    @Shadow
    @Final
    protected Mob mob;

    @Shadow
    protected LivingEntity targetMob;

    @Override
    @Unique
    public void roundabout$removeTarget(){
        targetMob = null;
    }

    @Override
    @Unique
    public void roundabout$setTarget(LivingEntity target){
        targetMob = target;
    }
    /**Soft and Wet Plunder sight*/
    @Inject(method = "getFollowDistance", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$getFollowDistance(CallbackInfoReturnable<Double> cir) {
        if (((StandUser)this.mob).roundabout$getEyeSightTaken() != null && mob.getLastHurtByMob() == null){
            cir.setReturnValue((this.mob.getAttributeValue(Attributes.FOLLOW_RANGE)*0.07));
        }
    }

    @Inject(method = "canContinueToUse", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$tick(CallbackInfoReturnable<Boolean> cir) {
        if (mob.getTarget() != null)
        {
            if (((StandUser)mob.getTarget()).roundabout$isParallelRunning())
            {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }

        if (mob instanceof Zombie ZE && targetMob instanceof Player $$0){
            IPlayerEntity ple = ((IPlayerEntity) $$0);
            byte shape = ple.roundabout$getShapeShift();
            ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
            if (shift != ShapeShifts.PLAYER) {
                if (ShapeShifts.isZombie(shift)) {
                    targetMob = null;
                    mob.setTarget(null);
                    mob.setLastHurtByPlayer(null);
                    mob.setLastHurtByMob(null);
                }
            }
        } else if (mob instanceof IronGolem ZE && targetMob instanceof Player $$0){
            IPlayerEntity ple = ((IPlayerEntity) $$0);
            byte shape = ple.roundabout$getShapeShift();
            ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
            if (shift != ShapeShifts.PLAYER) {
                if (ShapeShifts.isVillager(shift)) {
                    targetMob = null;
                    mob.setTarget(null);
                    mob.setLastHurtByPlayer(null);
                    mob.setLastHurtByMob(null);
                }
            }
        } else if (mob instanceof AbstractSkeleton ZE && targetMob instanceof Player $$0){
            IPlayerEntity ple = ((IPlayerEntity) $$0);
            byte shape = ple.roundabout$getShapeShift();
            ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
            if (shift != ShapeShifts.PLAYER) {
                if (ShapeShifts.isSkeleton(shift)) {
                    targetMob = null;
                    mob.setTarget(null);
                    mob.setLastHurtByPlayer(null);
                    mob.setLastHurtByMob(null);
                }
            }
        }
    }

    @Inject(method = "canAttack", at = @At("HEAD"), cancellable = true)
    private void roundabout$stopAttack(LivingEntity entity, TargetingConditions conditions, CallbackInfoReturnable<Boolean> cir)
    {
        if (entity != null)
        {
            if (((StandUser)entity).roundabout$isParallelRunning())
            {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
}
