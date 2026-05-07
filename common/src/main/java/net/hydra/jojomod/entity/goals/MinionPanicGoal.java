package net.hydra.jojomod.entity.goals;

import net.hydra.jojomod.entity.zombie_minion.BaseMinion;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;

public class MinionPanicGoal extends PanicGoal {
    public MinionPanicGoal(PathfinderMob $$0, double $$1) {
        super($$0, $$1);
    }

    @Override
    protected boolean shouldPanic() {
        return (this.mob instanceof BaseMinion bm && bm.shouldPanic());
    }
}