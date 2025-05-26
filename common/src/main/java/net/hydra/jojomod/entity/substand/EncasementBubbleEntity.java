package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.access.PenetratableWithProjectile;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersSoftAndWet;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class EncasementBubbleEntity extends Entity implements PenetratableWithProjectile {

    private static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(EncasementBubbleEntity.class, EntityDataSerializers.INT);
    public EncasementBubbleEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    public void setUser(LivingEntity User) {
        standUser = User;
        this.getEntityData().set(USER_ID, User.getId());
        if (!this.level().isClientSide()){
            standUserUUID = User.getUUID();
        }
    }
    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(USER_ID)){
            this.entityData.define(USER_ID, -1);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag var1) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag var1) {
    }

    @Override
    public boolean dealWithPenetration(Entity projectile) {
        popBubble();
        return true;
    }

    @Override
    public boolean hurt(DamageSource $$0, float $$1) {
        popBubble();
        return true;
    }
    public int lifeSpan = 0;

    public LivingEntity standUser;
    public UUID standUserUUID;

    @Override
    public boolean isPickable() {
        return true;
    }
    @Override
    public void tick() {

        if (!this.level().isClientSide()) {
            lifeSpan--;
            if (lifeSpan <= 0 || (this.standUser == null || !(((StandUser) this.standUser).roundabout$getStandPowers() instanceof PowersSoftAndWet))) {
                popBubble();
                return;
            }
        }
        super.tick();
    }

    public int getUserID() {
        return this.getEntityData().get(USER_ID);
    }
    public void setUserID(int idd) {
        this.getEntityData().set(USER_ID, idd);
        if (this.level().getEntity(this.getUserID()) instanceof LivingEntity LE){
            this.standUser = LE;
            if (!this.level().isClientSide()){
                standUserUUID = LE.getUUID();
            }
        }
    }
    public LivingEntity getStandUser(){
        if (standUser != null){
            return standUser;
        } else if (standUserUUID != null && !this.level().isClientSide()){
            Entity ett = ((ServerLevel)this.level()).getEntity(standUserUUID);
            if (ett instanceof LivingEntity lett){
                standUser = lett;
                this.setUserID(lett.getId());
            }
        } else if (this.level().getEntity(this.getUserID()) instanceof LivingEntity LE){
            standUser = LE;
        }
        return standUser;
    }
    protected float getEyeHeight(Pose $$0, EntityDimensions $$1) {
        return 0;
    }
    public void popBubble(){
        this.level().playSound(null, this.blockPosition(), ModSounds.BUBBLE_POP_EVENT,
                SoundSource.PLAYERS, 2F, (float)(0.98+(Math.random()*0.04)));
        if (!this.level().isClientSide()){
            ((ServerLevel) this.level()).sendParticles(ModParticles.BUBBLE_POP,
                    this.getX(), this.getY() + this.getBbHeight(), this.getZ(),
                    1, 0, 0,0, 0.015);
        }
        this.discard();
    }
}
