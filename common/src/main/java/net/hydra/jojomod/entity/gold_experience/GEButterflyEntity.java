package net.hydra.jojomod.entity.gold_experience;

import net.hydra.jojomod.entity.mobs.Butterfly;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class GEButterflyEntity extends Butterfly implements GELifeform {
    private boolean isReflectingDamage = false;
    @Nullable
    private UUID creatorUUID;
    private int lifespan = 600;

    private static final EntityDataAccessor<ItemStack> DATA_SOURCE_ITEM =
            SynchedEntityData.defineId(GEButterflyEntity.class, EntityDataSerializers.ITEM_STACK);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SOURCE_ITEM, ItemStack.EMPTY);
    }

    @Nullable
    private Vec3 targetPos = null;
    @Nullable
    private BlockPos targetBlockPos = null;
    @Nullable
    private Direction targetHitFace = null;

    public GEButterflyEntity(EntityType<? extends Butterfly> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        // lobtomize the butterfih
    }

    @Override
    @Nullable
    public UUID getCreatorUUID() {
        return this.creatorUUID;
    }

    @Override
    public void setCreatorUUID(@Nullable UUID uuid) {
        this.creatorUUID = uuid;
    }

    @Override
    @Nullable
    public LivingEntity getCreator() {
        if (this.creatorUUID != null && this.level() instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(this.creatorUUID);
            if (entity instanceof LivingEntity living) {
                return living;
            }
        }
        return null;
    }

    @Override
    public void setCreator(@Nullable LivingEntity creator) {
        this.creatorUUID = (creator != null) ? creator.getUUID() : null;
        if (creator != null && !this.level().isClientSide()) {
            Vec3 eyePos = creator.getEyePosition(1.0F);
            Vec3 lookVec = creator.getLookAngle();
            Vec3 reachVec = eyePos.add(lookVec.x * 100.0D, lookVec.y * 100.0D, lookVec.z * 100.0D);

            BlockHitResult result = this.level().clip(new ClipContext(
                    eyePos, reachVec,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    creator
            ));

            if (result.getType() != HitResult.Type.MISS) {
                this.targetBlockPos = result.getBlockPos();
                this.targetHitFace = result.getDirection();
                this.targetPos = Vec3.atCenterOf(this.targetBlockPos.relative(this.targetHitFace));
            } else {
                this.targetBlockPos = null;
                this.targetHitFace = null;
                this.targetPos = reachVec;
            }
        }
    }

    @Nullable
    public BlockPos getTargetBlockPos() {
        return this.targetBlockPos;
    }

    @Nullable
    public Direction getTargetHitFace() {
        return this.targetHitFace;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.lifespan > 0) {
                this.lifespan--;
                gotoTarget();
            } else {
                this.die(this.damageSources().generic());
            }
        }
    }

    private void gotoTarget() {
        if (this.horizontalCollision || this.verticalCollision) {
            this.die(this.damageSources().generic());
            return;
        }

        if (this.targetPos != null) {
            Vec3 toTarget = this.targetPos.subtract(this.position());
            double distSq = toTarget.lengthSqr();

            if (distSq <= 0.35D) {
                this.setPos(this.targetPos.x, this.targetPos.y, this.targetPos.z);
                this.die(this.damageSources().generic());
                return;
            }

            double speed = Math.min(0.45D, Math.sqrt(distSq));
            Vec3 flightDir = toTarget.normalize().scale(speed);
            this.setDeltaMovement(flightDir);
            this.hasImpulse = true;

            this.lookAt(EntityAnchorArgument.Anchor.EYES, this.targetPos);
        } else {
            this.setDeltaMovement(Vec3.ZERO);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.creatorUUID != null) {
            tag.putUUID("CreatorUUID", this.creatorUUID);
        }
        tag.putInt("GELifespan", this.lifespan);
        if (!this.getSourceItem().isEmpty()) {
            tag.put("GESourceItem", this.getSourceItem().save(new CompoundTag()));
        }
        if (this.targetPos != null) {
            tag.putDouble("TargetX", this.targetPos.x);
            tag.putDouble("TargetY", this.targetPos.y);
            tag.putDouble("TargetZ", this.targetPos.z);
        }
        if (this.targetBlockPos != null) {
            tag.putInt("TargetBlockX", this.targetBlockPos.getX());
            tag.putInt("TargetBlockY", this.targetBlockPos.getY());
            tag.putInt("TargetBlockZ", this.targetBlockPos.getZ());
        }
        if (this.targetHitFace != null) {
            tag.putInt("TargetHitFace", this.targetHitFace.get3DDataValue());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("CreatorUUID")) {
            this.creatorUUID = tag.getUUID("CreatorUUID");
        }
        if (tag.contains("GELifespan")) {
            this.lifespan = tag.getInt("GELifespan");
        }
        if (tag.contains("GESourceItem")) {
            this.setSourceItem(ItemStack.of(tag.getCompound("GESourceItem")));
        }
        if (tag.contains("TargetX") && tag.contains("TargetY") && tag.contains("TargetZ")) {
            this.targetPos = new Vec3(tag.getDouble("TargetX"), tag.getDouble("TargetY"), tag.getDouble("TargetZ"));
        }
        if (tag.contains("TargetBlockX") && tag.contains("TargetBlockY") && tag.contains("TargetBlockZ")) {
            this.targetBlockPos = new BlockPos(tag.getInt("TargetBlockX"), tag.getInt("TargetBlockY"), tag.getInt("TargetBlockZ"));
        }
        if (tag.contains("TargetHitFace")) {
            this.targetHitFace = Direction.from3DDataValue(tag.getInt("TargetHitFace"));
        }
    }

    @Override
    public boolean isReflectingDamage() { return isReflectingDamage; }
    @Override
    public void setReflectingDamage(boolean reflecting) { this.isReflectingDamage = reflecting; }
    @Override
    public int getLifespan() { return lifespan; }
    @Override
    public void setLifespan(int ticks) { this.lifespan = ticks; }
    @Override
    public void setSourceItem(ItemStack stack) {this.entityData.set(DATA_SOURCE_ITEM, stack);}
    @Override
    public ItemStack getSourceItem() {return this.entityData.get(DATA_SOURCE_ITEM);}
}