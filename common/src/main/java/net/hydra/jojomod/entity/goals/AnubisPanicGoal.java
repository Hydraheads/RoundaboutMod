package net.hydra.jojomod.entity.goals;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;

public class AnubisPanicGoal extends PanicGoal {
    public AnubisPanicGoal(PathfinderMob $$0, double $$1) {
        super($$0, $$1);
    }

    @Override
    protected boolean shouldPanic() {
        return ((StandUser)this.mob).roundabout$getStandPowers() instanceof PowersAnubis;
    }
}
