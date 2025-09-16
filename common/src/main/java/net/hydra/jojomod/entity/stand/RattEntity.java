package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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
            ELDER_GUARDIAN_SKIN = 9,

            FIRE = 81,
            LOADING = 82;

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


    public Vec3 getEyeP(float d) {
        return this.getPosition(d).add(0,0.1,0);
    }


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
    public final AnimationState loading = new AnimationState();

    @Override
    public void setupAnimationStates() {
        if (this.getUser() != null) {
            if (this.getAnimation() == FIRE) {
                this.fire.startIfStopped(this.tickCount);
            } else {
                this.fire.stop();
            }
            if (this.getAnimation() == LOADING) {
                this.loading.startIfStopped(this.tickCount);
            } else {
                this.loading.stop();
            }
        }
        super.setupAnimationStates();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() != null && source.getEntity() != this.getUser()) {
            if (this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersRatt PR) {
                PR.tryPower(PowersRatt.NET_RECALL,true);
                PR.tryPowerPacket(PowersRatt.NET_RECALL);
                this.forceDespawnSet = true;
                PR.setCooldown(PowersRatt.SETPLACE,50);
            }
            return this.getUser().hurt(source, amount);
        }
        return false;
    }

    @Override
    public boolean isAttackable() {return true;}
    @Override
    public boolean isPickable() {return true;}
    @Override
    public boolean skipAttackInteraction(Entity $$0) {return false;}
}

