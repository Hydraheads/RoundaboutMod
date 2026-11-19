package net.hydra.jojomod.entity.goals;

import net.hydra.jojomod.entity.D4CCloneEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;

public class D4CCloneLookAtMaster  extends LookAtPlayerGoal {
    private final D4CCloneEntity villager;

    public D4CCloneLookAtMaster(D4CCloneEntity p_25538_) {
        super(p_25538_, Player.class, 8.0F);
        this.villager = p_25538_;
    }

    public boolean canUse() {
        if (this.villager.getInteractedPlayer() != null) {
            this.lookAt = this.villager.getInteractedPlayer();
            return true;
        } else {
            return false;
        }
    }
}