package net.hydra.jojomod.entity;

import net.hydra.jojomod.access.NoHitboxRendering;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.stand.SilverChariotEntity;
import net.hydra.jojomod.stand.powers.PowersSilverChariot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;


public class SilverChariotMountEntity extends LivingEntity {

    public static final double eHeight = SilverChariotEntity.eHeight + 1D;
    public static final double eWidth = SilverChariotEntity.eWidth;

    public SilverChariotMountEntity(EntityType<? extends SilverChariotMountEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public SilverChariotMountEntity(Level $$0) {
        this(ModEntities.SC_MOUNT, $$0);
    }

    public static AttributeSupplier.Builder createStandAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.01D)
                .add(Attributes.FOLLOW_RANGE, 0.0D);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource $$0) {
        if ($$0.is(DamageTypes.IN_WALL)
                || $$0.is(DamageTypes.DROWN)
                || $$0.is(DamageTypes.FREEZE)
                || $$0.is(DamageTypes.HOT_FLOOR)
                || $$0.is(DamageTypes.SWEET_BERRY_BUSH)
                || $$0.is(DamageTypes.WITHER)
                || $$0.is(DamageTypes.CACTUS)
                || $$0.is(DamageTypes.IN_FIRE)
                || $$0.is(DamageTypes.ON_FIRE)) {
            return true;
        }
        return super.isInvulnerableTo($$0);
    }

    @Override
    public boolean hurt(DamageSource $$0, float $$1) {
        // return super.hurt($$0, $$1);
        return false;
    }

    /*
    @Override
    public boolean isInvisible() {
        return true;
    }
    */

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public void setSecondsOnFire(int seconds) {
    }

    private static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(SilverChariotMountEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> HOVER_CHARGE = SynchedEntityData.defineId(SilverChariotMountEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MAX_HOVER_CHARGE = SynchedEntityData.defineId(SilverChariotMountEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_HOVERING = SynchedEntityData.defineId(SilverChariotMountEntity.class, EntityDataSerializers.BOOLEAN);

    private static int configuredSelfCarryMaxHoveringCharge() {
        return Math.max(1, ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotSelfCarryModeMaxHoverCharge);
    }

    private static int configuredSelfCarryHoverRechargeRate() {
        return Math.max(1, ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotSelfCarryModeHoverRechargeRate);
    }

    public int getMaxHoverTicks() {
        // return maxHoverTicks;
        return this.entityData.get(MAX_HOVER_CHARGE);
    }

    public void setMaxHoverTicks(int ticks) {
        // this.maxHoverTicks = ticks;
        this.entityData.set(MAX_HOVER_CHARGE, Math.max(0, ticks));
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
        // hoverTicks = maxHoverTicks;
        setHoverTicks(getMaxHoverTicks());
    }

    public boolean isHovering() {
        return entityData.get(IS_HOVERING);
    }

    public void setHovering(boolean hovering) {
        entityData.set(IS_HOVERING, hovering);
    }

    private int tickLifeNoRiding = 0;

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            tickHovering();
            if (!this.isVehicle()) {
                tickLifeNoRiding++;
            }
            if (tickLifeNoRiding > 10) {
                this.discard();
            }
        }
    }

    private void tickHovering() {
        int hoverTicks = this.getHoverTicks();
        if (!isHovering() && isVehicle()) {
            // resetHoverTicks();
            setHoverTicks(hoverTicks + configuredSelfCarryHoverRechargeRate());
        } else if (isHovering() && isVehicle() && hoverTicks > 0) {
            setHoverTicks(hoverTicks - 1);
        }
    }

    @Override
    public boolean isNoGravity() {
        // return super.isNoGravity();
        return (isVehicle() && isHovering() && getHoverTicks() > 0) || super.isNoGravity();
        // return (isVehicle()) || super.isNoGravity();
    }

    public static double getSelfCarryHoverVerticalVelocity() {
        return (double) ClientNetworking.getAppropriateConfig().silverChariotSettings.selfCarryHoverVerticalVelocity;
    }

    public static double getSelfCarryHoverVelocityModifierX() {
        return (double) ClientNetworking.getAppropriateConfig().silverChariotSettings.selfCarryHoverVelocityModifierX;
    }

    public static double getSelfCarryHoverVelocityModifierZ() {
        return (double) ClientNetworking.getAppropriateConfig().silverChariotSettings.selfCarryHoverVelocityModifierZ;
    }

    @Override
    public void travel(Vec3 $$0) {
        if (isVehicle() && getControllingPassenger() instanceof Player player) {
            setYRot(player.getYRot());
            setXRot(player.getXRot() * 0.5F);

            yRotO = getYRot();
            xRotO = getXRot();
        }

        if (isVehicle() && isHovering() && getHoverTicks() > 0) {
            Vec3 velocity = getDeltaMovement();

            setDeltaMovement(velocity.x * getSelfCarryHoverVelocityModifierX(), getSelfCarryHoverVerticalVelocity(), velocity.z * getSelfCarryHoverVelocityModifierZ());
            hasImpulse = true;
        }

        super.travel($$0);
    }

    @Override
    protected void tickRidden(Player $$0, Vec3 $$1) {
        super.tickRidden($$0, $$1);
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }

    @Override
    protected Vec3 getRiddenInput(Player $$0, Vec3 $$1) {
        // float $$2 = $$0.xxa / 1.5F;
        // float $$4 = $$0.zza / 1.5F;
        float $$2 = $$0.xxa;
        float $$4 = $$0.zza;
        // float $$4 = $$0.zza * 2;
        if ($$4 <= 0.0F) {
            $$4 *= 0.25F;
        }

        return new Vec3($$2, 0D, $$4);

        // return new Vec3((double) $$2, $$3, (double) $$4);
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        return 0.25F;
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
    public double getPassengersRidingOffset() {
        return SilverChariotMountEntity.eHeight * 0.65D;
    }

    @Override
    protected void positionRider(Entity $$0, MoveFunction $$1) {
        /*
        if ($$0 instanceof Player) {
            $$1.accept($$0,this.getX(),this.getY()+1.35D,this.getZ());
        } else {
            super.positionRider($$0,$$1);
        }
        */
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
        int hoverCharge = configuredSelfCarryMaxHoveringCharge();
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
