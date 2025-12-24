package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class MetallicaKnifeEntity extends KnifeEntity {

    private static final EntityDataAccessor<Boolean> WAITING = SynchedEntityData.defineId(MetallicaKnifeEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LAUNCHED = SynchedEntityData.defineId(MetallicaKnifeEntity.class, EntityDataSerializers.BOOLEAN);

    public MetallicaKnifeEntity(EntityType<? extends KnifeEntity> type, Level level) {
        super(type, level);
        this.pickup = Pickup.DISALLOWED;
    }

    public MetallicaKnifeEntity(Level level, LivingEntity shooter) {
        super(ModEntities.METALLICA_KNIFE, level, shooter);

        this.pickup = Pickup.DISALLOWED;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(WAITING, false);
        this.entityData.define(LAUNCHED, false);
    }

    public void setWaiting(boolean wait) {
        this.entityData.set(WAITING, wait);
    }

    public boolean isWaiting() {
        return this.entityData.get(WAITING);
    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 2 && this.entityData.get(LAUNCHED)) {
            this.discard();
        }

        if (isWaiting()) {
            this.setNoGravity(true);
            this.noPhysics = true;

            double bob = Math.sin(this.tickCount * 0.2) * 0.02;

            if (this.tickCount < 5) {
                this.setDeltaMovement(0, 0.4, 0);
            } else {
                this.setDeltaMovement(0, bob, 0);
            }

            Entity owner = this.getOwner();
            if (owner != null) {
                this.setYRot(owner.getYRot());
                this.setXRot(owner.getXRot());

                this.yRotO = this.getYRot();
                this.xRotO = this.getXRot();
            }

            this.setPos(this.getX(), this.getY() + this.getDeltaMovement().y, this.getZ());

            if (this.level().isClientSide) {
                this.level().addParticle(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            }

            if (!this.level().isClientSide && this.tickCount > 600) {
                this.discard();
            }
            return;
        } else {
            this.noPhysics = false;
            this.setNoGravity(false);
        }

        super.tick();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsWaiting", isWaiting());
        tag.putBoolean("Launched", this.entityData.get(LAUNCHED));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setWaiting(tag.getBoolean("IsWaiting"));
        this.entityData.set(LAUNCHED, tag.getBoolean("Launched"));
    }
}