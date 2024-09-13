package net.hydra.jojomod.event.powers.stand;

import net.hydra.jojomod.event.powers.StandPowers;
import net.minecraft.world.entity.LivingEntity;

public class PowersStarPlatinum extends StandPowers {
    public PowersStarPlatinum(LivingEntity self) {
        super(self);
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersStarPlatinum(entity);
    }
}
