package net.hydra.jojomod.entity.corpses;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class FallenSpider extends FallenMob implements PlayerRideableJumping {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(FallenSpider.class, EntityDataSerializers.BYTE);
    private static final float SPIDER_SPECIAL_EFFECT_CHANCE = 0.1F;

    public FallenSpider(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.MAX_HEALTH, 16)
                .add(Attributes.ATTACK_DAMAGE, 1.5).
                add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    public String getData() {
        return "spider";
    }
    @Nullable
    public LivingEntity getControllingPassenger() {
        if (!this.getPassengers().isEmpty()) {
            Entity entity = this.getPassengers().get(0);
            if (entity instanceof LivingEntity && this.getActivated()
            && this.getController() == entity.getId()) {
                return (LivingEntity)entity;
            }
        }

        return null;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, false));
        this.addBehaviourGoals();
    }

    @Override
    protected float getStandingEyeHeight(Pose $$0, EntityDimensions $$1) {
        return 0.65F;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isControlledByLocalInstance()) {
            this.setClimbing(this.horizontalCollision);
        }
    }

    protected boolean isJumping;

    @Override
    protected float getRiddenSpeed(Player $$0) {
        return (float) ((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED)*0.4);
    }

    public boolean isJumping() {
        return this.isJumping;
    }

    public void setIsJumping(boolean $$0) {
        this.isJumping = $$0;
    }

    public double getCustomJump() {
        return 0.5F;
    }

    protected void executeRidersJump(float $$0, Vec3 $$1) {
        double $$2 = this.getCustomJump() * (double) $$0 * (double) this.getBlockJumpFactor();
        double $$3 = $$2 + (double) this.getJumpBoostPower();
        Vec3 $$4 = this.getDeltaMovement();
        this.setDeltaMovement($$4.x, $$3, $$4.z);
        this.setIsJumping(true);
        this.hasImpulse = true;
        if ($$1.z > 0.0) {
            float $$5 = Mth.sin(this.getYRot() * (float) (Math.PI / 180.0));
            float $$6 = Mth.cos(this.getYRot() * (float) (Math.PI / 180.0));
            this.setDeltaMovement(this.getDeltaMovement().add((double) (-0.4F * $$5 * $$0), 0.0, (double) (0.4F * $$6 * $$0)));
        }
    }

    @Override
    protected void tickRidden(Player $$0, Vec3 $$1) {
        super.tickRidden($$0, $$1);
        Vec2 $$2 = this.getRiddenRotation($$0);
        this.setRot($$2.y, $$2.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        if (this.isControlledByLocalInstance()) {

            if (this.onGround()) {
                this.setIsJumping(false);
                if (this.playerJumpPendingScale > 0.0F && !this.isJumping()) {
                    this.executeRidersJump(this.playerJumpPendingScale, $$1);
                }

                this.playerJumpPendingScale = 0.0F;
            }
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) (this.getBbHeight() * 0.5F);
    }

    @Override
    protected PathNavigation createNavigation(Level $$0) {
        return new WallClimberNavigation(this, $$0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte) 0);
    }


    @Override
    public boolean causeFallDamage(float $$0, float $$1, DamageSource $$2) {
        if ($$0 > 1.0F) {
        }

        int $$3 = this.calculateFallDamage($$0, $$1);
        if ($$3 <= 0) {
            return false;
        } else {
            this.hurt($$2, (float)$$3);

            this.playBlockFallSound();
            return true;
        }
    }
    @Override
    protected SoundEvent getAmbientSound() {
        if (this.getActivated()) {
            return SoundEvents.SPIDER_AMBIENT;
        } else {
            return super.getAmbientSound();
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource $$0) {
        if (this.getActivated()) {
            return SoundEvents.SPIDER_HURT;
        } else {
            return super.getHurtSound($$0);
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        if (this.getActivated()) {
            return SoundEvents.SPIDER_DEATH;
        } else {
            return super.getDeathSound();
        }
    }

    @Override
    protected void playStepSound(BlockPos $$0, BlockState $$1) {
        if (this.getActivated()) {
            if (this.getController() > 0 && this.getControllingPassenger() instanceof
            Player PE && this.getController() == PE.getId() &&
                    ((StandUser)PE).roundabout$getStandPowers().isPiloting()){
                return;
            }
            this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
        }
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    @Override
    public void makeStuckInBlock(BlockState $$0, Vec3 $$1) {
        if (!$$0.is(Blocks.COBWEB)) {
            super.makeStuckInBlock($$0, $$1);
        }
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance $$0) {
        return $$0.getEffect() == MobEffects.POISON ? false : super.canBeAffected($$0);
    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean $$0) {
        byte $$1 = this.entityData.get(DATA_FLAGS_ID);
        if ($$0) {
            $$1 = (byte) ($$1 | 1);
        } else {
            $$1 = (byte) ($$1 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, $$1);
    }

    protected float playerJumpPendingScale;

    @Override
    public void onPlayerJump(int var1) {
        if (var1 < 0) {
            var1 = 0;
        }

        if (var1 >= 90) {
            this.playerJumpPendingScale = 1.0F;
        } else {
            this.playerJumpPendingScale = 0.4F + 0.4F * (float) var1 / 90.0F;
        }
    }

    @Override
    public boolean canJump() {
        return false;
    }

    @Override
    public void handleStartJump(int $$0) {
    }

    @Override
    public void handleStopJump() {

    }


    protected Vec2 getRiddenRotation(LivingEntity $$0) {
        return new Vec2($$0.getXRot() * 0.5F, $$0.getYRot());
    }

    @Override
    protected Vec3 getRiddenInput(Player $$0, Vec3 $$1) {
        float $$2 = $$0.xxa * 0.5F;
        float $$3 = $$0.zza;
        if ($$3 <= 0.0F) {
            $$3 *= 0.25F;
        }

        return new Vec3((double) $$2, 0.0, (double) $$3);
    }

    protected void doPlayerRide(Player $$0) {
        if (!this.level().isClientSide) {
            $$0.setYRot(this.getYRot());
            $$0.setXRot(this.getXRot());
            $$0.startRiding(this);
        }
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
    public InteractionResult mobInteract(Player $$0, InteractionHand $$1) {
        if (this.isVehicle() || !this.getActivated() || !(getController() == $$0.getId())) {
            return super.mobInteract($$0, $$1);
        } else {

            this.doPlayerRide($$0);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
    }
}