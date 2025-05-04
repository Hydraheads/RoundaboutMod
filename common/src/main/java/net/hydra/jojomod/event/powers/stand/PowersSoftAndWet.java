package net.hydra.jojomod.event.powers.stand;

import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.stand.presets.PunchingStand;
import net.minecraft.world.entity.LivingEntity;

public class PowersSoftAndWet extends PunchingStand {
    public PowersSoftAndWet(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersD4C(entity);
    }
}

