package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;

public abstract class StandEntity extends Mob{
    private final int MaxFade = 8;

    protected static final EntityDataAccessor<Integer> ANCHOR_PLACE = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.INT);

    protected static final EntityDataAccessor<Byte> MOVE_FORWARD = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Byte> FADE_OUT = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.BYTE);

    protected static final EntityDataAccessor<Byte> OFFSET_TYPE = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> FOLLOWING_ID = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Byte> ANIMATION = SynchedEntityData.defineId(StandEntity.class,
            EntityDataSerializers.BYTE);
    public float bodyRotationX =0;
    public float bodyRotationY =0;
    public float headRotationX =0;
    public float headRotationY =0;
    public float standRotationX =0;
    public float standRotationY =0;

    private boolean isDisplay;
    @Nullable
    private LivingEntity User;
    @Nullable
    private LivingEntity Following;

    private int idleAnimationTimeout = 0;

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

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState punchState1 = new AnimationState();
    public final AnimationState punchState2 = new AnimationState();
    public final AnimationState punchState3 = new AnimationState();
    public final AnimationState blockAnimationState = new AnimationState();
    public final AnimationState barrageChargeAnimationState = new AnimationState();
    public final AnimationState barrageAnimationState = new AnimationState();
    public final AnimationState barrageEndAnimationState = new AnimationState();
    public final AnimationState barrageHurtAnimationState = new AnimationState();

    private void setupAnimationStates() {
        if (this.getUser() != null) {
            if (this.getAnimation() == 0) {
                this.idleAnimationState.startIfStopped(this.tickCount);
            } else {
                this.idleAnimationState.stop();
            }
            if (this.getAnimation() == 1) {
                this.punchState1.startIfStopped(this.tickCount);
            } else {
                this.punchState1.stop();
            }
            if (this.getAnimation() == 2) {
                this.punchState2.startIfStopped(this.tickCount);
            } else {
                this.punchState2.stop();
            }
            if (this.getAnimation() == 3) {
                this.punchState3.startIfStopped(this.tickCount);
            } else {
                this.punchState3.stop();
            }

            if (this.getAnimation() == 10) {
                this.blockAnimationState.startIfStopped(this.tickCount);
            } else {
                this.blockAnimationState.stop();
            }
            if (this.getAnimation() == 11) {
              this.barrageChargeAnimationState.startIfStopped(this.tickCount);
            } else {
                this.barrageChargeAnimationState.stop();
            }
            if (this.getAnimation() == 12) {
                this.barrageAnimationState.startIfStopped(this.tickCount);
            } else {
                this.barrageAnimationState.stop();
            }

            if (this.getAnimation() == 13) {
                this.barrageEndAnimationState.startIfStopped(this.tickCount);
            } else {
                this.barrageEndAnimationState.stop();
            }

            if (this.getAnimation() == 14) {
                this.barrageHurtAnimationState.startIfStopped(this.tickCount);
            } else {
                this.barrageHurtAnimationState.stop();
            }
        }
    }

    protected SoundEvent getSummonSound() {
        return ModSounds.SUMMON_SOUND_EVENT;
    }

    public void playSummonSound() {
        this.level().playSound(null, this.blockPosition(), getSummonSound(), SoundSource.PLAYERS, 1F, 1F);
    } //Plays the Summon sound. Happens when stand is summoned with summon key.

    public final byte getMoveForward() {
        return this.entityData.get(MOVE_FORWARD);
    } //returns leaning direction

    public final byte getOffsetType() {
        return this.entityData.get(OFFSET_TYPE);
    } //returns leaning direction
    public final void setOffsetType(byte oft) {
        this.entityData.set(OFFSET_TYPE, oft);
    }

    public final void setAnimation(byte animation) {
        this.entityData.set(ANIMATION, animation);
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

    public final int getAnchorPlace() {
        return this.entityData.get(ANCHOR_PLACE);
    }

    public final byte getAnimation() {
        return this.entityData.get(ANIMATION);
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

    public void setStandRotationX(float bodRot) {
        this.standRotationX = bodRot;
    }
    public void setStandRotationY(float bodRot) {
        this.standRotationY = bodRot;
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
     * @see StandRenderer#getStandOpacity
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
        this.entityData.define(FADE_OUT, (byte) 0);
        this.entityData.define(MOVE_FORWARD, (byte) 0);
        this.entityData.define(OFFSET_TYPE, (byte) 0);
        this.entityData.define(USER_ID, -1);
        this.entityData.define(FOLLOWING_ID, -1);
        this.entityData.define(ANIMATION, (byte) 0);
    }


    /**
     * Initialize Stands
     */
    protected StandEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
        //FadeOut = 10;
    }


    /**
     * Tricks Minecraft's rendering to make stands look like they are attached to mobs.
     * In vanilla, this is how mounts are handled in general, but we have a custom mount system.
     *
     * @see #startStandRiding
     */
    @Override
    public boolean isPassenger() {
        byte ot = this.getOffsetType();
        if (OffsetIndex.OffsetStyle(ot) != OffsetIndex.LOOSE_STYLE) {
            return this.getVehicle() != null;
        } else {
            return false;
        }
    }

    /**This override prevents an infinite loop when an entity is riding itself*/
    @Override
    public Entity getRootVehicle() {
        byte ot = this.getOffsetType();
        if (OffsetIndex.OffsetStyle(ot) != OffsetIndex.LOOSE_STYLE) {
            Entity entity = this;
            while (entity.isPassenger() && Objects.requireNonNull(entity.getVehicle()).getUUID() != entity.getUUID()) {
                entity = entity.getVehicle();
            }
            return entity;
        } else {
            return null;
        }
    }

    /**Chooses which offset animation types override stand direction rendering*/
    @Override
    public LivingEntity getVehicle() {
       byte ot = this.getOffsetType();
       if (OffsetIndex.OffsetStyle(ot) != OffsetIndex.LOOSE_STYLE) {
           if (OffsetIndex.OffsetStyle(ot) != OffsetIndex.FOLLOW_STYLE) {
               return this;
           } else {
               LivingEntity follower = this.getFollowing();
               if (follower != null && !follower.isRemoved()) {
                   //this will be changed to getfollower
                   if (((StandUser) follower).getStand() != null) {
                       if (((StandUser) follower).getStand() != this) {
                           follower = null;
                       }
                   } else {
                       follower = null;
                   }
               } else {
                   follower = null;
               }
               return follower;
           }
       } else {
           return null;
       }
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

    /**
     * When this is called, sets the User's owned stand to this one. Both the Stand and the User store
     * each other, and this is for setting the User's storage.
     *
     * @see net.hydra.jojomod.event.powers.StandUser#setStand
     */
    public boolean startStandRiding(LivingEntity entity, boolean force) {
        ((StandUser) entity).setStand(this);
        return true;
        //RoundaboutMod.LOGGER.info("MF");
    }

    /**
     * Called every tick in
     *
     * @see net.hydra.jojomod.mixin.ClientWorldMixin for the client and
     * @see net.hydra.jojomod.mixin.ServerWorldMixin for the server.
     * Basically, this lets the user/followee tick the stand so that it moves exactly with them.
     * The main purpose for this is to make the smooth visual effect of being ridden.
     * Also, if a stand is perfectly still, it's possible it is just not ticking due to not being mounted properly
     * with a follower.
     */
    public void tickStandOut() {
        byte ot = this.getOffsetType();
        if (OffsetIndex.OffsetStyle(ot) != OffsetIndex.LOOSE_STYLE) {
            this.setDeltaMovement(Vec3.ZERO);
            this.tick();
            if (this.getFollowing() == null) {
                return;
            }
            ((StandUser) this.getFollowing()).updateStandOutPosition(this);
        }
    }


    /** Stand does not take damage under normal circumstances.*/
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    /** This happens every tick. Basic stand movement/fade code, also see vex code for turning on noclip.*/
    @Override
    public void tick() {
        this.noPhysics = true;
        float pitch = this.getXRot();
        float yaw = this.getYRot();
        byte ot = this.getOffsetType();

        super.tick();

            if (this.level().isClientSide()){
                if (OffsetIndex.OffsetStyle(ot) == OffsetIndex.LOOSE_STYLE) {
                    this.setYBodyRot(yaw);
                    this.setYHeadRot(yaw);
                    this.yBodyRotO = yaw;
                    this.yHeadRotO = yaw;
                    this.absMoveTo(this.getX(),this.getY(),this.getZ(),yaw,pitch);
                }
                setupAnimationStates();
            } else {
                if (OffsetIndex.OffsetStyle(ot) == OffsetIndex.LOOSE_STYLE) {
                    this.setXRot(pitch);
                    this.setYRot(yaw);
                }
            }

            if (this.isAlive() && !this.dead){
                if (this.getNeedsUser() && !this.isDisplay) {
                    if (this.getUser() != null) {
                        boolean userActive = this.getUserData(this.getUser()).getActive();
                        LivingEntity thisStand = this.getUserData(this.getUser()).getStand();
                        if (this.getUser().isAlive() && userActive && (thisStand != null && thisStand.getId() == this.getId())) {

                            //Make it fade in
                            if (this.getFadeOut() < MaxFade) {
                                if (!this.level().isClientSide()) {
                                    this.incFadeOut((byte) 1);
                                }
                            }
                        } else {
                            TickDown();
                        }
                    } else {
                        TickDown();
                    }
                } else {
                    this.setFadeOut(this.getMaxFade());
                }
            }
        //this.noClip = false;
        this.setNoGravity(true);
    } // Happens every tick

    /** Makes the stand fade out every tick, eventually despawning.*/
    private void TickDown(){
        var currFade = this.getFadeOut();
        if (!this.level().isClientSide()) {
            if (currFade >= 0) {
                this.incFadeOut((byte) -1);
            }
        }
        if (currFade < 0) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    /** Math to determine the position of the stand floating away from its user.
     * Based on Jojovein donut code with great help from Urbancase.*/
    public Vec3 getStandOffsetVector(LivingEntity standUser){
        byte ot = this.getOffsetType();
        if (ot == OffsetIndex.FOLLOW) {
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
        float distanceFront;
        float standrotDir2 = 0;
        float standrotDir = (float) getPunchYaw(this.getAnchorPlace(),
                1);
        if (standrotDir >0){standrotDir2=90;} else if (standrotDir < 0) {standrotDir2=-90;}
        float addY = 0.3F;
        float addXYZ = 0.3F;
        float addXZ = 0.7F;

        if (ot == OffsetIndex.GUARD) {
            addXZ-= 0.015F;
            distanceFront = 1.05F;
        } else {
            distanceFront = ((StandUser) standUser).getDistanceOut(standUser,((StandUser) standUser).getStandReach(),true);
        }

        Vec3 frontVectors = FrontVectors(standUser, 0, distanceFront);

        Vec3 vec3d2 = DamageHandler.getRotationVector(0, standUser.getYHeadRot()+ standrotDir2);
        frontVectors = frontVectors.add(vec3d2.x * addXZ, 0, vec3d2.z * addXZ);
        return new Vec3(frontVectors.x,frontVectors.y + standUser.getEyeHeight(standUser.getPose()) + addY - 1.6,
                frontVectors.z);
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
        double r = (((double) vis / MaxFade) * 1.37);
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
        double y1 = standUser.getY() + 0.1 - yy;
        double z1 = standUser.getZ() - (r * (Math.cos(ang / 180)));

        return new Vec3(x1, y1, z1);
    }

    /** Builds Minecraft entity attributes like speed and health.
     * Admittedly, I think these numbers are arbitrary given how stands work.
     * The most notable thing about a stand is its hitbox size but that's factored in
     * @see ModEntities for now. */
    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.2F).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 2.0);
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