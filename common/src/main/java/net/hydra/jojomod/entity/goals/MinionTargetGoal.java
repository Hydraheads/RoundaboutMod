package net.hydra.jojomod.entity.goals;

import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.zombie_minion.BaseMinion;
import net.hydra.jojomod.event.index.Tactics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.GameRules;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class MinionTargetGoal extends TargetGoal {
    private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
    private static final int ALERT_RANGE_Y = 10;
    private boolean alertSameType;
    private int timestamp;
    private final Class<?>[] toIgnoreDamage;
    @Nullable
    private Class<?>[] toIgnoreAlert;

    public MinionTargetGoal(BaseMinion $$0, Class<?>... $$1) {
        super($$0, true);
        this.toIgnoreDamage = $$1;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        int $$0 = this.mob.getLastHurtByMobTimestamp();
        LivingEntity $$1 = this.mob.getLastHurtByMob();
        if ($$0 != this.timestamp && $$1 != null) {
            if ($$1.getType() == EntityType.PLAYER && this.mob.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                return false;
            } else {
                for (Class<?> $$2 : this.toIgnoreDamage) {
                    if ($$2.isAssignableFrom($$1.getClass())) {
                        return false;
                    }
                }
                return this.canAttack($$1, HURT_BY_TARGETING);
            }
        } else {
            return false;
        }
    }

    public MinionTargetGoal setAlertOthers(Class<?>... $$0) {
        this.alertSameType = true;
        this.toIgnoreAlert = $$0;
        return this;
    }

    @Override
    public void tick() {
        if(mob instanceof BaseMinion fm) {
            Entity target = fm.getTarget();
            if (target != null) {


                if (!fm.getTarget().isAlive()) {
                    stop();
                    fm.setTarget(null);
                    return;
                }
                this.mob.lookAt(target, 30.0f, 30.0f);
            }

        }
        super.tick();
    }
    @Override
    public boolean canContinueToUse() {
        LivingEntity $$0 = this.mob.getTarget();
        if ($$0 == null) {
           this.targetMob = null;
        }
        return super.canContinueToUse();
    }

    @Override
    public void stop() {
        this.mob.getNavigation().createPath(this.mob.getX(),this.mob.getY(),this.mob.getZ(),1);
        this.mob.getNavigation().stop();
        this.mob.setDeltaMovement(0,0,0);
        super.stop();
    }

    @Override
    public void start() {
        this.targetMob = this.mob.getTarget();
        this.timestamp = this.mob.getLastHurtByMobTimestamp();
        this.unseenMemoryTicks = 300;

        super.start();
    }


    protected void alertOther(Mob $$0, LivingEntity $$1) {
        $$0.setTarget($$1);
    }
}
