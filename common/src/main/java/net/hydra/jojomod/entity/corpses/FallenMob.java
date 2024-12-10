package net.hydra.jojomod.entity.corpses;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class FallenMob extends Mob {

    public boolean isActivated = false;
    public int ticksThroughPhases = 0;
    protected FallenMob(EntityType<? extends Mob> $$0, Level $$1) {
        super($$0, $$1);
    }
}
