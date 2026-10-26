package net.hydra.jojomod.entity.goals;

import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Spider;

public class WhitesnakeControlTargetGoal extends NearestAttackableTargetGoal<WhitesnakeEntity> {
    public WhitesnakeControlTargetGoal(Mob mob) {
        super(mob, WhitesnakeEntity.class, 10, true, false,
                target -> !(mob instanceof Drowned) || ((Drowned) mob).okTarget(target));
    }

    @Override
    public boolean canUse() {
        if (mob.getTarget() != null) return false;
        if (mob instanceof Spider && mob.getLightLevelDependentMagicValue() >= 0.5F) return false;
        double followDistance = getFollowDistance();
        if (followDistance <= 0) return false;
        targetConditions.range(followDistance);
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return mob.getTarget() instanceof WhitesnakeEntity && super.canContinueToUse();
    }
}
