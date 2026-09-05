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
            DEFAULT_SILVER_CHARIOT = 1;

    public static final byte
            CONTROL_MODE_NONE = 0,
            CONTROL_MODE_SELF_CARRY = 1,
            CONTROL_MODE_REMOTE = 2;

    private static final EntityDataAccessor<Byte> CONTROL_MODE = SynchedEntityData.defineId(
            SilverChariotEntity.class, EntityDataSerializers.BYTE
    );
    private static final EntityDataAccessor<Boolean> IS_ARMOURED = SynchedEntityData.defineId(
            SilverChariotEntity.class, EntityDataSerializers.BOOLEAN
    );
    private static final EntityDataAccessor<Boolean> HAS_RAPIER = SynchedEntityData.defineId(
            SilverChariotEntity.class, EntityDataSerializers.BOOLEAN
    );

    private boolean armoured;
    private boolean isCarryingUser;
    private final int maxGravityFreeCarryTicks = 100;
    private int gravityFreeCarryTicks = 0;

    public boolean isCarryingUser() {
        return isCarryingUser;
    }

    @Override
    public boolean standHasGravity() {
        return !isCarryingUser || gravityFreeCarryTicks > maxGravityFreeCarryTicks;
    }

    private boolean controlDimensionsActive;

    public final AnimationState sc = new AnimationState();
    public final AnimationState scBarrageCharge = new AnimationState();
    public final AnimationState scBarrage = new AnimationState();
    public final AnimationState scBarrageDamage = new AnimationState();
    public final AnimationState scBlock = new AnimationState();
    public final AnimationState scFallBrace = new AnimationState();

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
            SC_OFFHAND_WEAPON_SWIPE = 62;

    public boolean isArmored = false;

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        byte animationState = getAnimation();

        if (this.getUser() != null) {
            if (animationState == BLOCK) {
                this.scBlock.startIfStopped(this.tickCount);
            } else {
                this.scBlock.stop();
            }
            if (animationState == BARRAGE_CHARGE) {
                this.scBarrageCharge.startIfStopped(this.tickCount);
            } else {
                this.scBarrageCharge.stop();
            }
            if (animationState == BARRAGE) {
                this.scBarrage.startIfStopped(this.tickCount);
            } else {
                this.scBarrage.stop();
            }
            if (animationState == HURT_BY_BARRAGE) {
                this.scBarrageDamage.startIfStopped(this.tickCount);
            } else {
                this.scBarrageDamage.stop();
            }
            if (animationState == MINING_BARRAGE) {

            } else {

            }
            if (animationState == BROKEN_GUARD) {

            } else {

            }
            if (animationState == SC_FALL_BRACE) {
                this.scFallBrace.startIfStopped(this.tickCount);
            } else {
                this.scFallBrace.stop();
            }

            if (animationState == SC_) {

            } else {

            }

            if (animationState == SC_VAULT) {

            } else {

            }
            if (animationState == SC_SELF_GRAB) {

            } else {

            }
            if (animationState == SC_SELF_THROW) {

            } else {

            }
            if (animationState == SC_ARMOR_SHED) {

            } else {

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
        if (this.getUser() instanceof Player PE && this.level().isClientSide()) {
            C2SPacketUtil.updatePilot(this);
        }
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
        return !isRemoteControlled() && super.skipAttackInteraction(attacker);
    }

    public boolean isControlModeActive() {
        return entityData.get(CONTROL_MODE) == CONTROL_MODE_REMOTE;
    }

    @Override
    public boolean hasNoPhysics() {
        return true;
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
        return isRemoteControlled() || super.canBeHitByProjectile();
    }

    @Override
    public boolean isControlledByLocalInstance() {
        LivingEntity user =  this.getUser();
        if (user != null){
            Entity ent =  this.getUserData(user).roundabout$getStandPowers().getPilotingStand();
            if (ent != null && ent.is(this)){
                return (user instanceof Player $$0 ? $$0.isLocalPlayer() : this.isEffectiveAi());
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
