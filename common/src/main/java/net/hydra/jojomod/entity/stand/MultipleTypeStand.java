package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.event.index.OffsetIndex;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class MultipleTypeStand extends StandEntity {
    /**Colony stands (as the community calls them for some reason) fall into this category*/
    protected MultipleTypeStand(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
        setOffsetType(OffsetIndex.LOOSE);
    }

    @Override

    public boolean standHasGravity(){
        return true;
    }

    @Override
    public boolean lockPos(){
        return false;
    }
    @Override
    public boolean isASingularEntity(){
        return false;
    }

    @Override
    public void setUser(LivingEntity StandSet){
        this.User = StandSet;
        int standSetId = -1;
        if (StandSet != null){
            standSetId = StandSet.getId();
        }
        this.entityData.set(USER_ID, standSetId);
    }

    @Override
    public void setFollowing(LivingEntity StandSet){
    }

    @Override
    public boolean isValid(boolean userActive, LivingEntity thisStand, LivingEntity userEntity){
        return userEntity.isAlive() && !userEntity.isRemoved() && (!needsActive() || userActive) && validatePowers(userEntity);
    }
    @Override
    public void handleTickDownIfDupe(LivingEntity thisStand){
        TickDown();
    }
}
