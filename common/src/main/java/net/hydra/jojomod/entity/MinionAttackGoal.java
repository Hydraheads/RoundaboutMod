package net.hydra.jojomod.entity;

import net.hydra.jojomod.entity.zombie_minion.BaseMinion;
import net.hydra.jojomod.event.index.Tactics;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class MinionAttackGoal extends MeleeAttackGoal {
    BaseMinion corpse;
    public MinionAttackGoal(BaseMinion $$0, double $$1, boolean $$2) {
        super($$0, $$1, $$2);
        this.corpse = $$0;
    }

    @Override
    public void tick() {
        super.tick();
        if(corpse.getMovementTactic() == Tactics.HOLD.id){
            this.mob.getNavigation().stop();
            this.mob.getNavigation().createPath(this.mob,0);
        }
    }
}
