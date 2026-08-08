package net.hydra.jojomod.entity;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public class FakeItemEntity extends Entity implements TraceableEntity {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(FakeItemEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final int LIFETIME = 6000;
    private static final int INFINITE_PICKUP_DELAY = 32767;
    private static final int INFINITE_LIFETIME = -32768;
    private int age;
    private int pickupDelay;
    private int health = 5;
    @Nullable
    private UUID thrower;
    @Nullable
    private UUID target;
    public final float bobOffs;

    public FakeItemEntity(EntityType<? extends FakeItemEntity> type, Level level) {
        super(type, level);
        this.bobOffs = this.random.nextFloat() * (float) Math.PI * 2.0F;
        this.setYRot(this.random.nextFloat() * 360.0F);
    }

    public FakeItemEntity(Level $$0, double $$1, double $$2, double $$3, ItemStack $$4) {
        this($$0, $$1, $$2, $$3, $$4, $$0.random.nextDouble() * 0.2 - 0.1, 0.2, $$0.random.nextDouble() * 0.2 - 0.1);
    }

    public FakeItemEntity(Level $$0, double $$1, double $$2, double $$3, ItemStack $$4, double $$5, double $$6, double $$7) {
        this(ModEntities.FAKE_ITEM, $$0);
        this.setPos($$1, $$2, $$3);
        this.setDeltaMovement($$5, $$6, $$7);
        this.setItem($$4);
    }

    private FakeItemEntity(FakeItemEntity $$0) {
        super($$0.getType(), $$0.level());
        this.setItem($$0.getItem().copy());
        this.copyPosition($$0);
        this.age = $$0.age;
        this.bobOffs = $$0.bobOffs;
    }

    @Override
    public boolean dampensVibrations() {
        return this.getItem().is(ItemTags.DAMPENS_VIBRATIONS);
    }

    @Nullable
    @Override
    public Entity getOwner() {
        return this.thrower != null && this.level() instanceof ServerLevel $$0 ? $$0.getEntity(this.thrower) : null;
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_ITEM, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        if (!level().isClientSide()) {
            if (host == null || !PowerTypes.isErasingTime(host)) {
                this.discard();
            }
        }
        if (this.getItem().isEmpty()) {
            this.discard();
        } else {
            if (this.pickupDelay > 0 && this.pickupDelay != 32767) {
                this.pickupDelay--;
            }
            if (!this.level().isClientSide && this.age >= 6000) {
                this.discard();
            } else if (this.age >= 5999){
                this.age++;
            }
        }
        if (((IEntityAndData)this).roundabout$getNoGravTicks() > 0){
            ((IEntityAndData)this).roundabout$setNoGravTicks(((IEntityAndData)this).roundabout$getNoGravTicks()-1);
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.04, 0.0));
            }
        }
        if (this.getItem().isEmpty()) {
            this.discard();
        } else {
            super.tick();
            if (this.pickupDelay > 0 && this.pickupDelay != 32767) {
                this.pickupDelay--;
            }

            this.xo = this.getX();
            this.yo = this.getY();
            this.zo = this.getZ();
            Vec3 $$0 = this.getDeltaMovement();
            float $$1 = this.getEyeHeight() - 0.11111111F;
            if (this.isInWater() && this.getFluidHeight(FluidTags.WATER) > (double)$$1) {
                this.setUnderwaterMovement();
            } else if (this.isInLava() && this.getFluidHeight(FluidTags.LAVA) > (double)$$1) {
                this.setUnderLavaMovement();
            } else if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
            }

            if (this.level().isClientSide) {
                this.noPhysics = false;
            } else {
                this.noPhysics = !this.level().noCollision(this, this.getBoundingBox().deflate(1.0E-7));
                if (this.noPhysics) {
                    this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0, this.getZ());
                }
            }

            if (!this.onGround() || this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-5F || (this.tickCount + this.getId()) % 4 == 0) {
                this.move(MoverType.SELF, this.getDeltaMovement());
                float $$2 = 0.98F;
                if (this.onGround()) {
                    $$2 = this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.98F;
                }

                this.setDeltaMovement(this.getDeltaMovement().multiply((double)$$2, 0.98, (double)$$2));
                if (this.onGround()) {
                    Vec3 $$3 = this.getDeltaMovement();
                    if ($$3.y < 0.0) {
                        this.setDeltaMovement($$3.multiply(1.0, -0.5, 1.0));
                    }
                }
            }

            boolean $$4 = Mth.floor(this.xo) != Mth.floor(this.getX())
                    || Mth.floor(this.yo) != Mth.floor(this.getY())
                    || Mth.floor(this.zo) != Mth.floor(this.getZ());
            int $$5 = $$4 ? 2 : 40;
            if (this.tickCount % $$5 == 0 && !this.level().isClientSide && this.isMergable()) {
                this.mergeWithNeighbours();
            }

            if (this.age != -32768) {
                this.age++;
            }

            this.hasImpulse = this.hasImpulse | this.updateInWaterStateAndDoFluidPushing();
            if (!this.level().isClientSide) {
                double $$6 = this.getDeltaMovement().subtract($$0).lengthSqr();
                if ($$6 > 0.01) {
                    this.hasImpulse = true;
                }
            }

            if (!this.level().isClientSide && this.age >= 6000) {
                this.discard();
            }
        }
    }

    @Override
    protected BlockPos getBlockPosBelowThatAffectsMyMovement() {
        return this.getOnPos(0.999999F);
    }

    private void setUnderwaterMovement() {
        Vec3 $$0 = this.getDeltaMovement();
        this.setDeltaMovement($$0.x * 0.99F, $$0.y + (double)($$0.y < 0.06F ? 5.0E-4F : 0.0F), $$0.z * 0.99F);
    }

    private void setUnderLavaMovement() {
        Vec3 $$0 = this.getDeltaMovement();
        this.setDeltaMovement($$0.x * 0.95F, $$0.y + (double)($$0.y < 0.06F ? 5.0E-4F : 0.0F), $$0.z * 0.95F);
    }

    private void mergeWithNeighbours() {
        if (this.isMergable()) {
            for (FakeItemEntity $$1 : this.level()
                    .getEntitiesOfClass(FakeItemEntity.class, this.getBoundingBox().inflate(0.5, 0.0, 0.5), $$0x -> $$0x != this && $$0x.isMergable())) {
                if ($$1.isMergable()) {
                    this.tryToMerge($$1);
                    if (this.isRemoved()) {
                        break;
                    }
                }
            }
        }
    }

    private boolean isMergable() {
        ItemStack $$0 = this.getItem();
        return this.isAlive() && this.pickupDelay != 32767 && this.age != -32768 && this.age < 6000 && $$0.getCount() < $$0.getMaxStackSize();
    }

    private void tryToMerge(FakeItemEntity $$0) {
        ItemStack $$1 = this.getItem();
        ItemStack $$2 = $$0.getItem();
        if (Objects.equals(this.target, $$0.target) && areMergable($$1, $$2)) {
            if ($$2.getCount() < $$1.getCount()) {
                merge(this, $$1, $$0, $$2);
            } else {
                merge($$0, $$2, this, $$1);
            }
        }
    }

    public static boolean areMergable(ItemStack $$0, ItemStack $$1) {
        if (!$$1.is($$0.getItem())) {
            return false;
        } else if ($$1.getCount() + $$0.getCount() > $$1.getMaxStackSize()) {
            return false;
        } else {
            return $$1.hasTag() ^ $$0.hasTag() ? false : !$$1.hasTag() || $$1.getTag().equals($$0.getTag());
        }
    }

    public static ItemStack merge(ItemStack $$0, ItemStack $$1, int $$2) {
        int $$3 = Math.min(Math.min($$0.getMaxStackSize(), $$2) - $$0.getCount(), $$1.getCount());
        ItemStack $$4 = $$0.copyWithCount($$0.getCount() + $$3);
        $$1.shrink($$3);
        return $$4;
    }

    private static void merge(FakeItemEntity $$0, ItemStack $$1, ItemStack $$2) {
        ItemStack $$3 = merge($$1, $$2, 64);
        $$0.setItem($$3);
    }

    public Player host = null;

    private static void merge(FakeItemEntity $$0, ItemStack $$1, FakeItemEntity $$2, ItemStack $$3) {
        merge($$0, $$1, $$3);
        $$0.pickupDelay = Math.max($$0.pickupDelay, $$2.pickupDelay);
        $$0.age = Math.min($$0.age, $$2.age);
        if ($$3.isEmpty()) {
            $$2.discard();
        }
    }

    @Override
    public boolean fireImmune() {
        return this.getItem().getItem().isFireResistant() || super.fireImmune();
    }

    @Override
    public boolean hurt(DamageSource $$0, float $$1) {
        if (this.isInvulnerableTo($$0)) {
            return false;
        } else if (!this.getItem().isEmpty() && this.getItem().is(Items.NETHER_STAR) && $$0.is(DamageTypeTags.IS_EXPLOSION)) {
            return false;
        } else if (!this.getItem().getItem().canBeHurtBy($$0)) {
            return false;
        } else if (this.level().isClientSide) {
            return true;
        } else {
            this.markHurt();
            this.health = (int)((float)this.health - $$1);
            this.gameEvent(GameEvent.ENTITY_DAMAGE, $$0.getEntity());
            if (this.health <= 0) {
                this.discard();
            }

            return true;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        $$0.putShort("Health", (short)this.health);
        $$0.putShort("Age", (short)this.age);
        $$0.putShort("PickupDelay", (short)this.pickupDelay);
        if (this.thrower != null) {
            $$0.putUUID("Thrower", this.thrower);
        }

        if (this.target != null) {
            $$0.putUUID("Owner", this.target);
        }

        if (!this.getItem().isEmpty()) {
            $$0.put("Item", this.getItem().save(new CompoundTag()));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        this.health = $$0.getShort("Health");
        this.age = $$0.getShort("Age");
        if ($$0.contains("PickupDelay")) {
            this.pickupDelay = $$0.getShort("PickupDelay");
        }

        if ($$0.hasUUID("Owner")) {
            this.target = $$0.getUUID("Owner");
        }

        if ($$0.hasUUID("Thrower")) {
            this.thrower = $$0.getUUID("Thrower");
        }

        CompoundTag $$1 = $$0.getCompound("Item");
        this.setItem(ItemStack.of($$1));
        if (this.getItem().isEmpty()) {
            this.discard();
        }
    }

    @Override
    public void playerTouch(Player $$0) {
//        if (PowerTypes.isExistentiallyElsewhere($$0)){
//            return;
//        }
//        if (!this.level().isClientSide) {
//            ItemStack $$1 = this.getItem();
//            if (this.pickupDelay == 0 && (this.target == null || this.target.equals($$0.getUUID())) && $$0.getInventory().add($$1)) {
//                if ($$1.isEmpty()) {
//                    this.level().playSound(null, this.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1F, (float)(0.99+Math.random()*0.02F));
//
//                    this.discard();
//                }
//            }
//        }
    }

    @Override
    public Component getName() {
        Component $$0 = this.getCustomName();
        return (Component)($$0 != null ? $$0 : Component.translatable(this.getItem().getDescriptionId()));
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Nullable
    @Override
    public Entity changeDimension(ServerLevel $$0) {
        Entity $$1 = super.changeDimension($$0);
        if (!this.level().isClientSide && $$1 instanceof FakeItemEntity) {
            ((FakeItemEntity)$$1).mergeWithNeighbours();
        }

        return $$1;
    }

    public ItemStack getItem() {
        return this.getEntityData().get(DATA_ITEM);
    }

    public void setItem(ItemStack $$0) {
        this.getEntityData().set(DATA_ITEM, $$0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> $$0) {
        super.onSyncedDataUpdated($$0);
        if (DATA_ITEM.equals($$0)) {
            this.getItem().setEntityRepresentation(this);
        }
    }

    public void setTarget(@Nullable UUID $$0) {
        this.target = $$0;
    }

    public void setThrower(@Nullable UUID $$0) {
        if ($$0 != null) {
            Player pl =  this.level().getPlayerByUUID($$0);
            if (pl != null){
                if (MainUtil.getEntityIsTrulyInvisible(pl) && ClientNetworking.getAppropriateConfig().achtungSettings.hidesShotProjectiles){
                    ((IEntityAndData)this).roundabout$setTrueInvisibility(MainUtil.getEntityTrulyInvisibleTicks(pl));
                }
            }
        }
        this.thrower = $$0;
    }

    public int getAge() {
        return this.age;
    }

    public void setDefaultPickUpDelay() {
        this.pickupDelay = 10;
    }

    public void setNoPickUpDelay() {
        this.pickupDelay = 0;
    }

    public void setNeverPickUp() {
        this.pickupDelay = 32767;
    }

    public void setPickUpDelay(int $$0) {
        this.pickupDelay = $$0;
    }

    public boolean hasPickUpDelay() {
        return this.pickupDelay > 0;
    }

    public void setUnlimitedLifetime() {
        this.age = -32768;
    }

    public void setExtendedLifetime() {
        this.age = -6000;
    }

    public void makeFakeItem() {
        this.setNeverPickUp();
        this.age = 5999;
    }

    public float getSpin(float $$0) {
        return ((float)this.getAge() + $$0) / 20.0F + this.bobOffs;
    }

    public FakeItemEntity copy() {
        return new FakeItemEntity(this);
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.AMBIENT;
    }

    @Override
    public float getVisualRotationYInDegrees() {
        return 180.0F - this.getSpin(0.5F) / (float) (Math.PI * 2) * 360.0F;
    }
}
