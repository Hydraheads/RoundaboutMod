package net.hydra.jojomod.entity.goals;

import net.hydra.jojomod.entity.corpses.FallenCreeper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Creeper;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class SwellGoalFallen extends Goal {
    private final FallenCreeper creeper;
    @Nullable
    private LivingEntity target;

    public SwellGoalFallen(FallenCreeper $$0) {
        this.creeper = $$0;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity $$0 = this.creeper.getTarget();
        return this.creeper.getSwellDir() > 0 || ($$0 != null && this.creeper.distanceToSqr($$0) < 9.0 && this.creeper.getActivated());
    }

    @Override
    public void start() {
        this.creeper.getNavigation().stop();
        this.target = this.creeper.getTarget();
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.target == null || !this.creeper.getActivated()) {
            this.creeper.setSwellDir(-1);
        } else if (this.creeper.distanceToSqr(this.target) > 49.0) {
            this.creeper.setSwellDir(-1);
        } else if (!this.creeper.getSensing().hasLineOfSight(this.target)) {
            this.creeper.setSwellDir(-1);
        } else {
            this.creeper.setSwellDir(1);
        }
    }
}