package net.hydra.jojomod.mixin.justice;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
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
