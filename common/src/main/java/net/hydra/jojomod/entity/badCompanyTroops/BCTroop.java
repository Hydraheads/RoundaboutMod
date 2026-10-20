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
    public boolean hasNoPhysics(){
        return false;
    }

    @Override
    public boolean needsActive(){
        return false;
    }

    @Override
    public void remove(RemovalReason $$0) {
        super.remove($$0);
        if (getUser() != null && ((StandUser)getUser()).roundabout$getStandPowers() instanceof PowersBadCompany PBC) {
            PBC.troopRemoved(this);
        }
    }

    @Override
    public boolean isValid(boolean userActive, LivingEntity thisStand, LivingEntity userEntity){
        if (userEntity != null && ((StandUser)userEntity).roundabout$getStandPowers() instanceof PowersBadCompany PBC) {
            if (!PBC.troopOnList(this)) {
                return false;
            }
        }else {
            return false;
        }

        return userEntity.isAlive() && validatePowers(userEntity);
    }

    public static int getTroopValue() {
        return 1;
    }
}
