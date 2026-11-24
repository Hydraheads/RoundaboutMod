package net.hydra.jojomod.entity.gold_experience;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class GEFishEntity extends Salmon implements GELifeform {
    private boolean isReflectingDamage = false;
    @Nullable
    private UUID creatorUUID;
    private int lifespan = 600;

    private static final EntityDataAccessor<ItemStack> DATA_SOURCE_ITEM =
            SynchedEntityData.defineId(GEFishEntity.class, EntityDataSerializers.ITEM_STACK);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SOURCE_ITEM, ItemStack.EMPTY);
    }

    private float lockedXRot = 0.0F;
    private float lockedYRot = 0.0F;
    private boolean hasLockedRotation = false;

    public GEFishEntity(EntityType<? extends Salmon> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.lifespan > 0) {
                this.lifespan--;
                this.setNoGravity(this.isInWater());

                Entity rider = this.getFirstPassenger();
                if (rider instanceof LivingEntity livingRider) {
                    float targetYRot = livingRider.getYRot();
                    float targetXRot = Mth.clamp(livingRider.getXRot(), -60.0F, 60.0F);
                    this.setYRot(Mth.rotLerp(0.25F, this.getYRot(), targetYRot));
                    this.setXRot(Mth.lerp(0.25F, this.getXRot(), targetXRot));
                    this.yBodyRot = this.getYRot();
                    this.yHeadRot = this.getYRot();
                } else if (this.hasLockedRotation) {
                    this.setXRot(this.lockedXRot);
                    this.setYRot(this.lockedYRot);
                    this.yBodyRot = this.lockedYRot;
                    this.yHeadRot = this.lockedYRot;
                }

                swimStraightAhead();
            } else {
                this.die(this.damageSources().generic());
            }
        }
    }

    private void swimStraightAhead() {
        // 2 second delay before swimming
        if (this.tickCount > 40) {
            if (this.isInWater()) {
                Vec3 forwardDir = Vec3.directionFromRotation(this.getXRot(), this.getYRot());
                Vec3 move = forwardDir.scale(0.5D);

                boolean atSurface = !this.level().getFluidState(this.blockPosition().above()).is(FluidTags.WATER);
                if (atSurface && move.y > 0.0D) {
                    move = new Vec3(move.x, 0.0D, move.z);
                }

                this.setDeltaMovement(move);
            }
        } else {
            if (this.isInWater()) {
                this.setDeltaMovement(Vec3.ZERO);
            }
        }
    }

    @Override
    public boolean isInWater() {
        // used to make the fih not use the flop animation while it's skimming on the surface of water
        boolean isWaterBelow = this.level().getFluidState(this.blockPosition().below()).is(FluidTags.WATER);
        return super.isInWater() || isWaterBelow;
    }

    @Override
    protected void registerGoals() {
        // lobotomize the fih
    }

    // let the players ride the fih
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.level().isClientSide() && this.getPassengers().size() < 2 && !player.isPassenger()) {
            player.startRiding(this);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    // up to 2 people can hold the fih
    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().size() < 2;
    }

    // "seat" positioning, so that players are behind the fih instead of on it
    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (this.hasPassenger(passenger)) {
            //first passenger is right behind the fih, second passenger is behind the first
            int index = this.getPassengers().indexOf(passenger);
            float distanceBehind = 0.8F + (index * 0.8F);
            float angle = this.getYRot() * ((float) Math.PI / 180F);
            double offsetX = Math.sin(angle) * distanceBehind;
            double offsetZ = -Math.cos(angle) * distanceBehind;
            double offsetY = -0.2D;

            moveFunction.accept(passenger, this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ);
        } else {
            super.positionRider(passenger, moveFunction);
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
        tag.putBoolean("HasLockedRotation", this.hasLockedRotation);
        tag.putFloat("LockedXRot", this.lockedXRot);
        tag.putFloat("LockedYRot", this.lockedYRot);
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
        if (tag.contains("HasLockedRotation")) {
            this.hasLockedRotation = tag.getBoolean("HasLockedRotation");
            this.lockedXRot = tag.getFloat("LockedXRot");
            this.lockedYRot = tag.getFloat("LockedYRot");
        }
    }

    @Override
    public boolean isReflectingDamage() { return isReflectingDamage; }
    @Override
    public void setReflectingDamage(boolean reflecting) { this.isReflectingDamage = reflecting; }
    @Override
    @Nullable
    public UUID getCreatorUUID() { return this.creatorUUID; }
    @Override
    public void setCreatorUUID(@Nullable UUID uuid) { this.creatorUUID = uuid; }
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
        // unique to the fish, it needs to face where the creator is facing
        if (creator != null) {
            this.lockedXRot = creator.getXRot();
            this.lockedYRot = creator.getYRot();
            this.hasLockedRotation = true;

            this.setXRot(this.lockedXRot);
            this.setYRot(this.lockedYRot);
            this.yBodyRot = this.lockedYRot;
            this.yHeadRot = this.lockedYRot;
        }
    }
    @Override
    public int getLifespan() { return lifespan; }
    @Override
    public void setLifespan(int ticks) { this.lifespan = ticks; }
    @Override
    public void setSourceItem(ItemStack stack) {this.entityData.set(DATA_SOURCE_ITEM, stack);}
    @Override
    public ItemStack getSourceItem() {return this.entityData.get(DATA_SOURCE_ITEM);}
}