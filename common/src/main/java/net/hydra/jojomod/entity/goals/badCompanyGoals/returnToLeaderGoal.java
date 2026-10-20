package net.hydra.jojomod.entity.goals.badCompanyGoals;

import net.hydra.jojomod.entity.badCompanyTroops.BCTroop;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class returnToLeaderGoal extends Goal {

    private final BCTroop mob;
    private double wantedX;
    private double wantedY;
    private double wantedZ;
    private final double speedModifier;
    //private final float within;

    public returnToLeaderGoal(BCTroop $$0, double $$1) {
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
                this.wantedX = this.mob.getUser().position().x;
                this.wantedY = this.mob.getUser().position().y;
                this.wantedZ = this.mob.getUser().position().z;
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
        this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
    }
}
