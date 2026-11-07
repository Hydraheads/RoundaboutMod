package net.hydra.jojomod.entity;

import net.hydra.jojomod.access.NoHitboxRendering;
import net.hydra.jojomod.client.ClientNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;


public class SilverChariotMountEntity extends LivingEntity implements NoHitboxRendering {

    public SilverChariotMountEntity(EntityType<? extends SilverChariotMountEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public SilverChariotMountEntity(Level $$0) {
        this(ModEntities.SC_MOUNT, $$0);
    }

    private static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(SilverChariotMountEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> HOVER_CHARGE = SynchedEntityData.defineId(SilverChariotMountEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MAX_HOVER_CHARGE = SynchedEntityData.defineId(SilverChariotMountEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_HOVERING = SynchedEntityData.defineId(SilverChariotMountEntity.class, EntityDataSerializers.BOOLEAN);

    // private int maxHoverTicks = 200;
    // private int hoverTicks = 0;

    private static int configuredSelfCarryHoveringCharge() {
        return Math.max(1, ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotSelfCarryModeMaxHoverCharge);
    }

    public int getMaxHoverTicks() {
        // return maxHoverTicks;
        return this.entityData.get(MAX_HOVER_CHARGE);
    }

    public void setMaxHoverTicks(int ticks) {
        // this.maxHoverTicks = ticks;
        this.entityData.set(MAX_HOVER_CHARGE, ticks);
    }

    public int getHoverTicks() {
        // return hoverTicks;
        return entityData.get(HOVER_CHARGE);
    }

    public void setHoverTicks(int ticks) {
        // this.hoverTicks = ticks;
        this.entityData.set(HOVER_CHARGE, ticks);
    }

    public void resetHoverTicks() {
        // hoverTicks = 0;
        this.entityData.set(HOVER_CHARGE, 0);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            tickHovering();
        }
    }

    private void tickHovering() {
        int hoverTicks = this.getHoverTicks();
        int maxHoverTicks = this.getMaxHoverTicks();
        if (onGround()) {
            resetHoverTicks();
        } else {
            /*
            hoverTicks++;
            if (hoverTicks > maxHoverTicks) {
                hoverTicks = maxHoverTicks;
            }
            */
        }

        /*
        if (hoverTicks > maxHoverTicks) {
        } else {
            this.resetFallDistance();
        }
        */
    }

    @Override
    public void travel(Vec3 $$0) {
        // super.travel($$0);
        int hoverTicks = this.getHoverTicks();
        int maxHoverTicks = this.getMaxHoverTicks();
        if (this.isControlledByLocalInstance()) {
            boolean veh = this.isVehicle() && !this.getPassengers().isEmpty();
            if (veh) {
                float yboost = hoverTicks > maxHoverTicks ? 0.06f : 0.3f;
                this.moveRelative(3.0F, $$0);
                this.setDeltaMovement(this.getDeltaMovement().add(0, $$0.y*yboost, 0));
            }
        }
    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }

    @Override
    public boolean hurt(DamageSource $$0, float $$1) {
        // return super.hurt($$0, $$1);
        return false;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return null;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return null;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }

    public float yaccel = 0f;
    public final float verticalSpeed = 0.1f;
    public final float changeByHeight = 0.4f;

    @Override
    protected Vec3 getRiddenInput(Player $$0, Vec3 $$1) {
        float $$2 = $$0.xxa;
        float $$3 = yaccel;
        float $$4 = $$0.zza;
        // float $$4 = $$0.zza * 2;
        if ($$4 <= 0.0F) {
            // $$4 *= 0.25F;
        }

        return new Vec3((double) $$2, $$3, (double) $$4);
    }

    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        if (!this.getPassengers().isEmpty()) {
            Entity entity = this.getPassengers().get(0);
            if (entity instanceof LivingEntity) {
                return (LivingEntity) entity;
            }
        }
        return null;
    }

    @Override
    protected void positionRider(Entity $$0, MoveFunction $$1) {
        super.positionRider($$0, $$1);
    }

    @Nullable
    private Vec3 getDismountLocationInDirection(Vec3 $$0, LivingEntity $$1) {
        double $$2 = this.getX() + $$0.x;
        double $$3 = this.getBoundingBox().minY;
        double $$4 = this.getZ() + $$0.z;
        BlockPos.MutableBlockPos $$5 = new BlockPos.MutableBlockPos();

        for (Pose $$6 : $$1.getDismountPoses()) {
            $$5.set($$2, $$3, $$4);
            double $$7 = this.getBoundingBox().maxY + 0.75;

            do {
                double $$8 = this.level().getBlockFloorHeight($$5);
                if ((double)$$5.getY() + $$8 > $$7) {
                    break;
                }

                if (DismountHelper.isBlockFloorValid($$8)) {
                    AABB $$9 = $$1.getLocalBoundsForPose($$6);
                    Vec3 $$10 = new Vec3($$2, (double)$$5.getY() + $$8, $$4);
                    if (DismountHelper.canDismountTo(this.level(), $$1, $$9.move($$10))) {
                        $$1.setPose($$6);
                        return $$10;
                    }
                }

                $$5.move(Direction.UP);
            } while (!((double)$$5.getY() < $$7));
        }

        return null;
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity $$0) {
        Vec3 $$1 = getCollisionHorizontalEscapeVector(
                (double)this.getBbWidth(), (double)$$0.getBbWidth(), this.getYRot() + ($$0.getMainArm() == HumanoidArm.RIGHT ? 90.0F : -90.0F)
        );
        Vec3 $$2 = this.getDismountLocationInDirection($$1, $$0);
        if ($$2 != null) {
            return $$2;
        } else {
            Vec3 $$3 = getCollisionHorizontalEscapeVector(
                    (double)this.getBbWidth(), (double)$$0.getBbWidth(), this.getYRot() + ($$0.getMainArm() == HumanoidArm.LEFT ? 90.0F : -90.0F)
            );
            Vec3 $$4 = this.getDismountLocationInDirection($$3, $$0);
            return $$4 != null ? $$4 : this.position();
        }
    }

    @Override
    public boolean alwaysAccepts() {
        return super.alwaysAccepts();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        int hoverCharge = configuredSelfCarryHoveringCharge();
        entityData.define(HOVER_CHARGE, hoverCharge);
        entityData.define(MAX_HOVER_CHARGE, hoverCharge);
        entityData.define(IS_HOVERING, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void playStepSound(BlockPos $$0, BlockState $$1) {

    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    protected void checkFallDamage(double $$0, boolean $$1, BlockState $$2, BlockPos $$3) {

    }
}
