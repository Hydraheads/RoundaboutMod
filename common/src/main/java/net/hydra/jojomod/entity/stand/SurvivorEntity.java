package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersSurvivor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class SurvivorEntity extends MultipleTypeStand{
    /**
     * Initialize Stands
     *
     * @param entityType
     * @param world
     */
    protected SurvivorEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    @Override

    public boolean validatePowers(LivingEntity user){
        return (((StandUser)user).roundabout$getStandPowers() instanceof PowersSurvivor);
    }


    @Override
    public boolean needsActive(){
        return false;
    }
}
