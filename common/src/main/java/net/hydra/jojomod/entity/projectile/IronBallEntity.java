package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class IronBallEntity extends AbstractArrow {
    private static final EntityDataAccessor<ItemStack> IRON_BALL = SynchedEntityData.defineId(IronBallEntity.class,
                EntityDataSerializers.ITEM_STACK);

    public IronBallEntity(Level $$0, LivingEntity $$1) {
        super(ModEntities.IRON_BALL, $$1, $$0);
    }
    public IronBallEntity(EntityType<? extends IronBallEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public IronBallEntity(Level $$0, double $$1, double $$2, double $$3) {
        super(ModEntities.IRON_BALL, $$1, $$2, $$3, $$0);
    }

    public IronBallEntity(Level $$0, LivingEntity $$1, ItemStack $$2, double p_36862_, double p_36863_, double p_36864_) {
        super(ModEntities.IRON_BALL, p_36862_, p_36863_, p_36864_, $$0);
        this.setArrow($$2.copy());
    }

    @Override
    public void tick() {
        super.tick();
    }

    public void addItem(Entity target, ItemStack stack){
        if (target != null && !target.level().isClientSide()) {
            ItemEntity $$4 = new ItemEntity(target.level(), target.getX(),
                    target.getY() + target.getEyeHeight(), target.getZ(),
                    stack);
            $$4.setPickUpDelay(20);
            $$4.setThrower(target.getUUID());
            target.level().addFreshEntity($$4);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult $$0) {
    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        if (!level().isClientSide()) {
            BlockState $$1 = this.level().getBlockState($$0.getBlockPos());
            $$1.onProjectileHit(this.level(), $$1, $$0, this);
            this.discard();
        }

    }

    @Override
    public void remove(Entity.RemovalReason $$0) {
        if (this.pickup == AbstractArrow.Pickup.ALLOWED) {
            this.spawnAtLocation(this.getPickupItem().copy(), 0.1F);
        }
        this.setRemoved($$0);
    }
    public IronBallEntity(Level $$0, LivingEntity $$1, ItemStack stack) {
        super(ModEntities.IRON_BALL, $$1, $$0);
        setArrow(stack.copy());
    }

    public ItemStack getArrow() {
        return this.entityData.get(IRON_BALL);
    }
    public void setArrow(ItemStack arrow) {
        this.entityData.set(IRON_BALL,arrow);
    }
    @Override
    protected ItemStack getPickupItem() {
        return getArrow();
    }


    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        CompoundTag compoundtag = new CompoundTag();
        $$0.put("ironBall",this.getPickupItem().save(compoundtag));
        super.addAdditionalSaveData($$0);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        CompoundTag compoundtag = $$0.getCompound("ironBall");
        ItemStack itemstack = ItemStack.of(compoundtag);
        this.setArrow(itemstack);
        super.readAdditionalSaveData($$0);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IRON_BALL, ItemStack.EMPTY);
    }
}
