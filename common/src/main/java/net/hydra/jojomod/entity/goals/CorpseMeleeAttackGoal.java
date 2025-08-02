package net.hydra.jojomod.entity.goals;

import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.event.index.Tactics;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

//It's a bit empty now, but this is in case we end up wanting to apply anything to melee corpses
public class CorpseMeleeAttackGoal extends MeleeAttackGoal {
    FallenMob corpse;
    public CorpseMeleeAttackGoal(FallenMob $$0, double $$1, boolean $$2) {
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
