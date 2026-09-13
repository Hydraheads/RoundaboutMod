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
            ANIME_PART_3_SILVER_CHARIOT = 1,
            ANIME_PART_3_SILVER_CHARIOT_GREY = 2,
            MANGA_PART_3_SILVER_CHARIOT = 3,
            PART_5_SILVER_CHARIOT = 4;

    public static final byte
            CONTROL_MODE_NONE = 0,
            CONTROL_MODE_SELF_CARRY = 1,
            CONTROL_MODE_REMOTE = 2;

    public static final byte
            IS_PART_3_SKIN = 1,
            IS_PART_5_SKIN = 2;

    public boolean isPart3Skin() {
        byte skin = this.getSkin();
        return skin == ANIME_PART_3_SILVER_CHARIOT || skin == ANIME_PART_3_SILVER_CHARIOT_GREY || skin == MANGA_PART_3_SILVER_CHARIOT;
    }

    public void toggleActiveHandAnimation() {
        if (isPart3Skin()) {
            this.setActiveHand(RIGHT_HAND);
            this.scToggleRightSword.startIfStopped(this.tickCount);
            this.scToggleLeftSword.stop();
        } else {
            this.setActiveHand(LEFT_HAND);
            this.scToggleLeftSword.startIfStopped(this.tickCount);
            this.scToggleRightSword.stop();
        }
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

    public static final byte
            RIGHT_HAND = 0,
            LEFT_HAND = 1;

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
    public final AnimationState scArmorShed = new AnimationState();
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

    public static final byte
            SC_ = 40,
            SC_BARRAGE_CHARGE = 42,
            SC_BARRAGE = 43,
            SC_ATTACK_1 = 44,
            SC_ATTACK_2 = 45,
            SC_ATTACK_3 = 46,
            SC_IDLE_1 = 47,
            SC_IDLE_2 = 48,
            SC_IDLE_3 = 49,
            SC_IDLE_4 = 50,
            SC_BARRAGE_DAMAGE = 52,
            SC_MINING = 53,
            SC_ARMOR_SHED = 54,
            SC_RAPIER_SHOT = 55,
            SC_FALL_BRACE = 56,
            SC_ARMOR_SHED_GUARD_BROKEN = 57,
            SC_VAULT = 58,
            SC_SELF_GRAB = 59,
            SC_SELF_THROW = 60,
            SC_RAPIER_SHOT_CHARGE = 61,
            SC_OFFHAND_WEAPON_SWIPE = 62,
            SC_TOGGLE_ARMOURLESS = 63,
            SC_TOGGLE_NO_RAPIER = 64,
            SC_TOGGLE_RIGHT_RAPIER = 65,
            SC_TOGGLE_LEFT_RAPIER = 66,
            SC_GUARD_HIT = 67,
            SC_IDLE = 68,
            SC_TOGGLE_ACTIVE_HAND = 69;

    public boolean isArmored = false;

    @Override
    public void setupAnimationStates() {
        byte animationState = getAnimation();
        byte skin = getSkin();

        if (this.getUser() != null) {
            if (animationState == IDLE) {
                if (this.getIdleAnimation() == 0) {

                } else {

                }

                if (this.getIdleAnimation() == 1) {
                    this.scPart3Pose.startIfStopped(this.tickCount);
                } else {

                }

                if (this.getIdleAnimation() == 2) {
                    this.scPart5Pose.startIfStopped(this.tickCount);
                } else {
                    this.scPart5Pose.stop();
                }
            }

            if (animationState == IDLE && this.getIdleAnimation() == 0 && this.getArmoured()) {
                this.scIdleArmoured.startIfStopped(this.tickCount);
            } else {
                this.scIdleArmoured.stop();
            }
            if (animationState == IDLE && this.getIdleAnimation() == 0 && !this.getArmoured()) {
                this.scIdleNotArmoured.startIfStopped(this.tickCount);
            } else {
                this.scIdleNotArmoured.stop();
            }
            if (animationState == IDLE && this.getIdleAnimation() == 1) {
                this.scPart3Pose.startIfStopped(this.tickCount);
            } else {
                this.scPart3Pose.stop();
            }
            if (animationState == IDLE && this.getIdleAnimation() == 2) {
                this.scPart5Pose.startIfStopped(this.tickCount);
            } else {
                this.scPart5Pose.stop();
            }

            if (animationState == BARRAGE) {
                if (getActiveHand() == RIGHT_HAND) {
                    this.scRightBarrage.startIfStopped(this.tickCount);
                } else {
                    this.scLeftBarrage.startIfStopped(this.tickCount);
                }
                this.scHideRapiers.stop();
            } else {
                if (getActiveHand() == RIGHT_HAND) {
                    this.scRightBarrage.stop();
                } else {
                    this.scLeftBarrage.stop();
                }
                this.scHideRapiers.startIfStopped(this.tickCount);
            }
            if (animationState == SC_TOGGLE_ACTIVE_HAND) {

            } else {

            }

            if (animationState == SC_TOGGLE_RIGHT_RAPIER) {
                this.scToggleRightSword.startIfStopped(this.tickCount);
                this.scToggleLeftSword.stop();
            }
            if (animationState == SC_TOGGLE_LEFT_RAPIER) {
                this.scToggleLeftSword.startIfStopped(this.tickCount);
                this.scToggleRightSword.stop();
            }
            if (animationState == SC_TOGGLE_NO_RAPIER) {
                this.scToggleNoRapier.startIfStopped(this.tickCount);
            } else {
                this.scToggleNoRapier.stop();
            }
            if (animationState == SC_TOGGLE_ARMOURLESS) {
                this.scToggleNotArmouredState.startIfStopped(this.tickCount);
            } else {
                this.scToggleNotArmouredState.stop();
            }
            if (animationState == SC_GUARD_HIT) {
                if (getActiveHand() == RIGHT_HAND) {
                    this.scGuardRightHit.startIfStopped(this.tickCount);
                } else {
                    this.scGuardLeftHit.startIfStopped(this.tickCount);
                }
            } else {
                if (getActiveHand() == RIGHT_HAND) {
                    this.scGuardRightHit.stop();
                } else {
                    this.scGuardLeftHit.stop();
                }
            }
            if (animationState == FIRST_PUNCH) {
                if (getActiveHand() == RIGHT_HAND) {
                    this.scRightHit1.startIfStopped(this.tickCount);
                } else {
                    this.scLeftHit1.startIfStopped(this.tickCount);
                }
            } else {
                if (getActiveHand() == RIGHT_HAND) {
                    this.scRightHit1.stop();
                } else {
                    this.scLeftHit1.stop();
                }
            }
            if (animationState == SECOND_PUNCH) {
                if (getActiveHand() == RIGHT_HAND) {
                    this.scRightHit2.startIfStopped(this.tickCount);
                } else {
                    this.scLeftHit2.startIfStopped(this.tickCount);
                }
            } else {
                if (getActiveHand() == RIGHT_HAND) {
                    this.scRightHit2.stop();
                } else {
                    this.scLeftHit2.stop();
                }
            }
            if (animationState == THIRD_PUNCH) {
                if (getActiveHand() == RIGHT_HAND) {
                    this.scRightHit3.startIfStopped(this.tickCount);
                } else {
                    this.scLeftHit3.startIfStopped(this.tickCount);
                }
            } else {
                if (getActiveHand() == RIGHT_HAND) {
                    this.scRightHit3.stop();
                } else {
                    this.scLeftHit3.stop();
                }
            }
            if (animationState == BLOCK) {
                // this.scGuard.startIfStopped(this.tickCount);
                if (getActiveHand() == RIGHT_HAND) {
                    this.scGuardRightStart.startIfStopped(this.tickCount);
                } else {
                    this.scGuardLeftStart.startIfStopped(this.tickCount);
                }
            } else {
                // this.scGuard.stop();
                if (getActiveHand() == RIGHT_HAND) {
                    this.scGuardRightStart.stop();
                } else  {
                    this.scGuardLeftStart.stop();
                }
            }
            if (animationState == BARRAGE_CHARGE) {
                if (getActiveHand() == RIGHT_HAND) {
                    this.scRightBarrageWindup.startIfStopped(this.tickCount);
                } else {
                    this.scLeftBarrageWindup.startIfStopped(this.tickCount);
                }
            } else {
                if (getActiveHand() == RIGHT_HAND) {
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
                if (getActiveHand() == RIGHT_HAND) {
                    this.scGuardRightBreak.startIfStopped(this.tickCount);
                } else {
                    this.scGuardLeftBreak.startIfStopped(this.tickCount);
                }
            } else {
                if (getActiveHand() == RIGHT_HAND) {
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
                if (getActiveHand() == RIGHT_HAND) {
                    this.scGuardRightBreak.startIfStopped(this.tickCount);
                } else {
                    this.scGuardLeftBreak.startIfStopped(this.tickCount);
                }
            } else {
                if (getActiveHand() == RIGHT_HAND) {
                    this.scGuardRightBreak.stop();
                } else {
                    this.scGuardLeftBreak.stop();
                }
            }
            if (animationState == SC_SELF_GRAB) {

            } else {

            }
            if (animationState == SC_SELF_THROW) {

            } else {

            }
            if (animationState == SC_ARMOR_SHED) {
                this.scArmorShed.startIfStopped(this.tickCount);
            } else {
                this.scArmorShed.stop();
            }
            if (animationState == SC_ARMOR_SHED_GUARD_BROKEN) {

            } else {

            }
            if (animationState == SC_RAPIER_SHOT) {

            } else {

            }
            if (animationState == SC_RAPIER_SHOT_CHARGE) {

            } else {

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
