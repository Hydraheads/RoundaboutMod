package net.hydra.jojomod.event.powers.stand;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.StandEntity;
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

    @Override
    public boolean canSummonStand(){
        return true;
    }
    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.STAR_PLATINUM.create(this.getSelf().level());
    }
}
