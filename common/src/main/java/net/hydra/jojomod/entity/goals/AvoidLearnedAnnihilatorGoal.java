package net.hydra.jojomod.entity.goals;

import net.hydra.jojomod.entity.D4CCloneEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class AvoidLearnedAnnihilatorGoal extends Goal {

    private final D4CCloneEntity mob;
    private Entity annihilator;

    public AvoidLearnedAnnihilatorGoal(D4CCloneEntity mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        annihilator = mob.getLearnedAnnihilator();

        if (annihilator == null || !annihilator.isAlive()) {
            return false;
        }

        // Don't interfere with the special panic behavior.
        if (mob.getStrategy() == D4CCloneEntity.STRATEGY_BACKING_AWAY ||
                mob.getStrategy() == D4CCloneEntity.STRATEGY_FLEEING ||
                mob.getStrategy() == D4CCloneEntity.STRATEGY_ACKNOWLEDGING
        || !mob.figuredItOut()) {
            return false;
        }

        return mob.distanceTo(annihilator) < 8.0D;
    }

    @Override
    public boolean canContinueToUse() {
        return annihilator != null
                && annihilator.isAlive()
                && mob.distanceTo(annihilator) < 9.0D;
    }

    @Override
    public void start() {
        avoid();
    }

    @Override
    public void tick() {
        avoid();
    }

    private void avoid() {
        if (annihilator == null) {
            return;
        }

        Vec3 away = mob.position().subtract(annihilator.position());

        if (away.lengthSqr() < 0.001D) {
            return;
        }

        away = away.normalize();

        double distance = mob.distanceTo(annihilator);

        double targetDistance = 8.0D;

        Vec3 target = mob.position().add(
                away.x * (targetDistance - distance),
                0,
                away.z * (targetDistance - distance)
        );

        mob.getNavigation().moveTo(
                target.x,
                target.y,
                target.z,
                1.2D
        );
    }
}
