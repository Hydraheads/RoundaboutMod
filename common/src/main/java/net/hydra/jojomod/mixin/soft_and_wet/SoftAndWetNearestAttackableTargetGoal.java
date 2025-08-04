package net.hydra.jojomod.mixin.soft_and_wet;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class SoftAndWetNearestAttackableTargetGoal<T extends LivingEntity> extends TargetGoal {

    /**Soft and Wet blindness bubble functionality*/

    @Inject(method = "findTarget", at = @At(value = "TAIL"))
    protected void roundabout$findTarget(CallbackInfo ci) {
        if (!(this.target instanceof StandEntity)) {
            if (((StandUser)this.mob).roundabout$getEyeSightTaken() != null && mob.getLastHurtByMob() == null){
                this.target = this.mob.level().getNearestEntity(this.mob.level().getEntitiesOfClass(this.targetType, this.getTargetSearchArea(this.getFollowDistance()), (p_148152_) -> {
                    return true;
                }), this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
            }
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    public SoftAndWetNearestAttackableTargetGoal(Mob $$0, boolean $$1) {
        super($$0, $$1);
    }

    @Shadow
    @Final
    protected Class<T> targetType;

    @Shadow
    @Nullable
    protected LivingEntity target;
    @Shadow
    protected TargetingConditions targetConditions;


    @Shadow
    protected AABB getTargetSearchArea(double $$0) {
        return null;
    }
}
