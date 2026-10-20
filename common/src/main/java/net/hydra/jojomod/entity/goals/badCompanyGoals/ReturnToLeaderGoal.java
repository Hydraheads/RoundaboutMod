package net.hydra.jojomod.entity.goals.badCompanyGoals;

import net.hydra.jojomod.entity.badCompanyTroops.BCTroop;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class ReturnToLeaderGoal extends Goal {

    private final BCTroop mob;
    private final double speedModifier;
    //private final float within;

    public ReturnToLeaderGoal(BCTroop $$0, double $$1) {
        this.mob = $$0;
        this.speedModifier = $$1;
        //this.within = $$2;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        if (this.mob.getUser() == null || !mob.haveToReturn) {
            return false;
        //} else if (this.target.distanceToSqr(this.mob) > (double)(this.within * this.within)) {
        //    return false;
        } else {
            /*Vec3 $$0 = DefaultRandomPos.getPosTowards(this.mob, 16, 7, this.target.position(), (double)((float)Math.PI / 2F));
            if ($$0 == null) {
                return false;
            } else {*/

                return true;
            //}
        }
    }

    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone() && this.mob.getUser().isAlive() ;
    }

    public void stop() {

    }

    public void start() {
        this.mob.getNavigation().moveTo(mob.getUser(), this.speedModifier);
    }

    int ticksToRecalculate = 8;

    @Override
    public void tick(){
        ticksToRecalculate--;
        if (ticksToRecalculate < 0) {
            this.mob.getNavigation().moveTo(mob.getUser(), this.speedModifier);
            ticksToRecalculate = 5;
        }
    }
}
