package net.hydra.jojomod.entity.stand;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.projectile.HallucinatoryAcidProjectile;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.hydra.jojomod.stand.powers.WhitesnakeControlInventory;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;

public class WhitesnakeEntity extends FollowingStandEntity {
    public static final byte ANIME_SKIN = 0;
    public static final byte MANGA_SKIN = 1;
    public static final byte ANIME_PURPLE_SKIN = 2;
    public static final byte ANIME_GREEN_SKIN = 3;
    public static final byte ANIME_YELLOW_SKIN = 4;
    public static final byte ANIME_AQUA_SKIN = 5;
    public static final byte MANGA_PURPLE_SKIN = 6;
    public static final byte MANGA_RED_SKIN = 7;
    public static final byte GOLD_SKIN = 8;
    public static final byte SILVER_SKIN = 9;
    public static final byte COTTON_CANDY_SKIN = 10;
    public static final byte ASBR_SKIN = 11;
    public static final byte JOJOVELLER_SKIN = 12;
    public static final byte DARK_SKIN = 13;
    public static final byte SOUR_CANDY_SKIN = 14;
    public static final byte EDGY_GOLD_SKIN = 15;
    public static final byte GOLD_TRIMMED_SKIN = 16;
    public static final byte CHOP_ATTACK = 82;
    public static final byte CHOP_CHARGED = 83;
    public static final byte DISC_STEAL_WINDUP = 88;
    public static final byte DISC_STEAL_RELEASE = 89;
    public static final byte ACID_TOSS = 90;
    public static final byte SNAKE_BITE = 39;
    public static final byte SNAKE_BITE_IMPACT = 40;
    private static final EntityDataAccessor<Optional<UUID>> DISGUISE_ID = SynchedEntityData.defineId(
            WhitesnakeEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<String> DISGUISE_NAME = SynchedEntityData.defineId(
            WhitesnakeEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> DISGUISE_FLICKER_START = SynchedEntityData.defineId(
            WhitesnakeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MELTING_HOVER_CHARGE = SynchedEntityData.defineId(
            WhitesnakeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MELTING_HOVER_MAX_CHARGE = SynchedEntityData.defineId(
            WhitesnakeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> MELTING_HOVERING = SynchedEntityData.defineId(
            WhitesnakeEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> REMOTE_MODE = SynchedEntityData.defineId(
            WhitesnakeEntity.class, EntityDataSerializers.BYTE);
    private static final byte REMOTE_MODE_NONE = 0;
    private static final byte REMOTE_MODE_CONTROL = 1;
    private static final byte REMOTE_MODE_AUTO = 2;
    private static final float MELTING_ANIMATION_BLEND_STEP = 0.2F;
    private boolean controlDimensionsActive;
    private boolean meltingDimensionsActive;
    private float controlStrafe;
    private float controlForward;
    private float controlBodyYaw;
    private boolean controlBodyRotationActive;
    private Vec3 meltingTrailAnchor;
    private Direction meltingTrailGravity = Direction.DOWN;
    private int meltingHoverDripTicks;
    private int controlKnockbackTicks;
    private float meltingSwimBlend;
    private float meltingSwimBlendOld;
    private float meltingAcidTossBlend;
    private float meltingAcidTossBlendOld;
    public final AnimationState finalChop = new AnimationState();
    public final AnimationState finalChopHalf = new AnimationState();
    public final AnimationState finalChopCharged = new AnimationState();
    public final AnimationState finalChopWindup = new AnimationState();
    public final AnimationState hideFists = new AnimationState();
    public final AnimationState discStealWindup = new AnimationState();
    public final AnimationState discStealRelease = new AnimationState();
    public final AnimationState acidToss = new AnimationState();
    public final AnimationState meltingIdle = new AnimationState();
    public final AnimationState meltingSwim = new AnimationState();
    public final AnimationState itemGrab = new AnimationState();
    public final AnimationState itemThrow = new AnimationState();
    public final AnimationState itemRetract = new AnimationState();
    public final AnimationState impale = new AnimationState();
    public final AnimationState phaseGrab = new AnimationState();
    public final AnimationState snakeBite = new AnimationState();
    public final AnimationState snakeBiteImpact = new AnimationState();

    public WhitesnakeEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
        getNavigation().setCanFloat(true);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(0, new FloatGoal(this) {
            @Override
            public boolean canUse() {
                return isAutoModeActive() && super.canUse();
            }
        });
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DISGUISE_ID, Optional.empty());
        entityData.define(DISGUISE_NAME, "");
        entityData.define(DISGUISE_FLICKER_START, -1000);
        int hoverCharge = configuredMeltingHoverCharge();
        entityData.define(MELTING_HOVER_CHARGE, hoverCharge);
        entityData.define(MELTING_HOVER_MAX_CHARGE, hoverCharge);
        entityData.define(MELTING_HOVERING, false);
        entityData.define(REMOTE_MODE, REMOTE_MODE_NONE);
    }

    public boolean isDisguised() {
        return entityData.get(DISGUISE_ID).isPresent() && !entityData.get(DISGUISE_NAME).isEmpty();
    }

    @Nullable
    public GameProfile getDisguiseProfile() {
        Optional<UUID> id = entityData.get(DISGUISE_ID);
        String name = entityData.get(DISGUISE_NAME);
        return id.isPresent() && !name.isEmpty() ? new GameProfile(id.get(), name) : null;
    }

    public String getDisguiseName() {
        return entityData.get(DISGUISE_NAME);
    }

    public void setDisguise(GameProfile profile) {
        entityData.set(DISGUISE_ID, Optional.of(profile.getId()));
        entityData.set(DISGUISE_NAME, profile.getName());
        entityData.set(DISGUISE_FLICKER_START, tickCount);
    }

    public void clearDisguise() {
        entityData.set(DISGUISE_ID, Optional.empty());
        entityData.set(DISGUISE_NAME, "");
        entityData.set(DISGUISE_FLICKER_START, -1000);
    }

    public boolean isDisguiseFlickering(float partialTick) {
        float elapsed = tickCount + partialTick - entityData.get(DISGUISE_FLICKER_START);
        return elapsed >= 0.0F && elapsed < 18.0F;
    }

    public boolean shouldRenderDisguiseDuringFlicker(float partialTick) {
        float elapsed = tickCount + partialTick - entityData.get(DISGUISE_FLICKER_START);
        return ((int) (elapsed / 3.0F) & 1) == 1;
    }

    public boolean isDisguiseGuarding() {
        LivingEntity user = getUser();
        return isDisguised() && user != null && ((StandUser) user).roundabout$getStandPowers().isGuarding();
    }

    public boolean isDisguiseMining() {
        LivingEntity user = getUser();
        return isDisguised() && user != null
                && ((StandUser) user).roundabout$getStandPowers().getActivePower() == PowerIndex.MINING;
    }

    public boolean isAutoModeActive() {
        return entityData.get(REMOTE_MODE) == REMOTE_MODE_AUTO;
    }

    public void setAutoMode(boolean active) {
        if (active) setRemoteMode(REMOTE_MODE_AUTO);
        else if (isAutoModeActive()) setRemoteMode(REMOTE_MODE_NONE);
    }

    public void setControlMode(boolean active) {
        if (active) setRemoteMode(REMOTE_MODE_CONTROL);
        else if (entityData.get(REMOTE_MODE) == REMOTE_MODE_CONTROL) setRemoteMode(REMOTE_MODE_NONE);
    }

    private void setRemoteMode(byte mode) {
        if (entityData.get(REMOTE_MODE) == mode) return;
        entityData.set(REMOTE_MODE, mode);
        boolean controlled = mode != REMOTE_MODE_NONE;
        if (controlled) ((IGravityEntity) this).roundabout$setGravityDirection(Direction.DOWN);
        controlDimensionsActive = controlled;
        meltingDimensionsActive = controlled && isMeltingModeActive();
        if (!controlled) setPose(Pose.STANDING);
        refreshDimensions();
        updateControlVisibility(controlled);
    }

    public boolean isControlModeActive() {
        return entityData.get(REMOTE_MODE) == REMOTE_MODE_CONTROL;
    }

    public boolean isSnakeBiteActive() {
        LivingEntity user = getUser();
        return user != null && ((StandUser) user).roundabout$getStandPowers() instanceof PowersWhitesnake powers
                && powers.getActivePower() == PowersWhitesnake.SNAKE_BITE;
    }

    @Override
    public double getPunchYaw(double yaw, double multiplier) {
        LivingEntity user = getUser();
        if (!isRemoteControlled() && user != null
                && ((StandUser) user).roundabout$getStandPowers() instanceof PowersWhitesnake powers
                && powers.isAcidTossActive()) {
            return 0.0D;
        }
        return super.getPunchYaw(yaw, multiplier);
    }

    @Override
    public boolean isRemoteControlled() {
        return entityData.get(REMOTE_MODE) != REMOTE_MODE_NONE;
    }

    @Override
    public boolean lockPos() {
        return !isRemoteControlled();
    }

    @Override
    public boolean standHasGravity() {
        return isRemoteControlled();
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        if (isMeltingModeActive()) {
            return EntityDimensions.scalable(0.6F, 1.0F);
        }
        if (isRemoteControlled() && pose == Pose.SWIMMING) {
            return EntityDimensions.scalable(0.6F, 0.6F);
        }
        if (isRemoteControlled() && pose == Pose.CROUCHING) {
            return EntityDimensions.scalable(0.6F, 1.5F);
        }
        if (isRemoteControlled()) {
            return EntityDimensions.scalable(0.6F, 1.8F);
        }
        return super.getDimensions(pose);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        if (isMeltingModeActive()) return 0.85F;
        if (isRemoteControlled() && pose == Pose.SWIMMING) return 0.4F;
        if (isRemoteControlled()) return pose == Pose.CROUCHING ? 1.27F : 1.62F;
        return super.getStandingEyeHeight(pose, dimensions);
    }

    public void controlJump() {
        if (!isMeltingModeActive()) jumpFromGround();
    }

    public void controlSwim(boolean ascending, boolean descending) {
        if (!isInWater() || isMeltingModeActive()) return;
        if (ascending) jumpInLiquid(FluidTags.WATER);
        if (descending) {
            Vec3 velocity = getDeltaMovement();
            setDeltaMovement(velocity.x, velocity.y - 0.04D, velocity.z);
        }
    }

    @Override
    public boolean isSwimming() {
        return isRemoteControlled() ? getSharedFlag(4) : super.isSwimming();
    }

    public void setControlInput(float strafe, float forward) {
        controlStrafe = strafe;
        controlForward = forward;
    }

    public void clearControlInput() {
        controlStrafe = 0.0F;
        controlForward = 0.0F;
        xxa = 0.0F;
        zza = 0.0F;
        Vec3 velocity = getDeltaMovement();
        setDeltaMovement(0.0D, velocity.y, 0.0D);
    }

    public int getMeltingHoverCharge() {
        return entityData.get(MELTING_HOVER_CHARGE);
    }

    public int getMaxMeltingHoverCharge() {
        return entityData.get(MELTING_HOVER_MAX_CHARGE);
    }

    private static int configuredMeltingHoverCharge() {
        return Math.max(1, ClientNetworking.getAppropriateConfig().whitesnakeSettings.meltingModeHoverDuration);
    }

    public boolean isMeltingHovering() {
        return entityData.get(MELTING_HOVERING);
    }

    public void setMeltingHovering(boolean hovering) {
        entityData.set(MELTING_HOVERING, hovering && getMeltingHoverCharge() > 0);
    }

    public boolean isMeltingModeActive() {
        LivingEntity user = getUser();
        return isRemoteControlled() && user != null
                && ((StandUser) user).roundabout$getStandPowers() instanceof PowersWhitesnake powers
                && powers.isMeltingMode();
    }

    public float getMeltingSwimBlend(float partialTick) {
        return Mth.lerp(partialTick, meltingSwimBlendOld, meltingSwimBlend);
    }

    public float getMeltingAcidTossBlend(float partialTick) {
        return Mth.lerp(partialTick, meltingAcidTossBlendOld, meltingAcidTossBlend);
    }

    @Override
    public void tick() {
        super.tick();
        boolean controlled = isRemoteControlled();
        updateControlVisibility(controlled);
        boolean melting = isMeltingModeActive();
        if (controlDimensionsActive != controlled || meltingDimensionsActive != melting) {
            controlDimensionsActive = controlled;
            meltingDimensionsActive = melting;
            if (!controlled) setPose(Pose.STANDING);
            refreshDimensions();
        }
        if (!level().isClientSide() && isDisguised()) {
            LivingEntity user = getUser();
            if (!controlled || user == null || !((StandUser) user).roundabout$getActive()) clearDisguise();
        }
        if (!level().isClientSide() && getUser() instanceof Player player
                && WhitesnakeControlInventory.controlledStand(player) == this) {
            WhitesnakeControlInventory.pickupNearby(player);
        }
        if (!level().isClientSide()) {
            tickPressurePlates(controlled);
            tickMeltingHoverMeter(controlled);
            tickMeltingAcid(controlled);
        }
        if (level().isClientSide() && isControlModeActive() && !melting) {
            tickControlBodyRotation();
        } else {
            controlBodyRotationActive = false;
        }
        if (isMeltingModeActive()) setSprinting(false);
        if (controlKnockbackTicks > 0) controlKnockbackTicks--;
    }

    private void tickControlBodyRotation() {
        if (!controlBodyRotationActive) {
            controlBodyYaw = yBodyRot;
            controlBodyRotationActive = true;
        }
        double xMovement = getX() - xo;
        double zMovement = getZ() - zo;
        if (xMovement * xMovement + zMovement * zMovement > 0.0025D) {
            float movementYaw = (float) (Mth.atan2(zMovement, xMovement) * Mth.RAD_TO_DEG) - 90.0F;
            if (Math.abs(Mth.wrapDegrees(getYRot() - movementYaw)) > 95.0F) {
                movementYaw += 180.0F;
            }
            controlBodyYaw = Mth.rotLerp(0.3F, controlBodyYaw, movementYaw);
        }
        float headDifference = Mth.wrapDegrees(getYRot() - controlBodyYaw);
        if (Math.abs(headDifference) > 50.0F) {
            controlBodyYaw += headDifference - Math.copySign(50.0F, headDifference);
        }
        setYBodyRot(controlBodyYaw);
    }

    private void tickPressurePlates(boolean controlled) {
        if (!controlled || level().isClientSide()) return;
        double inset = 1.0E-4D;
        int minX = Mth.floor(getBoundingBox().minX + inset);
        int maxX = Mth.floor(getBoundingBox().maxX - inset);
        int minZ = Mth.floor(getBoundingBox().minZ + inset);
        int maxZ = Mth.floor(getBoundingBox().maxZ - inset);
        int feetY = Mth.floor(getBoundingBox().minY + inset);
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                BlockPos pos = new BlockPos(x, feetY, z);
                BlockState state = level().getBlockState(pos);
                if (state.getBlock() instanceof BasePressurePlateBlock) {
                    state.entityInside(level(), pos, this);
                }
            }
        }
    }

    private void updateControlVisibility(boolean controlled) {
        if (!level().isClientSide() || forceVisible == controlled) return;
        forceVisible = controlled;
        if (controlled) fadePercent = 1.0F;
    }

    private void tickMeltingHoverMeter(boolean controlled) {
        int configuredMaximum = configuredMeltingHoverCharge();
        if (getMaxMeltingHoverCharge() != configuredMaximum) {
            entityData.set(MELTING_HOVER_MAX_CHARGE, configuredMaximum);
        }
        LivingEntity user = getUser();
        boolean hoverEnabled = controlled && user != null
                && ((StandUser) user).roundabout$getStandPowers() instanceof PowersWhitesnake powers
                && (powers.isMeltingMode() || ClientNetworking.getAppropriateConfig().whitesnakeSettings.controlModeCanHover);
        boolean hovering = hoverEnabled && isMeltingHovering() && getMeltingHoverCharge() > 0;
        if (isMeltingHovering() != hovering) entityData.set(MELTING_HOVERING, hovering);
        int charge = getMeltingHoverCharge();
        boolean canRegenerate = hoverEnabled && hasGravitySurfaceSupport() && !isSnakeBiteActive();
        int next = hovering ? charge - 1 : canRegenerate ? charge + 1 : charge;
        next = Math.max(0, Math.min(getMaxMeltingHoverCharge(), next));
        if (next != charge) entityData.set(MELTING_HOVER_CHARGE, next);
        if (next == 0 && isMeltingHovering()) entityData.set(MELTING_HOVERING, false);
    }

    private void tickMeltingAcid(boolean controlled) {
        LivingEntity user = getUser();
        if (!controlled || user == null || !isMeltingModeActive()) {
            meltingTrailAnchor = null;
            meltingTrailGravity = Direction.DOWN;
            meltingHoverDripTicks = 0;
            return;
        }

        if (isMeltingHovering()) {
            meltingTrailAnchor = null;
            meltingHoverDripTicks++;
            if (meltingHoverDripTicks >= 20) {
                meltingHoverDripTicks = 0;
                HallucinatoryAcidProjectile drip = new HallucinatoryAcidProjectile(user, level(), 1);
                drip.setPos(getX(), getBoundingBox().minY + 0.1D, getZ());
                drip.setDeltaMovement(0.0D, -0.08D, 0.0D);
                level().addFreshEntity(drip);
            }
            return;
        }

        meltingHoverDripTicks = 0;
        Direction gravity = ((IGravityEntity) this).roundabout$getGravityDirection();
        if (gravity != meltingTrailGravity) {
            meltingTrailAnchor = null;
            meltingTrailGravity = gravity;
        }
        boolean supported = hasGravitySurfaceSupport();
        if (gravity == Direction.UP || !supported) {
            meltingTrailAnchor = null;
            return;
        }

        Vec3 current = position();
        if (meltingTrailAnchor == null) {
            meltingTrailAnchor = current;
            return;
        }
        if (current.distanceToSqr(meltingTrailAnchor) < 0.2025D) return;
        BlockPos trailPos = BlockPos.containing(meltingTrailAnchor);
        if (gravity == Direction.DOWN) {
            HallucinatoryAcidProjectile.placeTrailStageOne(level(), trailPos, user);
        } else {
            HallucinatoryAcidProjectile.placeTrailWall(level(), trailPos, user, gravity);
        }
        meltingTrailAnchor = current;
    }

    private boolean hasGravitySurfaceSupport() {
        Direction gravity = ((IGravityEntity) this).roundabout$getGravityDirection();
        Vec3 gravityProbe = new Vec3(gravity.step()).scale(0.18D);
        return !level().noCollision(this, getBoundingBox().deflate(0.03D).move(gravityProbe));
    }

    @Override
    public HumanoidArm getMainArm() {
        return isDisguised() ? HumanoidArm.RIGHT : super.getMainArm();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        entityData.get(DISGUISE_ID).ifPresent(id -> tag.putUUID("DisguiseId", id));
        if (!entityData.get(DISGUISE_NAME).isEmpty()) tag.putString("DisguiseName", entityData.get(DISGUISE_NAME));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("DisguiseId") && tag.contains("DisguiseName")) {
            entityData.set(DISGUISE_ID, Optional.of(tag.getUUID("DisguiseId")));
            entityData.set(DISGUISE_NAME, tag.getString("DisguiseName"));
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        LivingEntity user = getUser();
        if (isRemoteControlled() && user != null) {
            Vec3 userVelocity = user.getDeltaMovement();
            boolean damaged = user.hurt(source, amount);
            user.setDeltaMovement(userVelocity);
            if (damaged) {
                hurtTime = 10;
                hurtDuration = 10;
                level().broadcastEntityEvent(this, (byte) 2);
                if (source.getEntity() instanceof Mob attacker && !source.isIndirect()) {
                    knockback(0.4F, attacker.getX() - getX(), attacker.getZ() - getZ());
                }
                controlKnockbackTicks = 1;
                hurtMarked = true;
            }
            return damaged;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 2 && isRemoteControlled()) controlKnockbackTicks = 1;
    }

    @Override
    public boolean isPickable() {
        return isRemoteControlled() || super.isPickable();
    }

    @Override
    public boolean isAttackable() {
        return isRemoteControlled() || super.isAttackable();
    }

    @Override
    public boolean skipAttackInteraction(Entity attacker) {
        return !isRemoteControlled() && super.skipAttackInteraction(attacker);
    }

    @Override
    public boolean canBeHitByProjectile() {
        return isRemoteControlled() || super.canBeHitByProjectile();
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return !isRemoteControlled();
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return isRemoteControlled() ? MovementEmission.ALL : super.getMovementEmission();
    }

    @Override
    public boolean fireImmune() {
        return !isRemoteControlled();
    }

    @Override
    public boolean canBreatheUnderwater() {
        return !isRemoteControlled();
    }

    @Override
    public boolean isPushedByFluid() {
        return isRemoteControlled();
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        if (isRemoteControlled() && effect.getEffect() == ModEffects.HALLUCINATION) return false;
        return isRemoteControlled() || super.canBeAffected(effect);
    }

    @Override
    public boolean isControlledByLocalInstance() {
        LivingEntity user = getUser();
        if (user instanceof Player player) {
            if (((StandUser) player).roundabout$getStandPowers().isPiloting()) {
                LivingEntity controlled = ((StandUser) player).roundabout$getStandPowers().getPilotingStand();
                if (controlled != null && controlled.is(this)) {
                    return player.isLocalPlayer();
                }
            }
        }
        return super.isControlledByLocalInstance();
    }

    @Override
    public boolean isEffectiveAi() {
        return isAutoModeActive() ? !level().isClientSide() : !isRemoteControlled() && super.isEffectiveAi();
    }

    @Override
    public void travel(Vec3 movement) {
        if (isSnakeBiteActive()) {
            LivingEntity user = getUser();
            if (level().isClientSide() && isControlledByLocalInstance() && user != null
                    && ((StandUser) user).roundabout$getStandPowers() instanceof PowersWhitesnake powers) {
                Vec3 direction = getViewVector(0.0F).normalize();
                setPos(position().add(direction.scale(
                        PowersWhitesnake.snakeBiteTravelSpeed(powers.getAttackTimeDuring()))));
                C2SPacketUtil.updatePilot(this);
            }
            setDeltaMovement(Vec3.ZERO);
            return;
        }
        if (isAutoModeActive()) {
            super.travel(movement);
            return;
        }
        if (isRemoteControlled()) {
            if (level().isClientSide() && isControlledByLocalInstance()) {
                super.travel(new Vec3(controlStrafe, movement.y, controlForward));
                C2SPacketUtil.updatePilot(this);
            } else if (controlKnockbackTicks > 0) {
                super.travel(Vec3.ZERO);
            } else {
                Vec3 velocity = getDeltaMovement();
                setDeltaMovement(0.0D, velocity.y, 0.0D);
                super.travel(Vec3.ZERO);
            }
            return;
        }
        super.travel(movement);
    }

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        byte animation = getAnimation();
        boolean melting = isMeltingModeActive();
        boolean meltingIdle = melting && animation == IDLE;
        boolean meltingMoving = meltingIdle && this.walkAnimation.speed() > 0.01F;
        boolean meltingAcidToss = melting && animation == ACID_TOSS;

        meltingSwimBlendOld = meltingSwimBlend;
        meltingAcidTossBlendOld = meltingAcidTossBlend;
        if (!melting) {
            meltingSwimBlend = 0.0F;
            meltingAcidTossBlend = 0.0F;
        } else if (meltingAcidToss) {
            meltingAcidTossBlend = 1.0F;
            meltingSwimBlend = Math.max(0.0F, meltingSwimBlend - MELTING_ANIMATION_BLEND_STEP);
        } else {
            meltingAcidTossBlend = Math.max(0.0F,
                    meltingAcidTossBlend - MELTING_ANIMATION_BLEND_STEP);
            float swimTarget = meltingMoving && meltingAcidTossBlend == 0.0F ? 1.0F : 0.0F;
            meltingSwimBlend = Mth.clamp(meltingSwimBlend
                    + Mth.clamp(swimTarget - meltingSwimBlend,
                    -MELTING_ANIMATION_BLEND_STEP, MELTING_ANIMATION_BLEND_STEP), 0.0F, 1.0F);
        }

        if (melting && (meltingIdle || meltingAcidTossBlend > 0.0F)) {
            this.meltingIdle.startIfStopped(this.tickCount);
        }
        else this.meltingIdle.stop();
        if (melting && (meltingMoving || meltingSwimBlend > 0.0F)) {
            this.meltingSwim.startIfStopped(this.tickCount);
        }
        else this.meltingSwim.stop();
        if (melting) {
            if (meltingAcidToss || meltingAcidTossBlend > 0.0F) {
                this.acidToss.startIfStopped(this.tickCount);
            } else {
                this.acidToss.stop();
            }
        }

        if (animation != BARRAGE) this.hideFists.startIfStopped(this.tickCount);
        else this.hideFists.stop();
        if (animation == FINAL_ATTACK_WINDUP) this.finalChopWindup.startIfStopped(this.tickCount);
        else this.finalChopWindup.stop();
        if (animation == FINAL_ATTACK) this.finalChop.startIfStopped(this.tickCount);
        else this.finalChop.stop();
        if (animation == CHOP_ATTACK) this.finalChopHalf.startIfStopped(this.tickCount);
        else this.finalChopHalf.stop();
        if (animation == CHOP_CHARGED) this.finalChopCharged.startIfStopped(this.tickCount);
        else this.finalChopCharged.stop();
        if (animation == DISC_STEAL_WINDUP) this.discStealWindup.startIfStopped(this.tickCount);
        else this.discStealWindup.stop();
        if (animation == DISC_STEAL_RELEASE) this.discStealRelease.startIfStopped(this.tickCount);
        else this.discStealRelease.stop();
        if (!melting) {
            if (animation == ACID_TOSS) this.acidToss.startIfStopped(this.tickCount);
            else this.acidToss.stop();
        }
        if (animation == ITEM_GRAB) this.itemGrab.startIfStopped(this.tickCount);
        else this.itemGrab.stop();
        if (animation == ITEM_THROW) this.itemThrow.startIfStopped(this.tickCount);
        else this.itemThrow.stop();
        if (animation == ITEM_RETRACT) this.itemRetract.startIfStopped(this.tickCount);
        else this.itemRetract.stop();
        if (animation == IMPALE) this.impale.startIfStopped(this.tickCount);
        else this.impale.stop();
        if (animation == PHASE_GRAB) this.phaseGrab.startIfStopped(this.tickCount);
        else this.phaseGrab.stop();
        if (animation == SNAKE_BITE) this.snakeBite.startIfStopped(this.tickCount);
        else this.snakeBite.stop();
        if (animation == SNAKE_BITE_IMPACT) this.snakeBiteImpact.startIfStopped(this.tickCount);
        else this.snakeBiteImpact.stop();
    }
}
