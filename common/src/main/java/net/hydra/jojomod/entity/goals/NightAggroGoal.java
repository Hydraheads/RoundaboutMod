package net.hydra.jojomod.entity.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class NightAggroGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    // makes mobs work like spiders where they aggro at night
    public NightAggroGoal(Mob mob, Class<T> targetClass) {
        super(mob, targetClass, true);
    }

    // both of the checkers are pretty simple, it just makes them aggro if it isn't bright enough.
    @Override
    public boolean canUse() {
        float brightness = this.mob.getLightLevelDependentMagicValue();
        if (brightness >= 0.5F) {
            return false;
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        float brightness = this.mob.getLightLevelDependentMagicValue();
        if (brightness >= 0.5F) {
            return false;
        }
        return super.canContinueToUse();
    }
}