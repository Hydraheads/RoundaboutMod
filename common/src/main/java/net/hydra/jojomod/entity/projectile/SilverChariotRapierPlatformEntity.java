package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.SilverChariotEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.UUID;

public class SilverChariotRapierPlatformEntity extends Entity {
    // private static final EntityDataAccessor<Byte> SKIN = SynchedEntityData.defineId(SilverChariotRapierPlatformEntity.class, EntityDataSerializers.BYTE);

    public SilverChariotRapierPlatformEntity(EntityType<? extends SilverChariotRapierPlatformEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    // public SilverChariotEntity silverChariot = null;
    private byte skin = 0;

    public byte getSkin() {
        return this.skin;
    }

    public SilverChariotRapierPlatformEntity(Level $$1, byte skin) {
        this(ModEntities.SILVER_CHARIOT_RAPIER_PLATFORM, $$1);
        // this.silverChariot = silverChariot;
        this.skin = skin;
    }

    private static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(SilverChariotRapierPlatformEntity.class, EntityDataSerializers.INT);

    public LivingEntity standUser;
    public UUID standUserUUID;

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

    public LivingEntity getUser(){
        if (this.level().getEntity(this.getUserID()) instanceof LivingEntity LE){
            return LE;
        }
        return null;
    }

    public void setUser(LivingEntity User) {
        standUser = User;
        this.getEntityData().set(USER_ID, User.getId());
        if (!this.level().isClientSide()){
            standUserUUID = User.getUUID();
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

    /*
    public byte getSkin() {
        return this.getEntityData().get(SKIN);
    }

    public void setSkin(byte skin) {
        this.getEntityData().set(SKIN, skin);
    }
     */

    @Override
    public boolean alwaysAccepts() {
        return super.alwaysAccepts();
    }

    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(USER_ID)) {
            this.entityData.define(USER_ID, -1);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
    }

    /*
    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.fixed(1.0F, 0.2F);
    }
     */

    private int life = 0;

    @Override
    public void tick() {
        super.tick();
        this.life += 1;
        if (this.life > 600) {
            this.discard();
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return super.isIgnoringBlockTriggers();
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }
}
