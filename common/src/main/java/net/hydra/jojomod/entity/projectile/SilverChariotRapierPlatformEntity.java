package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

public class SilverChariotRapierPlatformEntity extends Entity {
    // private static final EntityDataAccessor<Byte> SKIN = SynchedEntityData.defineId(SilverChariotRapierPlatformEntity.class, EntityDataSerializers.BYTE);

    public SilverChariotRapierPlatformEntity(EntityType<? extends SilverChariotRapierPlatformEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public SilverChariotRapierPlatformEntity(Level $$1) {
        this(ModEntities.SILVER_CHARIOT_RAPIER_PLATFORM, $$1);
        this.life = 0;
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

    private int life;

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 600) {
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
