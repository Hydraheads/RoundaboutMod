package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.access.NoVibrationEntity;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
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

    /**Disable this for a stand with its own movement or positioning unless you want it to freeze in the air*/
    public boolean lockPos(){
        return true;
    }
    public float fadePercent =0F;
    protected static final EntityDataAccessor<Integer> FADE_PERCENT = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Byte> FADE_OUT = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.BYTE);



    /**USER_ID is the mob id of the stand's user. Needs to be stored as an int,
     * because clients do not have access to UUIDS.*/
    protected static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(StandEntity.class,
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
    public LivingEntity User;

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

    public Vec3 getBonusOffset() {
        return Vec3.ZERO;
    }

    public void playerSetProperties(Player PE) {
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
        this.entityData.define(FADE_OUT, (byte) 0);
        this.entityData.define(FADE_PERCENT, 100);
        this.entityData.define(USER_ID, -1);
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

    @Override
    public boolean canBreatheUnderwater() {
        return true;
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

    public boolean hasNoPhysics(){
        return !isRemoteControlled();
    }

    @Override
    public void push(Entity $$0) {
    }

    @Override
    public void doPush(Entity $$0) {
    }

    /** This happens every tick. Basic stand movement/fade code, also see vex code for turning on noclip.*/
    @Override
    public void tick() {
        validateUUID();
        this.noPhysics = hasNoPhysics();
        super.tick();

            if (this.level().isClientSide()){
                setupAnimationStates();
            }

            if ((this.isAlive() && !this.dead || forceVisible) && !forceDespawnSet){
                if (this.getNeedsUser() && !this.forceVisible) {
                    LivingEntity userEntity = this.getUser();
                    if (userEntity != null && !userEntity.isRemoved()) {
                        StandUser user = this.getUserData(userEntity);
                        boolean userActive = user.roundabout$getActive();
                        LivingEntity thisStand = user.roundabout$getStand();
                        if (isValid(userActive,thisStand, userEntity)) {

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
                            handleTickDownIfDupe(thisStand);
                        }
                    } else {
                        TickDown();
                    }
                } else {
                    if (!this.getNeedsUser()){
                        if (this.getFadeOut() < MaxFade) {
                            if (!this.level().isClientSide()) {
                                this.incFadeOut((byte) 1);
                            }
                        }
                    } else {
                        this.setFadeOut(this.getMaxFade());
                    }
                }
            } else {
                TickDown();
            }
        //this.noClip = false;
        this.setNoGravity(!standHasGravity());
    } // Happens every tick


    public boolean standHasGravity(){
        return false;
    }


    public boolean validatePowers(LivingEntity user){
        return (((StandUser)user).roundabout$getStandPowers() instanceof StandPowers);
    }

    public boolean needsActive(){
        return true;
    }
    public boolean isValid(boolean userActive, LivingEntity thisStand, LivingEntity userEntity){
        return userEntity.isAlive() && !userEntity.isRemoved() && (!needsActive() || userActive) && (thisStand != null && thisStand.is(this));
    }
    public void handleTickDownIfDupe(LivingEntity thisStand){

        if (thisStand != null && !thisStand.is(this) && thisStand instanceof StandEntity SE &&
                SE.getFadeOut() >= 1 && this.getFadeOut() > 1){
            this.setFadeOut((byte) 1);
            TickDown();
        }
        TickDown();
    }

    public boolean forceDespawnSet = false;
    public void forceDespawn(boolean despawn){
        forceDespawnSet = despawn;
    }

    /** Makes the stand fade out every tick, eventually despawning.*/
    public void TickDown(){
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


    /**Stands like harvest and survivor do not qualify as a "singular entity", so they will not despawn off
     * of conditions like pressing the summoning key again */
    public boolean isASingularEntity(){
        return true;
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
        return true;
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

    @Override
    public boolean causeFallDamage(float $$0, float $$1, DamageSource $$2) {
        return false;
    }
    @Override
    protected void playBlockFallSound() {
    }
    @Override
    protected void checkFallDamage(double $$0, boolean $$1, BlockState $$2, BlockPos $$3) {
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

    public boolean forceVisualRotation(){
        return false;
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
