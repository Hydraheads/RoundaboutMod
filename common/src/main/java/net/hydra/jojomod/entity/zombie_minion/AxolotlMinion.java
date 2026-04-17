package net.hydra.jojomod.entity.zombie_minion;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class AxolotlMinion extends BaseMinion{
    public AxolotlMinion(EntityType<? extends AxolotlMinion> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal(1, new FallenZombieAttackGoal(this, 1.0, true));
        this.addBehaviourGoals();
    }
}
