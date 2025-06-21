package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.access.NoVibrationEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.mixin.WorldTickClient;
import net.hydra.jojomod.mixin.WorldTickServer;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class StandEntity extends Mob implements NoVibrationEntity {
    /**The entity code for a stand. Not to be confused with StandPowers, which contain
     * the actual ability data of stands, this code exists more for the physical
     * entities.*/

    /**MaxFade and FADE_OUT control a stand become less and less transparent as it is
     * summoned. When a stand completely fades out, it despawns.*/
    public final int MaxFade = 8;

    public boolean lockPos(){
        return true;
    }
    public float fadePercent =0F;
    protected static final EntityDataAccessor<Integer> FADE_PERCENT = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Byte> FADE_OUT = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Integer> ANCHOR_PLACE = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> ANCHOR_PLACE_ATTACK = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.INT);

    /**The data of stand leaning from player inputs, might go to the user itself at some point.*/
    protected static final EntityDataAccessor<Byte> MOVE_FORWARD = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.BYTE);

    /**OFFSET_TYPE specifies if the stand is floating by your side,
     * facing your direction, or detached on its own, for instance.*/
    protected static final EntityDataAccessor<Byte> OFFSET_TYPE = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.BYTE);

    /**USER_ID is the mob id of the stand's user. Needs to be stored as an int,
     * because clients do not have access to UUIDS.*/
    protected static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.INT);

    /**FOLLOWING_ID is the mob the stand is floating by. This does not have to be
     * the user, for instance, if a stand like killer queen is planted in someone else.*/
    protected static final EntityDataAccessor<Integer> FOLLOWING_ID = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.INT);

    /**The animation number playing on the entity. The number is technically arbitrary,
     * as this file defines what each value plays on a per stand basis and can be overridden.*/
    protected static final EntityDataAccessor<Byte> ANIMATION = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Byte> IDLE_ANIMATION = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.BYTE);

    /**When stands grab and throw items, for instance, they hold this*/
    protected static final EntityDataAccessor<ItemStack> HELD_ITEM = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.ITEM_STACK);
    protected static final EntityDataAccessor<Byte> SKIN = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.BYTE);

    protected static final EntityDataAccessor<Float> DISTANCE_OUT = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.FLOAT);

    protected static final EntityDataAccessor<Float> SIZE_PERCENT = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> IDLE_ROTATION = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> IDLE_Y_OFFSET = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.FLOAT);

    private byte lastOffsetType = 0;
    public boolean canAcquireHeldItem = false;

    /**This rotation data is for the model rotating when you look certain directions,
     * punch, etc. As it is dynamically calculated, it has to be stored somewhere.*/
    public float bodyRotationX =0;
    public float bodyRotationY =0;
    public float headRotationX =0;
    public float headRotationY =0;
    public float standRotationX =0;
    public float standRotationY =0;
    public float standRotationZ =0;

    /**isDisplay could theoretically be data set on a stand to not be
     * faded out if it doesnt have a user. It is not
     * worked on much yet.*/
    private boolean isDisplay;

    /**Like UserID and FollowingID, but for the actual entity data.*/
    @Nullable
    private LivingEntity User;
    @Nullable
    private LivingEntity Following;

    /**No sculker noises*/
    @Override
    public boolean getVibration(){
        return false;
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    public void setUser(LivingEntity StandSet){
        this.User = StandSet;
        int standSetId = -1;
        if (StandSet != null){
            standSetId = StandSet.getId();
        }
        this.entityData.set(USER_ID, standSetId);

        this.setFollowing(StandSet);
    }

    public void setFollowing(LivingEntity StandSet){
        this.Following = StandSet;
        int standSetId = -1;
        if (StandSet != null){
            standSetId = StandSet.getId();
        }
        this.entityData.set(FOLLOWING_ID, standSetId);
    }

    public static final byte
            IDLE = 0,
            FIRST_PUNCH = 1,
            SECOND_PUNCH = 2,
            THIRD_PUNCH = 3,
            BLOCK = 10,
            BARRAGE_CHARGE = 11,
            BARRAGE = 12,
            BARRAGE_FINISHER = 13,
            HURT_BY_BARRAGE = 14,
            BROKEN_GUARD = 15,
            MINING_BARRAGE = 16,
            LEAP = 17,
            LEAP_END = 18,
            TIME_STOP = 30,
            TIME_STOP_RELEASE = 31,
            BLOCK_GRAB = 32,
            BLOCK_THROW = 33,
            ITEM_GRAB = 34,
            ITEM_THROW = 35,
            BLOCK_RETRACT = 36,
            ITEM_RETRACT = 37,
            ENTITY_GRAB = 38,
            KICK_BARRAGE_WINDUP = 42,
            KICK_BARRAGE_END = 43,
            IMPALE = 81,
            KICK_BARRAGE = 80,
            FINAL_ATTACK_WINDUP = 85,
            FINAL_ATTACK = 86,
            PHASE_GRAB = 87;

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState idleAnimationState2 = new AnimationState();
    public final AnimationState idleAnimationState3 = new AnimationState();
    public final AnimationState idleAnimationState4 = new AnimationState();
    public final AnimationState punchState1 = new AnimationState();
    public final AnimationState punchState2 = new AnimationState();
    public final AnimationState punchState3 = new AnimationState();
    public final AnimationState blockAnimationState = new AnimationState();
    public final AnimationState blockLoinAnimationState = new AnimationState();
    public final AnimationState barrageChargeAnimationState = new AnimationState();
    public final AnimationState barrageAnimationState = new AnimationState();
    public final AnimationState miningBarrageAnimationState = new AnimationState();
    public final AnimationState barrageEndAnimationState = new AnimationState();
    public final AnimationState barrageHurtAnimationState = new AnimationState();
    public final AnimationState brokenBlockAnimationState = new AnimationState();
    public final AnimationState standLeapAnimationState = new AnimationState();
    public final AnimationState standLeapEndAnimationState = new AnimationState();
    public final AnimationState armlessAnimation = new AnimationState();
    public final AnimationState armlessAnimationIdle = new AnimationState();

    /**Override this to define animations. Above are animation states defined.*/
    public void setupAnimationStates() {
        if (this.getUser() != null) {
            byte idle = getIdleAnimation();
            byte animation = getAnimation();
            if (animation == IDLE && idle == 1) {
                this.idleAnimationState2.startIfStopped(this.tickCount);
            } else {
                this.idleAnimationState2.stop();
            }
            if (animation == IDLE && idle == 0) {
                this.idleAnimationState.startIfStopped(this.tickCount);
            } else {
                this.idleAnimationState.stop();
            }
            if (animation == IDLE && idle == 2) {
                this.idleAnimationState3.startIfStopped(this.tickCount);
            } else {
                this.idleAnimationState3.stop();
            }
            if (animation == IDLE && idle == 3) {
                this.idleAnimationState4.startIfStopped(this.tickCount);
            } else {
                this.idleAnimationState4.stop();
            }
            if (idle == 4) {
                if (animation == IDLE){
                    this.armlessAnimationIdle.startIfStopped(this.tickCount);
                    this.armlessAnimation.stop();
                } else {
                    this.armlessAnimation.startIfStopped(this.tickCount);
                    this.armlessAnimationIdle.stop();
                }
            } else {
                this.armlessAnimationIdle.stop();
                this.armlessAnimation.stop();
            }

            if (animation == FIRST_PUNCH)
                this.punchState1.startIfStopped(this.tickCount);
            else
                this.punchState1.stop();

            if (animation == SECOND_PUNCH)
                this.punchState2.startIfStopped(this.tickCount);
            else
                this.punchState2.stop();

            if (animation == THIRD_PUNCH)
                this.punchState3.startIfStopped(this.tickCount);
            else
                this.punchState3.stop();

            if (animation == BLOCK)
                this.blockAnimationState.startIfStopped(this.tickCount);
            else
                this.blockAnimationState.stop();

            if (animation == BARRAGE_CHARGE)
              this.barrageChargeAnimationState.startIfStopped(this.tickCount);
            else
                this.barrageChargeAnimationState.stop();

            if (animation == BARRAGE)
                this.barrageAnimationState.startIfStopped(this.tickCount);
            else
                this.barrageAnimationState.stop();

            if (animation == BARRAGE_FINISHER)
                this.barrageEndAnimationState.startIfStopped(this.tickCount);
            else
                this.barrageEndAnimationState.stop();

            if (animation == HURT_BY_BARRAGE)
                this.barrageHurtAnimationState.startIfStopped(this.tickCount);
            else
                this.barrageHurtAnimationState.stop();

            if (animation == BROKEN_GUARD)
                this.brokenBlockAnimationState.startIfStopped(this.tickCount);
            else
                this.brokenBlockAnimationState.stop();

            if (animation == MINING_BARRAGE)
                this.miningBarrageAnimationState.startIfStopped(this.tickCount);
            else
                this.miningBarrageAnimationState.stop();

            if (animation == LEAP)
                this.standLeapAnimationState.startIfStopped(this.tickCount);
            else
                this.standLeapAnimationState.stop();

            if (animation == LEAP_END)
                this.standLeapEndAnimationState.startIfStopped(this.tickCount);
            else
                this.standLeapEndAnimationState.stop();
        }
    }

    public boolean forceVisible = false;


    public final byte getMoveForward() {
        return this.entityData.get(MOVE_FORWARD);
    } //returns leaning direction

    public final byte getOffsetType() {
        if (this.level().isClientSide()){
            if (ClientUtil.getScreenFreeze()){
                return this.lastOffsetType;
            }
        }
        return this.entityData.get(OFFSET_TYPE);
    } //returns leaning direction

    public final void setOffsetType(byte oft) {
        this.entityData.set(OFFSET_TYPE, oft);
    }

    public final void setAnimation(byte animation) {
        this.entityData.set(ANIMATION, animation);
    }

    public final void setIdleAnimation(byte animation) {
        this.entityData.set(IDLE_ANIMATION, animation);
    }

    public final void setSkin(byte skin) {
        this.entityData.set(SKIN, skin);
    }


    public boolean dismountOnHit(){
        return true;
    }

    public boolean canRestrainWhileMounted(){
        return true;
    }

    public final void setHeldItem(ItemStack stack) {
        this.entityData.set(HELD_ITEM, stack);
    }

    /**
     * Presently, this is how the stand knows to lean in any direction based on player movement.
     * Creates the illusion of floaty movement within the stand.
     * Relevant in stand model code:
     *
     */
    public final void setMoveForward(Byte MF) {
        this.entityData.set(MOVE_FORWARD, MF);
    } //sets leaning direction

    public byte getMaxFade() {
        return MaxFade;
    }

    public final void setFadeOut(Byte FadeOut) {
        this.entityData.set(FADE_OUT, FadeOut);
    } //sets leaning direction
    public int getFadeOut() {
        return this.entityData.get(FADE_OUT);
    }

    public final void setFadePercent(Integer FadeOut) {
        this.entityData.set(FADE_PERCENT, FadeOut);
    } //sets leaning direction
    public int getFadePercent() {
        return this.entityData.get(FADE_PERCENT);
    }

    public final int getAnchorPlace() {
        return this.entityData.get(ANCHOR_PLACE);
    }

    public final int getAnchorPlaceAttack() {
        return this.entityData.get(ANCHOR_PLACE_ATTACK);
    }

    public boolean fireImmune() {
        return true;
    }

    public final byte getAnimation() {
        return this.entityData.get(ANIMATION);
    }

    public final byte getIdleAnimation() {
        return this.entityData.get(IDLE_ANIMATION);
    }

    public final ItemStack getHeldItem() {
        return this.entityData.get(HELD_ITEM);
    }

    public final byte getSkin() {
        return this.entityData.get(SKIN);
    }

    public final boolean getNeedsUser() {
        return true;
    }

    public float getBodyRotationX() {
        return this.bodyRotationX;
    }
    public float getBodyRotationY() {
        return this.bodyRotationY;
    }

    public void setBodyRotationX(float bodRot) {
        this.bodyRotationX = bodRot;
    }
    public void setBodyRotationY(float bodRot) {
        this.bodyRotationY = bodRot;
    }
    public float getHeadRotationX() {
        return this.headRotationX;
    }
    public float getHeadRotationY() {
        return this.headRotationY;
    }

    public void setHeadRotationX(float bodRot) {
        this.headRotationX = bodRot;
    }
    public void setHeadRotationY(float bodRot) {
        this.headRotationY = bodRot;
    }
    public float getStandRotationX() {
        return this.standRotationX;
    }
    public float getStandRotationY() {
        return this.standRotationY;
    }
    public float getStandRotationZ() {
        return this.standRotationZ;
    }

    public void setStandRotationX(float bodRot) {
        this.standRotationX = bodRot;
    }
    public void setStandRotationY(float bodRot) {
        this.standRotationY = bodRot;
    }
    public void setStandRotationZ(float bodRot) {
        this.standRotationZ = bodRot;
    }

    /**
     * This is called when setting the anchor place of a stand, which is to say whether it will position itself
     * next to the player to the left, right, front, back, or anywhere in between. Players individually can set
     * this to accommodate their style and FOV settings. Purely cosmetic, as stands will teleport before taking
     * actions..
     */
    public final void setAnchorPlace(Integer degrees) {
        this.entityData.set(ANCHOR_PLACE, degrees);
    }
    public final void setAnchorPlaceAttack(Integer degrees) {
        this.entityData.set(ANCHOR_PLACE_ATTACK, degrees);
    }

    public void playerSetProperties(Player PE) {
    }
    public final void setDistanceOut(float blocks) {
        this.entityData.set(DISTANCE_OUT, blocks);
    }
    public final float getDistanceOut() {
        return this.entityData.get(DISTANCE_OUT);
    }
    public final void setSizePercent(float blocks) {
        this.entityData.set(SIZE_PERCENT, blocks);
    }
    public final float getSizePercent() {
        return this.entityData.get(SIZE_PERCENT);
    }
    public final void setIdleRotation(float blocks) {
        this.entityData.set(IDLE_ROTATION, blocks);
    }
    public final float getIdleRotation() {
        return this.entityData.get(IDLE_ROTATION);
    }
    public final void setIdleYOffset(float blocks) {
        this.entityData.set(IDLE_Y_OFFSET, blocks);
    }
    public final float getIdleYOffset() {
        return this.entityData.get(IDLE_Y_OFFSET);
    }

    /**
     * These functions tell the game if the stand's user is Swimming, Crawling, or Elytra Flying.
     * Currently used for idle animation variants.
     *
     */
    @Override
    public boolean isSwimming() {
        if (this.getUser() != null) {
            return this.getUser().isSwimming();
        } else {
            return false;
        }
    }

    @Override
    public boolean isVisuallyCrawling() {
        if (this.getUser() != null) {
            return this.getUser().isVisuallyCrawling();
        } else {
            return false;
        }
    }

    @Override
    public boolean isFallFlying() {
        if (this.getUser() != null) {
            return (this.getUser()).isFallFlying();
        } else {
            return false;
        }
    }

    public boolean getDisplay() {
        return this.isDisplay;
    }

    public void setDisplay(boolean display) {
        this.isDisplay = display;
    }


    /**
     * Controls the visibility of stands, specifically their fading in or out when summoned.
     * Potentially can be used to make stand blink on the verge of death?
     *
     * see StandRenderer#getStandOpacity
     * <p>
     * When a stand hits negative opacity, it automatically despawns
     * @see #TickDown
     */
    public void incFadeOut(byte inc) {
        this.setFadeOut((byte) (this.getFadeOut()+inc));
    }

    /**
     * These are not components as they are simpler variables to store/load than entities.
     * It would be nice if move forward were to be less packet intensive.
     */
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANCHOR_PLACE, 55);
        this.entityData.define(ANCHOR_PLACE_ATTACK, 55);
        this.entityData.define(DISTANCE_OUT, 1.07F);
        this.entityData.define(SIZE_PERCENT, 1F);
        this.entityData.define(IDLE_ROTATION, 0F);
        this.entityData.define(IDLE_Y_OFFSET, 0.1F);
        this.entityData.define(FADE_OUT, (byte) 0);
        this.entityData.define(FADE_PERCENT, 100);
        this.entityData.define(MOVE_FORWARD, (byte) 0);
        this.entityData.define(OFFSET_TYPE, (byte) 0);
        this.entityData.define(USER_ID, -1);
        this.entityData.define(FOLLOWING_ID, -1);
        this.entityData.define(ANIMATION, (byte) 0);
        this.entityData.define(IDLE_ANIMATION, (byte) 0);
        this.entityData.define(HELD_ITEM, ItemStack.EMPTY);
        this.entityData.define(SKIN, (byte) 0);
    }


    /**
     * Initialize Stands
     */
    protected StandEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
        //FadeOut = 10;
    }




    public boolean hasUser() {
        return this.getUser() != null;
    } //returns IF stand has a master


    public StandUser getUserData(LivingEntity User) {
        return ((StandUser) User);
    }


    /**
     * Sets stand User, the mob who "owns" the stand
     */
    public void setMaster(LivingEntity Master) {
        this.setUser(Master);
    }

    /**
     * Unused, will be used to lock a stand onto another mob.
     * Use case example: Killer Queen BTD following someone else
     * Code to tick on follower will be needed in client world mixin
     */

    public LivingEntity getFollowing() {
        if (this.level().isClientSide){
            return (LivingEntity) this.level().getEntity(this.entityData.get(FOLLOWING_ID));
        } else {
            if (this.Following != null && this.Following.isRemoved()){
                this.setFollowing(null);
            }
            return this.Following;
        }
    }

    /**
     * Sets stand User, the mob who "owns" the stand
     */
    public LivingEntity getUser() {
        if (this.level().isClientSide){
            return (LivingEntity) this.level().getEntity(this.entityData.get(USER_ID));
        } else {
            return this.User;
        }
    }

    public UUID validationUUID = null;
    public void validateUUID(){
        if (!this.level().isClientSide) {
            if (this.getUser() != null && this.getUser() instanceof Player PE) {
                if (validationUUID == null) {
                    validationUUID = PE.getUUID();
                } else {
                    Player ZE = this.level().getPlayerByUUID(validationUUID);
                    if (ZE != null){
                        if (!ZE.is(PE) || ZE.getId() != PE.getId()){
                            this.discard();
                        }
                    }
                }
            }
        }
    }

    /**
     * When this is called, sets the User's owned stand to this one. Both the Stand and the User store
     * each other, and this is for setting the User's storage.
     *
     * @see net.hydra.jojomod.event.powers.StandUser#roundabout$setStand
     */
    public boolean startStandRiding(LivingEntity entity, boolean force) {
        ((StandUser) entity).roundabout$setStand(this);
        return true;
        //RoundaboutMod.LOGGER.info("MF");
    }

    /**
     * Called every tick in
     *
     * @see WorldTickClient for the client and
     * @see WorldTickServer for the server.
     * Basically, this lets the user/followee tick the stand so that it moves exactly with them.
     * The main purpose for this is to make the smooth visual effect of being ridden.
     * Also, if a stand is perfectly still, it's possible it is just not ticking due to not being mounted properly
     * with a follower.
     */
    public void tickStandOut() {


            byte ot = this.getOffsetType();
            if (lockPos()) {
                this.setDeltaMovement(Vec3.ZERO);
            }
            this.tick();
            if (this.getFollowing() == null) {
                return;
            }

            if (!(OffsetIndex.OffsetStyle(ot) == OffsetIndex.LOOSE_STYLE) || this.isControlledByLocalInstance() ) {
                ((StandUser) this.getFollowing()).roundabout$updateStandOutPosition(this);
            }
    }

    public void tickStandOut2() {
        byte ot = this.getOffsetType();
        if (lockPos()) {
            this.setDeltaMovement(Vec3.ZERO);
        }
        if (this.getFollowing() == null) {
            return;
        }
        if (!(OffsetIndex.OffsetStyle(ot) == OffsetIndex.LOOSE_STYLE) || this.isControlledByLocalInstance() ) {
            ((StandUser) this.getFollowing()).roundabout$updateStandOutPosition(this);
        }
    }


    /** Stand does not take damage under normal circumstances.*/
    public boolean hurt(DamageSource source, float amount) {
        if (this.getUser() != null && MainUtil.isStandDamage(source)){
            return this.getUser().hurt(source,amount);
        }
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        return false;
    }
    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }
    @Override
    public boolean isAttackable() {
        return false;
    }
    @Override
    public boolean skipAttackInteraction(Entity $$0) {
        return true;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canCollideWith(Entity $$0) {
        return false;
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    /** This happens every tick. Basic stand movement/fade code, also see vex code for turning on noclip.*/
    @Override
    public void tick() {
        validateUUID();
        this.noPhysics = true;
        float pitch = this.getXRot();
        float yaw = this.getYRot();
        byte ot = this.getOffsetType();
        if (this.lastOffsetType != ot){
            this.lastOffsetType = ot;
        }
        super.tick();

            if (this.level().isClientSide()){
                setupAnimationStates();
            } else {
                if (!forceVisible) {
                    if (OffsetIndex.OffsetStyle(ot) == OffsetIndex.LOOSE_STYLE) {
                        this.setXRot(pitch);
                        this.setYRot(yaw);
                        this.setYBodyRot(yaw);
                        this.xRotO = pitch;
                        this.yRotO = yaw;
                    }
                }
            }

            if (this.isAlive() && !this.dead || forceVisible){
                if (this.getNeedsUser() && !this.forceVisible) {
                    if (this.getUser() != null && !this.getUser().isRemoved()) {
                        StandUser user = this.getUserData(this.getUser());
                        boolean userActive = user.roundabout$getActive();
                        LivingEntity thisStand = user.roundabout$getStand();
                        if (this.getUser().isAlive() && userActive && (thisStand != null && thisStand.is(this))) {

                            //Make it fade in
                            if (this.getFadeOut() < MaxFade) {
                                if (!this.level().isClientSide()) {
                                    this.incFadeOut((byte) 1);
                                }
                            }

                            if (!this.level().isClientSide() && (((StandUser)this.getUser()).roundabout$getStandDisc().isEmpty() ||
                            ((StandUser)this.getUser()).roundabout$getStandDisc().is(ModItems.STAND_DISC))){
                                this.discard();
                                return;
                            }
                        } else {
                            if (thisStand != null && !thisStand.is(this) && thisStand instanceof StandEntity SE &&
                            SE.getFadeOut() >= 1 && this.getFadeOut() > 1){
                                this.setFadeOut((byte) 1);
                                TickDown();
                            }
                            TickDown();
                        }
                    } else {
                        TickDown();
                    }
                } else {
                    this.setFadeOut(this.getMaxFade());
                }
            } else {
                TickDown();
            }
        //this.noClip = false;
        this.setNoGravity(true);
    } // Happens every tick

    /** Makes the stand fade out every tick, eventually despawning.*/
    private void TickDown(){
        var currFade = this.getFadeOut();
        if (!this.level().isClientSide()) {
            if (currFade >= 0) {
                MainUtil.ejectInFront(this);
                this.incFadeOut((byte) -1);
                if (!this.getHeldItem().isEmpty()) {
                    if (this.canAcquireHeldItem) {
                        double $$3 = this.getEyeY() - 0.3F;
                        ItemEntity $$4 = new ItemEntity(this.level(), this.getX(), $$3, this.getZ(), this.getHeldItem());
                        $$4.setPickUpDelay(40);
                        $$4.setThrower(this.getUUID());
                        this.level().addFreshEntity($$4);
                        this.setHeldItem(ItemStack.EMPTY);
                    }
                }
            }
        }
        if (currFade < 0) {
            if (!this.getPassengers().isEmpty()){
                this.ejectPassengers();
            }
            this.discard();
        }
    }


    @Override
    @javax.annotation.Nullable
    public Entity changeDimension(ServerLevel $$0) {
        if (this.level() instanceof ServerLevel && !this.isRemoved()) {
            if (!this.getHeldItem().isEmpty()) {
                if (this.canAcquireHeldItem) {
                    double $$3 = this.getEyeY() - 0.3F;
                    ItemEntity $$4 = new ItemEntity(this.level(), this.getX(), $$3, this.getZ(), this.getHeldItem().copy());
                    $$4.setPickUpDelay(40);
                    $$4.setThrower(this.getUUID());
                    this.level().addFreshEntity($$4);
                    this.setHeldItem(ItemStack.EMPTY);
                }
            }
        }
        return super.changeDimension($$0);
    }

    @Override
    public void remove(Entity.RemovalReason $$0) {
        MainUtil.ejectInFront(this);
        if (!this.getHeldItem().isEmpty()) {
            if (this.canAcquireHeldItem) {

                    double $$3 = this.getEyeY() - 0.3F;
                    ItemEntity $$4 = new ItemEntity(this.level(), this.getX(), $$3, this.getZ(), this.getHeldItem().copy());
                    $$4.setPickUpDelay(40);
                    $$4.setThrower(this.getUUID());
                    this.level().addFreshEntity($$4);
                    this.setHeldItem(ItemStack.EMPTY);
            }
        }
        super.remove($$0);
    }

    public boolean canBeHitByStands(){
        return (isRemoteControlled() || this.getFollowing() != this.getUser());
    }
    public boolean isRemoteControlled(){
        Entity ent = this.getUser();
        if (ent != null) {
            if (ent instanceof Player PE) {
                StandUser user = ((StandUser) PE);
                StandPowers powers = user.roundabout$getStandPowers();
                if (powers.isPiloting()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double $$0) {
        return false;
    }

    /** Math to determine the position of the stand floating away from its user.
     * Based on Jojovein donut code with great help from Urbancase.*/
    public Vec3 getStandOffsetVector(LivingEntity standUser){
        byte ot = this.getOffsetType();
        if (OffsetIndex.OffsetStyle(ot) == OffsetIndex.FOLLOW_STYLE) {
            return getIdleOffset(standUser);
        } else if (OffsetIndex.OffsetStyle(ot) == OffsetIndex.FIXED_STYLE) {
            return getAttackOffset(standUser,ot);
        }
        return new Vec3(this.getX(),this.getY(),this.getZ());
    }

    /** The offset that can potentially can be used for rushes, punches, blocking, etc.
     * Involves the stand being in an L shape away from the user,
     * with the StandModel.java handling the inward rotation*/
    public Vec3 getAttackOffset(LivingEntity standUser, byte ot) {
        if (ot == OffsetIndex.BENEATH) {
            Vec3 frontVectors = FrontVectors(standUser, 180, 0F);
            return new Vec3(frontVectors.x, frontVectors.y-1.1, frontVectors.z);
        } else {
            float distanceFront;
            float standrotDir2 = 0;
            float standrotDir = (float) getPunchYaw(this.getAnchorPlaceAttack(),
                    1);
            if (standrotDir >0){standrotDir2=90;} else if (standrotDir < 0) {standrotDir2=-90;}
            float addY = 0.3F;
            float addXYZ = 0.3F;
            float addXZ = 0.7F;

            if (ot == OffsetIndex.GUARD || ot == OffsetIndex.GUARD_AND_TRACE) {
                addXZ -= 0.015F;
                distanceFront = 1.05F;
            } else if (ot == OffsetIndex.GUARD_FURTHER_RIGHT) {
                addXZ+= 0.15F;
                distanceFront = 1.05F;
            } else {
                distanceFront = ((StandUser) standUser).roundabout$getStandPowers().getDistanceOutAccurate(standUser,((StandUser) standUser).roundabout$getStandReach(),true);
            }

            Vec3 frontVectors = FrontVectors(standUser, 0, distanceFront);

            Vec3 vec3d2 = DamageHandler.getRotationVector(0, standUser.getYHeadRot()+ standrotDir2);
            frontVectors = frontVectors.add(vec3d2.x * addXZ, 0, vec3d2.z * addXZ);
            return new Vec3(frontVectors.x,frontVectors.y + standUser.getEyeHeight(standUser.getPose()) + addY - 1.6,
                    frontVectors.z);
        }
    }
    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
            $$0.putBoolean("roundabout.AcquireHeldItem",this.canAcquireHeldItem);
            CompoundTag compoundtag = new CompoundTag();
            $$0.put("roundabout.HeldItem",this.getHeldItem().save(compoundtag));
        super.addAdditionalSaveData($$0);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        this.canAcquireHeldItem = $$0.getBoolean("roundabout.AcquireHeldItem");
        CompoundTag compoundtag = $$0.getCompound("roundabout.HeldItem");
        ItemStack itemstack = ItemStack.of(compoundtag);
        this.setHeldItem(itemstack);
        super.readAdditionalSaveData($$0);
    }

    public boolean isTechnicallyInWall() {
        float $$0 = this.getDimensions(this.getPose()).width * 0.8F;
        AABB $$1 = AABB.ofSize(this.getEyePosition(), (double)$$0, 1.0E-6, (double)$$0);
        return BlockPos.betweenClosedStream($$1)
                .anyMatch(
                        $$1x -> {
                            BlockState $$2 = this.level().getBlockState($$1x);
                            return !$$2.isAir()
                                    && $$2.isSuffocating(this.level(), $$1x)
                                    && Shapes.joinIsNotEmpty(
                                    $$2.getCollisionShape(this.level(), $$1x).move((double)$$1x.getX(), (double)$$1x.getY(), (double)$$1x.getZ()),
                                    Shapes.create($$1),
                                    BooleanOp.AND
                            );
                        }
                );
    }
    public boolean isTechnicallyInImpassableWall() {
        float $$0 = this.getDimensions(this.getPose()).width * 0.8F;
        AABB $$1 = AABB.ofSize(this.getEyePosition(), (double)$$0, 1.0E-6, (double)$$0);
        return BlockPos.betweenClosedStream($$1)
                .anyMatch(
                        $$1x -> {
                            BlockState $$2 = this.level().getBlockState($$1x);
                            return !$$2.isAir() && !($$2.getBlock() instanceof IronBarsBlock) &&
                            !($$2.getBlock() instanceof FenceBlock) &&
                            !($$2.getBlock() instanceof FenceGateBlock) &&
                                    !($$2.getBlock() instanceof SlabBlock) &&
                                    !($$2.getBlock() instanceof AnvilBlock) &&
                                    !($$2.getBlock() instanceof BellBlock) &&
                                    !($$2.getBlock() instanceof RodBlock)
                                    && $$2.isSuffocating(this.level(), $$1x)
                                    && Shapes.joinIsNotEmpty(
                                    $$2.getCollisionShape(this.level(), $$1x).move((double)$$1x.getX(), (double)$$1x.getY(), (double)$$1x.getZ()),
                                    Shapes.create($$1),
                                    BooleanOp.AND
                            );
                        }
                );
    }


    public boolean ignoreTridentSpin(){
        return true;
    }

    /** Uses rotation to grab a point in front of an entity, with DR being an optional pitch offset*/
    public Vec3 FrontVectors(Entity standUser, double dr, float distance) {

        Vec3 vec3d = new Vec3(standUser.getX(),standUser.getY(),standUser.getZ());
        Vec3 vec3d2 = DamageHandler.getRotationVector(standUser.getXRot(), (float) (standUser.getYRot()+dr));
        return vec3d.add(vec3d2.x * distance, vec3d2.y * distance, vec3d2.z * distance);
    }

    /**Ensures that no matter where the stand is passively configured to rest, an appropriate yaw
     * position is returned. Useful for stand rotation in attack animations*/
    public double getPunchYaw(double Yaw, double multi){
        if (Yaw < 90){return Yaw*multi;}
        else if (Yaw <= 180){return (180-Yaw)*multi;}
        else if (Yaw <= 270){return -((Yaw-180)*multi);}
        else{return -((360-Yaw)*multi);}
    }

    /**This is the way a stand looks when it is passively floating by you*/
    public Vec3 getIdleOffset(LivingEntity standUser) {
        int vis = this.getFadeOut();
        double r = (((double) vis / MaxFade) * ((standUser.getBbWidth()/2)+this.getDistanceOut()));
        if (r < 0.5) {
            r = 0.5;
        }
        double yawfix = standUser.getYRot();
        yawfix += this.getAnchorPlace();
        if (yawfix > 360) {
            yawfix -= 360;
        } else if (yawfix < 0) {
            yawfix += 360;
        }
        double ang = (yawfix - 180) * Math.PI;

        double mcap = 0.3;
        Vec3 xyz = standUser.getDeltaMovement();
        double yy = xyz.y() * 0.3;
        if (yy > mcap) {
            yy = mcap;
        } else if (yy < -mcap) {
            yy = -mcap;
        }
        if (isSwimming() || isVisuallyCrawling() || isFallFlying()) {
            yy += 1;
        }

        double x1 = standUser.getX() - -1 * (r * (Math.sin(ang / 180)));
        double y1 = standUser.getY() + getIdleYOffset() - yy;
        double z1 = standUser.getZ() - (r * (Math.cos(ang / 180)));

        return new Vec3(x1, y1, z1);
    }

    /** Builds Minecraft entity attributes like speed and health.
     * Admittedly, I think these numbers are arbitrary given how stands work.
     * The most notable thing about a stand is its hitbox size but that's factored in */
    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.2F).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    public boolean startRiding(Entity $$0) {
        if (!($$0 instanceof Boat) && !($$0 instanceof Minecart)){
            return this.startRiding($$0, false);
        }
        return false;
    }

    public float getLookYaw(double maxDistance){
        Vec3 pointVec = DamageHandler.getRayPoint(this.getUser(), maxDistance);
        if (pointVec != null) {
            double d = pointVec.x - this.getX();
            double e = pointVec.y - this.getY();
            double f = pointVec.z - this.getZ();
            double g = Math.sqrt(d * d + f * f);
            return (Mth.wrapDegrees((float) ((Mth.atan2(e, g) * 57.2957763671875))));
        } else {
            return 0;
        }
    }

}
