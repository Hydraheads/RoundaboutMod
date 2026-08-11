package net.hydra.jojomod.mixin.justice;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.entity.navigation.ActiveCloneManager;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.function.Predicate;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class JusticeNearestAttackableTargetGoal<T extends LivingEntity> extends TargetGoal {

    @Shadow
    protected TargetingConditions targetConditions;

    @Shadow
    protected abstract AABB getTargetSearchArea(double d);

    @Shadow
    @Final
    protected Class<T> targetType;

    /**Zombie and Skeleton morphs pacify aggro initially*/
    @Inject(method = "start", at = @At(value = "HEAD"))
    protected void roundabout$start(CallbackInfo ci) {
        if (this.mob instanceof Zombie ZE && target instanceof Player $$0){
            IPlayerEntity ple = ((IPlayerEntity) $$0);
            byte shape = ple.roundabout$getShapeShift();
            ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
            if (shift != ShapeShifts.PLAYER) {
                if (ShapeShifts.isZombie(shift)) {
                    target = null;
                    ZE.setLastHurtByPlayer(null);
                    ZE.setLastHurtByMob(null);
                    ZE.setTarget(null);
                }
            } else {
                //vampires cannot be targeted
                if (FateTypes.isEvil(target)){
                    if (target.getLastHurtMob() instanceof Zombie)
                        return;
                    target = null;
                }
            }
        } else if (this.mob instanceof AbstractSkeleton ZE && target instanceof Player $$0){
            IPlayerEntity ple = ((IPlayerEntity) $$0);
            byte shape = ple.roundabout$getShapeShift();
            ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
            if (shift != ShapeShifts.PLAYER) {
                if (ShapeShifts.isSkeleton(shift)) {
                    target = null;
                    ZE.setLastHurtByPlayer(null);
                    ZE.setLastHurtByMob(null);
                    ZE.setTarget(null);
                }
            }
        }
    }


    /**Skeleton morphs incur wolf anger*/
    @Unique
    boolean roundabout$isAngryAt(LivingEntity $$0) {
        if ($$0 instanceof Player PE){
            if (this.mob.canAttack($$0)) {
                IPlayerEntity ple = ((IPlayerEntity) $$0);
                byte shape = ple.roundabout$getShapeShift();
                ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
                if (shift != ShapeShifts.PLAYER) {
                    if (ShapeShifts.isSkeleton(shift)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Inject(method = "findTarget", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$findTarget2(CallbackInfo ci) {
        if (this.mob instanceof Wolf WE){
            Predicate<LivingEntity> newCond = this::roundabout$isAngryAt;

            TargetingConditions targetConditionsX = TargetingConditions.forCombat().range(this.getFollowDistance()).selector(newCond);
            LivingEntity TG = this.mob.level().getNearestPlayer(targetConditionsX, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
            if (TG != null){
                this.target = TG;
                ci.cancel();
                return;
            }
        }
    }

    @Inject(method = "findTarget", at = @At("TAIL"))
    private void roundabout$considerClone(CallbackInfo ci) {
        if (this.targetType != Player.class) {
            return;
        }
        if (!(mob.getType().builtInRegistryHolder().key().location().getNamespace().contains("aquatic"))) {
            if (this.target == null) {
                CloneEntity nearestClone = ActiveCloneManager.getNearest(mob);
                if (nearestClone != null) {
                    if (mob.getAttributes().hasAttribute(Attributes.FOLLOW_RANGE)) {
                        double followRange = mob.getAttributeValue(Attributes.FOLLOW_RANGE);
                        double followRangeSqr = followRange * followRange;
                        if (nearestClone != null
                                && mob.distanceToSqr(nearestClone) <= followRangeSqr
                                && (target == null
                                || mob.distanceToSqr(nearestClone) < mob.distanceToSqr(target))
                                && mob.hasLineOfSight(nearestClone)) {
                            if (targetConditions.test(this.mob, nearestClone) || (nearestClone.getPlayer() != null &&
                                    targetConditions.test(this.mob, nearestClone.getPlayer()))) {
                                this.target = nearestClone;
                            }
                        }
                    }
                }
            }
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    public JusticeNearestAttackableTargetGoal(Mob $$0, boolean $$1) {
        super($$0, $$1);
    }

    @Shadow
    @Nullable
    protected LivingEntity target;
}
