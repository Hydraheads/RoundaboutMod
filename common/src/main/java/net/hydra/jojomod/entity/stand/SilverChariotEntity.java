package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersManhattanTransfer;
import net.hydra.jojomod.stand.powers.PowersSilverChariot;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SilverChariotEntity extends FollowingStandEntity {
    public SilverChariotEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public static final byte
            PART_3 = 1,
            PART_3_GREY = 2,
            PART_3_MANGA = 3,
            PART_5 = 4,
            AQUA = 5,
            AZURE = 6,
            BLUE = 7,
            CRYSTAL = 8,
            END_OF_THE_WORLD = 9,
            GENESIS_OF_THE_UNIVERSE = 10,
            JOJONIUM_A = 11,
            JOJONIUM_B = 12,
            JOJONIUM_C = 13,
            NIGHTMARE = 14,
            ORANGE = 15,
            PASSIONE = 16,
            PURPLE = 17,
            SANDY = 18,
            TURQUOISE = 19,
            YELLOW = 20;

    public static final byte
            CONTROL_MODE_NONE = 0,
            CONTROL_MODE_SELF_CARRY = 1,
            CONTROL_MODE_REMOTE = 2;

    public static final byte
            IS_PART_3_SKIN = 1,
            IS_PART_5_SKIN = 2;

    public boolean isPart5Skin() {
        byte skin = this.getSkin();
        return skin == PART_5;
    }

    private static final EntityDataAccessor<Byte> CONTROL_MODE = SynchedEntityData.defineId(
            SilverChariotEntity.class, EntityDataSerializers.BYTE
    );
    private static final EntityDataAccessor<Boolean> IS_ARMOURED = SynchedEntityData.defineId(
            SilverChariotEntity.class, EntityDataSerializers.BOOLEAN
    );
    private static final EntityDataAccessor<Boolean> HAS_RAPIER = SynchedEntityData.defineId(
            SilverChariotEntity.class, EntityDataSerializers.BOOLEAN
    );
    private static final EntityDataAccessor<Boolean> IS_FAKE = SynchedEntityData.defineId(
            SilverChariotEntity.class, EntityDataSerializers.BOOLEAN
    );

    public void setIsFake() {

    }

    private static final EntityDataAccessor<Boolean> IS_DUAL_WIELDING = SynchedEntityData.defineId(
            SilverChariotEntity.class, EntityDataSerializers.BOOLEAN
    );

    public static final byte
            RIGHT_HAND = 1,
            LEFT_HAND = 2,
            DUAL_WIELD = 3;

    private static final EntityDataAccessor<Byte> ACTIVE_HAND = SynchedEntityData.defineId(
            SilverChariotEntity.class, EntityDataSerializers.BYTE
    );

    public void setActiveHand(byte handValue) {
        if (this.entityData.hasItem(ACTIVE_HAND)) {
            this.entityData.set(ACTIVE_HAND, handValue);
        }
    }

    public byte getActiveHand() {
        if (this.entityData.hasItem(ACTIVE_HAND)) {
            return this.entityData.get(ACTIVE_HAND);
        }
        return RIGHT_HAND;
    }

    public void setArmoured(boolean armoured) {
        if (this.entityData.hasItem(IS_ARMOURED)) {
            this.entityData.set(IS_ARMOURED, armoured);
        }
    }

    public boolean getArmoured() {
        if (this.entityData.hasItem(IS_ARMOURED)) {
            return this.entityData.get(IS_ARMOURED);
        }
        return true;
    }

    private boolean armoured;
    private boolean isCarryingUser;
    private final int maxNoGravityCarryTicks = 100;
    private int noGravityCarryTicks = 0;

    public boolean isCarryingUser() {
        return isCarryingUser;
    }

    @Override
    public boolean standHasGravity() {
        return !isCarryingUser || noGravityCarryTicks > maxNoGravityCarryTicks;
    }

    private boolean controlDimensionsActive;

    public final AnimationState sc = new AnimationState();
    // public final AnimationState scBarrageCharge = new AnimationState();
    // public final AnimationState scBarrage = new AnimationState();
    public final AnimationState scBarrageDamage = new AnimationState();
    // public final AnimationState scGuard = new AnimationState();
    public final AnimationState scFallBrace = new AnimationState();
    public final AnimationState scPart3Pose = new AnimationState();
    public final AnimationState scPart5Pose = new AnimationState();
    public final AnimationState scIdleArmoured = new AnimationState();
    public final AnimationState scIdleNotArmoured = new AnimationState();
    public final AnimationState scArmourShed = new AnimationState();
    public final AnimationState scToggleNotArmouredState = new AnimationState();
    public final AnimationState scToggleNoRapier = new AnimationState();
    public final AnimationState scToggleRightSword = new AnimationState();
    public final AnimationState scToggleLeftSword = new AnimationState();
    public final AnimationState scGuardRightStart = new AnimationState();
    public final AnimationState scGuardRightHit = new AnimationState();
    public final AnimationState scGuardRightBreak = new AnimationState();
    public final AnimationState scGuardLeftStart = new AnimationState();
    public final AnimationState scGuardLeftHit = new AnimationState();
    public final AnimationState scGuardLeftBreak = new AnimationState();
    public final AnimationState scRightBarrageWindup = new AnimationState();
    public final AnimationState scRightBarrage = new AnimationState();
    public final AnimationState scLeftBarrageWindup = new AnimationState();
    public final AnimationState scLeftBarrage = new AnimationState();
    public final AnimationState scRightCombo = new AnimationState();
    public final AnimationState scRightHit1 = new AnimationState();
    public final AnimationState scRightHit2 = new AnimationState();
    public final AnimationState scRightHit3 = new AnimationState();
    public final AnimationState scLeftCombo = new AnimationState();
    public final AnimationState scLeftHit1 = new AnimationState();
    public final AnimationState scLeftHit2 = new AnimationState();
    public final AnimationState scLeftHit3 = new AnimationState();
    public final AnimationState scHideRapiers = new AnimationState();
    public final AnimationState scRightRapierSpin = new AnimationState();
    public final AnimationState scLeftRapierSpin = new AnimationState();
    public final AnimationState scDualRapierSpin = new AnimationState();
    public final AnimationState scRightRapierSlash = new AnimationState();
    public final AnimationState scLeftRapierSlash = new AnimationState();
    // public final AnimationState scDualRapierSlash = new AnimationState();
    public final AnimationState scLeftStatueCutting = new AnimationState();
    public final AnimationState scRightStatueCutting = new AnimationState();
    public final AnimationState scRightRapierShotHold = new AnimationState();
    public final AnimationState scRightRapierShotRelease = new AnimationState();
    public final AnimationState scLeftRapierShotHold = new AnimationState();
    public final AnimationState scLeftRapierShotRelease = new AnimationState();
    public final AnimationState scLeftVault = new AnimationState();
    public final AnimationState scRightVault = new AnimationState();
    public final AnimationState scRightOffhandSwipe = new AnimationState();
    public final AnimationState scLeftOffhandSwipe = new AnimationState();
    public final AnimationState scUserCarry = new AnimationState();
    public final AnimationState scUserCarryIdle = new AnimationState();
    public final AnimationState scUserThrow = new AnimationState();
    public final AnimationState scArmIdle = new AnimationState();
    public final AnimationState scHideRightArm = new AnimationState();
    public final AnimationState scHideLeftArm = new AnimationState();

    public static final byte
            SC_ = 40,
            SC_ARMOUR_SHED = 41,
            SC_RAPIER_SHOT_HOLD = 42,
            SC_RAPIER_SHOT_RELASE = 43,
            SC_FALL_BRACE = 44,
            SC_ARMOUR_SHED_GUARD_BROKEN = 45,
            SC_VAULT = 46,
            SC_USER_CARRY = 47,
            SC_USER_THROW = 48,
            SC_TOGGLE_ACTIVE_HAND = 49,
            SC_OFFHAND_WEAPON_SWIPE = 50,
            SC_TOGGLE_ARMOUR_OFF = 51,
            SC_TOGGLE_ARMOUR_ON = 52,
            SC_TOGGLE_NO_RAPIER = 53,
            SC_TOGGLE_RIGHT_RAPIER = 54,
            SC_TOGGLE_LEFT_RAPIER = 55,
            SC_GUARD_HIT = 56,
            SC_IDLE = 57,
            SC_ARM_SUMMON = 58,
            SC_RAPIER_SPIN = 59,
            SC_RAPIER_SLASH = 60,
            SC_STATUE_CUTTING = 61,
            SC_SLAB_CUTTING = 62,
            SC_USER_CARRY_IDLE = 63,
            SC_HIDE_RIGHT_ARM = 64,
            SC_HIDE_LEFT_ARM = 65;

    public boolean isArmored = false;

    @Override
    public void setupAnimationStates() {
        byte animationState = getAnimation();
        byte idle = getIdleAnimation();
        boolean isPart3Skin = getSkin() != PART_5;
        byte activeHand = getActiveHand();

        if (this.getUser() != null) {
            if (isPart3Skin) {
                this.scToggleRightSword.startIfStopped(this.tickCount);
                this.scToggleLeftSword.stop();
            } else {
                this.scToggleLeftSword.startIfStopped(this.tickCount);
                this.scToggleRightSword.stop();
            }

            if (animationState == BARRAGE) {
                if (isPart3Skin) {
                    this.scRightBarrage.startIfStopped(this.tickCount);
                } else {
                    this.scLeftBarrage.startIfStopped(this.tickCount);
                }
                this.scHideRapiers.stop();
            } else {
                if (isPart3Skin) {
                    this.scRightBarrage.stop();
                } else {
                    this.scLeftBarrage.stop();
                }
                this.scHideRapiers.startIfStopped(this.tickCount);
            }

            if (animationState == IDLE && idle == 0 && this.getArmoured()) {
                this.scIdleArmoured.startIfStopped(this.tickCount);
            } else {
                this.scIdleArmoured.stop();
            }
            if (animationState == IDLE && idle == 0 && !this.getArmoured()) {
                this.scIdleNotArmoured.startIfStopped(this.tickCount);
            } else {
                this.scIdleNotArmoured.stop();
            }
            if (animationState == IDLE && idle == 1) {
                this.scPart3Pose.startIfStopped(this.tickCount);
            } else {
                this.scPart3Pose.stop();
            }
            if (animationState == IDLE && idle == 2) {
                this.scPart5Pose.startIfStopped(this.tickCount);
            } else {
                this.scPart5Pose.stop();
            }

            if (animationState == SC_) {

            } else {

            }

            if (animationState == SC_RAPIER_SPIN) {
                if (isPart3Skin) {
                    this.scRightRapierSpin.startIfStopped(this.tickCount);
                } else {
                    this.scLeftRapierSpin.startIfStopped(this.tickCount);
                }
            } else {
                this.scRightRapierSpin.stop();
                this.scLeftRapierSpin.stop();
            }
            if (animationState == SC_RAPIER_SLASH) {
                if (isPart3Skin) {
                    this.scRightRapierSlash.startIfStopped(this.tickCount);
                } else {
                    this.scLeftRapierSlash.startIfStopped(this.tickCount);
                }
            } else {
                this.scRightRapierSlash.stop();
                this.scLeftRapierSlash.stop();
            }
            if (animationState == SC_OFFHAND_WEAPON_SWIPE) {
                if (isPart3Skin) {
                    this.scLeftOffhandSwipe.startIfStopped(this.tickCount);
                } else {
                    this.scToggleLeftSword.stop();
                    this.scLeftOffhandSwipe.startIfStopped(this.tickCount);
                }
            } else {
                this.scLeftOffhandSwipe.stop();
            }
            if (animationState == SC_ARM_SUMMON) {
                this.scArmIdle.startIfStopped(this.tickCount);
            } else {
                this.scArmIdle.stop();
            }
            if (animationState == SC_STATUE_CUTTING) {
                if (isPart3Skin) {
                    this.scRightStatueCutting.startIfStopped(this.tickCount);
                } else {
                    this.scLeftStatueCutting.startIfStopped(this.tickCount);
                }
            } else {
                if (isPart3Skin) {
                    this.scRightStatueCutting.stop();
                } else {
                    this.scLeftStatueCutting.stop();
                }
            }
            if (animationState == SC_SLAB_CUTTING) {
                if (isPart3Skin) {
                    this.scRightRapierSlash.startIfStopped(this.tickCount);
                } else {
                    this.scLeftRapierSlash.startIfStopped(this.tickCount);
                }
            } else {
                if (isPart3Skin) {
                    this.scRightRapierSlash.stop();
                } else {
                    this.scLeftRapierSlash.stop();
                }
            }
            if (animationState == SC_TOGGLE_ARMOUR_ON) {
                this.scToggleNotArmouredState.stop();
            }

            if (animationState == SC_GUARD_HIT) {
                if (isPart3Skin) {
                    this.scGuardRightHit.startIfStopped(this.tickCount);
                } else {
                    this.scGuardLeftHit.startIfStopped(this.tickCount);
                }
            } else {
                if (isPart3Skin) {
                    this.scGuardRightHit.stop();
                } else {
                    this.scGuardLeftHit.stop();
                }
            }
            if (animationState == FIRST_PUNCH) {
                if (isPart3Skin) {
                    this.scRightHit1.startIfStopped(this.tickCount);
                } else {
                    this.scLeftHit1.startIfStopped(this.tickCount);
                }
            } else {
                if (isPart3Skin) {
                    this.scRightHit1.stop();
                } else {
                    this.scLeftHit1.stop();
                }
            }
            if (animationState == SECOND_PUNCH) {
                if (isPart3Skin) {
                    this.scRightHit2.startIfStopped(this.tickCount);
                } else {
                    this.scLeftHit2.startIfStopped(this.tickCount);
                }
            } else {
                if (isPart3Skin) {
                    this.scRightHit2.stop();
                } else {
                    this.scLeftHit2.stop();
                }
            }
            if (animationState == THIRD_PUNCH) {
                if (isPart3Skin) {
                    this.scRightHit3.startIfStopped(this.tickCount);
                } else {
                    this.scLeftHit3.startIfStopped(this.tickCount);
                }
            } else {
                if (isPart3Skin) {
                    this.scRightHit3.stop();
                } else {
                    this.scLeftHit3.stop();
                }
            }
            if (animationState == BLOCK) {
                // this.scGuard.startIfStopped(this.tickCount);
                if (isPart3Skin) {
                    this.scGuardRightStart.startIfStopped(this.tickCount);
                } else {
                    this.scGuardLeftStart.startIfStopped(this.tickCount);
                }
            } else {
                // this.scGuard.stop();
                if (isPart3Skin) {
                    this.scGuardRightStart.stop();
                } else  {
                    this.scGuardLeftStart.stop();
                }
            }
            if (animationState == BARRAGE_CHARGE) {
                if (isPart3Skin) {
                    this.scRightBarrageWindup.startIfStopped(this.tickCount);
                } else {
                    this.scLeftBarrageWindup.startIfStopped(this.tickCount);
                }
            } else {
                if (isPart3Skin) {
                    this.scRightBarrageWindup.stop();
                } else {
                    this.scLeftBarrageWindup.stop();
                }
            }
            if (animationState == HURT_BY_BARRAGE) {
                this.scBarrageDamage.startIfStopped(this.tickCount);
            } else {
                this.scBarrageDamage.stop();
            }
            if (animationState == BROKEN_GUARD) {
                if (isPart3Skin) {
                    this.scGuardRightBreak.startIfStopped(this.tickCount);
                } else {
                    this.scGuardLeftBreak.startIfStopped(this.tickCount);
                }
            } else {
                if (isPart3Skin) {
                    this.scGuardRightBreak.stop();
                } else {
                    this.scGuardLeftBreak.stop();
                }
            }
            if (animationState == SC_FALL_BRACE) {
                this.scFallBrace.startIfStopped(this.tickCount);
            } else {
                this.scFallBrace.stop();
            }

            if (animationState == SC_VAULT) {
                if (isPart3Skin) {
                    this.scRightVault.startIfStopped(this.tickCount);
                } else {
                    this.scLeftVault.startIfStopped(this.tickCount);
                }
            } else {
                if (isPart3Skin) {
                    this.scRightVault.stop();
                } else {
                    this.scLeftVault.stop();
                }
            }
            if (animationState == SC_USER_CARRY) {
                this.scUserCarry.startIfStopped(this.tickCount);
            } else {
                this.scUserCarry.stop();
            }
            if (animationState == SC_USER_CARRY_IDLE) {
                this.scUserCarryIdle.startIfStopped(this.tickCount);
            } else {
                this.scUserCarryIdle.stop();
            }
            if (animationState == SC_USER_THROW) {
                this.scUserThrow.startIfStopped(this.tickCount);
            } else {
                this.scUserThrow.stop();
            }

            if (animationState == SC_ARMOUR_SHED) {
                this.scArmourShed.startIfStopped(this.tickCount);
                this.scToggleNotArmouredState.startIfStopped(this.tickCount);
            } else {
                this.scArmourShed.stop();
            }
            if (animationState == SC_ARMOUR_SHED_GUARD_BROKEN) {

            } else {

            }
            if (animationState == SC_RAPIER_SHOT_HOLD) {
                if (isPart3Skin) {
                    this.scRightRapierShotHold.startIfStopped(this.tickCount);
                } else {
                    this.scLeftRapierShotHold.startIfStopped(this.tickCount);
                }
            } else {
                if (isPart3Skin) {
                    this.scRightRapierShotHold.stop();
                } else {
                    this.scLeftRapierShotHold.stop();
                }
            }
            if (animationState == SC_RAPIER_SHOT_RELASE) {
                if (isPart3Skin) {
                    this.scRightRapierShotRelease.startIfStopped(this.tickCount);
                } else {
                    this.scLeftRapierShotRelease.startIfStopped(this.tickCount);
                }
                this.scToggleNoRapier.startIfStopped(this.tickCount);
            } else {
                if (isPart3Skin) {
                    this.scRightRapierShotRelease.stop();
                } else {
                    this.scLeftRapierShotRelease.stop();
                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(CONTROL_MODE, CONTROL_MODE_NONE);
        entityData.define(IS_ARMOURED, true);
        entityData.define(HAS_RAPIER, true);
        entityData.define(ACTIVE_HAND, RIGHT_HAND);
        entityData.define(IS_FAKE, false);
        entityData.define(IS_DUAL_WIELDING, false);
    }

    private float controlStrafe;
    private float controlForward;

    private boolean controlBodyRotationActive;
    private float controlBodyYaw;

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

    @Override
    public boolean isRemoteControlled() {
        // return entityData.get(CONTROL_MODE) != CONTROL_MODE_NONE;
        return super.isRemoteControlled();
    }

    @Override
    public void travel(Vec3 vec3) {
        // TODO: Remove the teleporting camera for control mode when moving out of max range, as suggested by DOGael.
        super.travel(vec3);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void tickRidden(Player $$0, Vec3 $$1) {
        super.tickRidden($$0, $$1);
    }

    @Override
    public double getMyRidingOffset() {
        return super.getMyRidingOffset();
    }

    @Override
    public void rideTick() {
        super.rideTick();
    }

    @Override
    protected void positionRider(Entity $$0, MoveFunction $$1) {
        super.positionRider($$0, $$1);
    }

    @Override
    protected boolean canRide(Entity $$0) {
        return super.canRide($$0);
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset();
    }

    @Override
    protected boolean couldAcceptPassenger() {
        return super.couldAcceptPassenger();
    }

    @Override
    protected boolean canAddPassenger(Entity $$0) {
        return super.canAddPassenger($$0);
    }

    @Override
    public boolean canBeHitByStands() {
        return super.canBeHitByStands();
    }

    @Override
    public boolean canBreatheUnderwater() {
        return super.canBreatheUnderwater();
    }

    @Override
    protected Vec3 getRiddenInput(Player $$0, Vec3 $$1) {
        return super.getRiddenInput($$0, $$1);
    }

    @Override
    protected float getRiddenSpeed(Player $$0) {
        return super.getRiddenSpeed($$0);
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return !isRemoteControlled();
    }

    @Override
    public boolean fireImmune() {
        return !isRemoteControlled();
    }

    @Override
    public boolean skipAttackInteraction(Entity attacker) {
        return super.skipAttackInteraction(attacker);
    }

    public boolean isControlModeActive() {
        return entityData.get(CONTROL_MODE) == CONTROL_MODE_REMOTE;
    }

    @Override
    public boolean hasNoPhysics() {
        return !isRemoteControlled();
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
    public HumanoidArm getMainArm() {
        return super.getMainArm();
    }

    @Override
    public void setItemInHand(InteractionHand $$0, ItemStack $$1) {
        super.setItemInHand($$0, $$1);
    }

    @Override
    public ItemStack getOffhandItem() {
        return super.getOffhandItem();
    }

    @Override
    public ItemStack getMainHandItem() {
        return super.getMainHandItem();
    }

    @Override
    public boolean lockPos() {
        return !isRemoteControlled();
    }

    @Override
    public boolean canBeHitByProjectile() {
        // if (this.getUserData(this.getUser()) != null) {
        // if (this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersSilverChariot PSC) {
        // if (isDesummoning) {
        //    return false;
        // }
        // }
        // }
        return isRemoteControlled() && super.canBeHitByProjectile();
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
    protected float getFlyingSpeed() {
        return 0.20F;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return super.hurt(source, amount);
    }
}
