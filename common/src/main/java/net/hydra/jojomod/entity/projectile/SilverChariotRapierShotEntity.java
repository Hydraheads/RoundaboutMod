package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.UnburnableProjectile;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class SilverChariotRapierShotEntity extends AbstractArrow implements UnburnableProjectile {
    private static final EntityDataAccessor<Integer> ROUNDABOUT$BOUNCES = SynchedEntityData.defineId(SilverChariotRapierShotEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> ROUNDABOUT$TYPE = SynchedEntityData.defineId(SilverChariotRapierShotEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> SKIN = SynchedEntityData.defineId(SilverChariotRapierShotEntity.class, EntityDataSerializers.BYTE);

    public SilverChariotRapierShotEntity(EntityType<? extends SilverChariotRapierShotEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public int getBounces() {
        if (this.getEntityData().hasItem(ROUNDABOUT$BOUNCES)) {
            this.getEntityData().get(ROUNDABOUT$BOUNCES);
        }
        return 0;
    }

    public void setBounces(int bounces) {
        if (this.getEntityData().hasItem(ROUNDABOUT$BOUNCES)) {
            this.getEntityData().set(ROUNDABOUT$BOUNCES, bounces);
        }
    }

    public byte getSkin() {
        return this.getEntityData().get(SKIN);
    }
    public void setSkin(byte skin) {
        this.getEntityData().set(SKIN, skin);
    }

    @Override
    public boolean alwaysAccepts() {
        return super.alwaysAccepts();
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        super.onHitBlock($$0);
    }

    public static final byte
                BASE = (byte) 1,
                PLATFORM = (byte) 2;

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(ROUNDABOUT$BOUNCES, 1);
    }
}
