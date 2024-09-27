package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.scores.Team;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

@Mixin(PanicGoal.class)
public abstract class ZPanicGoal extends Goal {

    @Shadow
    @Final
    protected PathfinderMob mob;
    @Final
    private static TargetingConditions roundabout$HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

    @Unique
    @Final
    private static int roundabout$ALERT_RANGE_Y = 10;
    @Unique
    private boolean roundabout$alertSameType;
    @Unique
    private int roundabout$timestamp;

    @Unique
    @Final
    private static int roundabout$EMPTY_REACH_CACHE = 0;
    @Unique
    @Final
    private static int roundabout$CAN_REACH_CACHE = 1;
    @Unique
    @Final
    private static int roundabout$CANT_REACH_CACHE = 2;
    @Unique
    @Final
    protected Mob roundabout$mob;
    @Final
    protected boolean mustSee;
    @Final
    private boolean mustReach;
    @Unique
    private int roundabout$reachCache;
    @Unique
    private int roundabout$reachCacheTime;
    @Unique
    private int roundabout$unseenTicks;
    @Unique
    @Nullable
    protected LivingEntity roundabout$targetMob;
    @Unique
    protected int roundabout$unseenMemoryTicks = 60;
    @Unique
    @Nullable
    private Class<?>[] roundabout$toIgnoreAlert;



    @Inject(method = "canContinueToUse", at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$canContinueToUse(CallbackInfoReturnable<Boolean> cir) {
        if (!((StandUser)this.mob).roundabout$getStandDisc().isEmpty()) {
            cir.setReturnValue(false);
            LivingEntity $$0 = this.mob.getTarget();
            if ($$0 == null) {
                $$0 = this.roundabout$targetMob;
            }

            if ($$0 == null) {
                cir.setReturnValue(false);
            } else if (!this.mob.canAttack($$0)) {
                cir.setReturnValue(false);
            } else {
                Team $$1 = this.mob.getTeam();
                Team $$2 = $$0.getTeam();
                if ($$1 != null && $$2 == $$1) {
                } else {
                    double $$3 = this.roundabout$getFollowDistance();
                    if (this.mob.distanceToSqr($$0) > $$3 * $$3) {
                        cir.setReturnValue(false);
                    } else {
                        if (this.mustSee) {
                            if (this.mob.getSensing().hasLineOfSight($$0)) {
                                this.roundabout$unseenTicks = 0;
                            } else if (++this.roundabout$unseenTicks > reducedTickDelay(this.roundabout$unseenMemoryTicks)) {
                                cir.setReturnValue(false);
                            }
                        }

                        this.mob.setTarget($$0);
                        cir.setReturnValue(true);
                    }
                }
            }
        }
    }
    @Inject(method = "canUse", at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$canUse(CallbackInfoReturnable<Boolean> cir) {
        if (!((StandUser)this.mob).roundabout$getStandDisc().isEmpty()){
            int $$0 = this.mob.getLastHurtByMobTimestamp();
            LivingEntity $$1 = this.mob.getLastHurtByMob();
            if ($$0 != this.roundabout$timestamp && $$1 != null) {
                if ($$1.getType() == EntityType.PLAYER && this.mob.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                    cir.setReturnValue(false);
                } else {
                    cir.setReturnValue(this.roundabout$canAttack($$1, roundabout$HURT_BY_TARGETING));
                }
            } else {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "start", at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$start(CallbackInfo ci) {
        if (!((StandUser)this.mob).roundabout$getStandDisc().isEmpty()){
            this.mob.setTarget(this.mob.getLastHurtByMob());
            this.roundabout$targetMob = this.mob.getTarget();
            this.roundabout$timestamp = this.mob.getLastHurtByMobTimestamp();
            this.roundabout$unseenMemoryTicks = 300;
            ((IMob)this.mob).roundabout$resetAtkCD();
            ci.cancel();
        }
        this.roundabout$reachCache = 0;
        this.roundabout$reachCacheTime = 0;
        this.roundabout$unseenTicks = 0;
    }

    @Inject(method = "stop", at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$stop(CallbackInfo ci) {
        if (!((StandUser)this.mob).roundabout$getStandDisc().isEmpty()){
            this.mob.setTarget(null);
            this.roundabout$targetMob = null;
            ci.cancel();
        }
    }

    @Unique
    private boolean roundabout$canReach(LivingEntity $$0) {
        this.roundabout$reachCacheTime = reducedTickDelay(10 + this.mob.getRandom().nextInt(5));
        Path $$1 = this.mob.getNavigation().createPath($$0, 0);
        if ($$1 == null) {
            return false;
        } else {
            Node $$2 = $$1.getEndNode();
            if ($$2 == null) {
                return false;
            } else {
                int $$3 = $$2.x - $$0.getBlockX();
                int $$4 = $$2.z - $$0.getBlockZ();
                return (double)($$3 * $$3 + $$4 * $$4) <= 2.25;
            }
        }
    }

    @Unique
    protected boolean roundabout$canAttack(@Nullable LivingEntity $$0, TargetingConditions $$1) {
        if ($$0 == null) {
            return false;
        } else if (!$$1.test(this.mob, $$0)) {
            return false;
        } else if (!this.mob.isWithinRestriction($$0.blockPosition())) {
            return false;
        } else {
            if (this.mustReach) {
                if (--this.roundabout$reachCacheTime <= 0) {
                    this.roundabout$reachCache = 0;
                }

                if (this.roundabout$reachCache == 0) {
                    this.roundabout$reachCache = this.roundabout$canReach($$0) ? 1 : 2;
                }

                if (this.roundabout$reachCache == 2) {
                    return false;
                }
            }

            return true;
        }
    }

    @Unique
    protected double roundabout$getFollowDistance() {
        return this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
    }
}
