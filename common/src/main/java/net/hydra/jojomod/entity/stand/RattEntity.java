package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.Roundabout;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;

public class RattEntity extends StandEntity {
    public RattEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public static final byte
            ANIME_SKIN = 1,
            MANGA_SKIN = 2,
            MELON_SKIN = 3,
            SAND_SKIN = 4,
            AZTEC_SKIN = 5,
            TOWER_SKIN = 6,
            SNOWY_SKIN = 7,
            GUARDIAN_SKIN = 8,

            FIRE = 81,
            FIRE_NO_RECOIL = 82;

    public List<Byte> getSkinList() {
        return Arrays.asList(
                ANIME_SKIN,
                MANGA_SKIN,
                MELON_SKIN,
                SAND_SKIN,
                TOWER_SKIN,
                SNOWY_SKIN,
                GUARDIAN_SKIN
        );
    }

    protected static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(RattEntity.class,
            EntityDataSerializers.INT);

    public LivingEntity Target;
    public LivingEntity getTarget() {
        if (this.level().isClientSide){
            return (LivingEntity) this.level().getEntity(this.entityData.get(TARGET_ID));
        } else {
            if (this.Target != null && this.Target.isRemoved()){
                this.setFollowing(null);
            }
            return this.Target;
        }
    }
    public void setTarget(LivingEntity StandSet){
        this.Target = StandSet;
        int standSetId = -1;
        if (StandSet != null){
            standSetId = StandSet.getId();
        }
        this.entityData.set(TARGET_ID, standSetId);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TARGET_ID, -1);
    }

    @Override
    public boolean forceVisualRotation(){
        return true;
    }

    @Override
    public boolean lockPos(){
        return false;
    }
    @Override
    public boolean hasNoPhysics(){
        return false;
    }

    @Override
    public boolean standHasGravity() {
        return true;
    }


    public final AnimationState fire = new AnimationState();
    public final AnimationState fire_no_recoil = new AnimationState();

    @Override
    public void setupAnimationStates() {
        if (this.getUser() != null) {
            if (this.getAnimation() == FIRE) {
                this.fire.startIfStopped(this.tickCount);
            } else {
                this.fire.stop();
            }
            if (this.getAnimation() == FIRE_NO_RECOIL) {
                this.fire_no_recoil.startIfStopped(this.tickCount);
            } else {
                this.fire_no_recoil.stop();
            }
        }
        super.setupAnimationStates();
    }
}

