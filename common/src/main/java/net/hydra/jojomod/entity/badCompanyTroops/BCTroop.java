package net.hydra.jojomod.entity.badCompanyTroops;

import net.hydra.jojomod.entity.stand.MultipleTypeStand;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersBadCompany;
import net.hydra.jojomod.stand.powers.PowersSurvivor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class BCTroop extends MultipleTypeStand {
    protected BCTroop(EntityType<? extends MultipleTypeStand> entityType, Level world) {
        super(entityType, world);
    }

    public boolean validatePowers(LivingEntity user){
        return (((StandUser)user).roundabout$getStandPowers() instanceof PowersBadCompany);
    }

    @Override
    public boolean needsActive(){
        return false;
    }
}
