package net.hydra.jojomod.entity.goals;

import net.hydra.jojomod.entity.D4CCloneEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class D4CCloneObeyMasterGoal extends Goal {
    private final D4CCloneEntity mob;

    public D4CCloneObeyMasterGoal(D4CCloneEntity p_25958_) {
        this.mob = p_25958_;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (!this.mob.isAlive()) {
            return false;
        } else if (this.mob.isInWater()) {
            return false;
        } else if (!this.mob.onGround()) {
            return false;
        } else if (this.mob.hurtMarked) {
            return false;
        } else {
            Player player = this.mob.getInteractedPlayer();
            if (player == null) {
                return false;
            } else if (this.mob.distanceToSqr(player) > 16.0D) {
                return false;
            } else {
                return player.containerMenu != null;
            }
        }
    }

    public void start() {
        this.mob.getNavigation().stop();
    }

    public void stop() {
        this.mob.setInteractedPlayer((Player)null);
    }
}