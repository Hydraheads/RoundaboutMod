package net.hydra.jojomod.entity.corpses;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.index.Tactics;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

public class CorpseTargetGoal extends TargetGoal {
    private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
    private static final int ALERT_RANGE_Y = 10;
    private boolean alertSameType;
    private int timestamp;
    private final Class<?>[] toIgnoreDamage;
    @Nullable
    private Class<?>[] toIgnoreAlert;

    public CorpseTargetGoal(FallenMob $$0, Class<?>... $$1) {
        super($$0, true);
        this.toIgnoreDamage = $$1;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
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

    public CorpseTargetGoal setAlertOthers(Class<?>... $$0) {
        this.alertSameType = true;
        this.toIgnoreAlert = $$0;
        return this;
    }

    @Override
    public void start() {
        if (mob instanceof FallenMob fm){
            if (fm.getActivated() && fm.getTargetTactic() != Tactics.PEACEFUL.id){
                this.mob.setTarget(fm.corpseTarget);
            } else {
                this.mob.setTarget(null);
            }

        }
        this.targetMob = this.mob.getTarget();
        this.timestamp = this.mob.getLastHurtByMobTimestamp();
        this.unseenMemoryTicks = 300;

        super.start();
    }


    protected void alertOther(Mob $$0, LivingEntity $$1) {
        $$0.setTarget($$1);
    }
}
