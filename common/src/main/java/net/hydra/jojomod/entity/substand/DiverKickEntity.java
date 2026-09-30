package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class DiverKickEntity extends Entity {
    private static final EntityDataAccessor<Direction> FACING = SynchedEntityData.defineId(DiverKickEntity.class, EntityDataSerializers.DIRECTION);
    private static final EntityDataAccessor<Byte> SKIN = SynchedEntityData.defineId(DiverKickEntity.class, EntityDataSerializers.BYTE);

    public static final int MAX_TICKS = 20;

    public DiverKickEntity(EntityType<DiverKickEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.blocksBuilding = false;
    }

    public DiverKickEntity(Level level, BlockPos pos, Direction facing, byte skin) {
        this(ModEntities.DIVER_KICK, level);
        this.setPos(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        this.entityData.set(FACING, facing);
        this.entityData.set(SKIN, skin);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(FACING, Direction.UP);
        this.entityData.define(SKIN, (byte) 0);
    }

    public Direction getFacing() {
        return this.entityData.get(FACING);
    }

    public byte getSkin() {
        return this.entityData.get(SKIN);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount >= MAX_TICKS) {
            this.discard();
        }
    }

    // Gets rid of ANYTHING that might mess with collisions, etc. this is only for animations after all.
    @Override public boolean canCollideWith(Entity other) { return false; }
    @Override public boolean canBeCollidedWith() { return false; }
    @Override public boolean isPickable() { return false; }
    @Override public boolean isAttackable() { return false; }
    @Override public boolean isPushable() { return false; }
    @Override public void push(Entity entity) {}
    @Override public void push(double x, double y, double z) {}
    @Override public boolean hurt(DamageSource source, float amount) { return false; }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Facing")) this.entityData.set(FACING, Direction.from3DDataValue(tag.getInt("Facing")));
        if (tag.contains("Skin")) this.entityData.set(SKIN, tag.getByte("Skin"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Facing", getFacing().get3DDataValue());
        tag.putByte("Skin", getSkin());
    }
}