package net.hydra.jojomod.entity.goals;

import net.hydra.jojomod.entity.D4CCloneEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class D4CMeleeAttackGoal extends MeleeAttackGoal {

    private final D4CCloneEntity mob;

    public D4CMeleeAttackGoal(D4CCloneEntity mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        if (!mob.canFocusOnFighting(mob.getTarget())) {
            return false;
        }

        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (!mob.canFocusOnFighting(mob.getTarget())) {
            return false;
        }

        return super.canContinueToUse();
    }
}