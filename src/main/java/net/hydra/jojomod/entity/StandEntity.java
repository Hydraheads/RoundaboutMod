package net.hydra.jojomod.entity;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.networking.component.StandComponent;
import net.hydra.jojomod.networking.component.StandUserComponent;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.example.entity.DynamicExampleEntity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;
import java.util.UUID;

public abstract class StandEntity extends MobEntity implements GeoEntity {
    private final int MaxFade = 8;

    protected static final TrackedData<Integer> ANCHOR_PLACE = DataTracker.registerData(StandEntity.class,
            TrackedDataHandlerRegistry.INTEGER);

    protected static final TrackedData<Integer> MOVE_FORWARD = DataTracker.registerData(StandEntity.class,
            TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Integer> FADE_OUT = DataTracker.registerData(StandEntity.class,
            TrackedDataHandlerRegistry.INTEGER);


    public float bodyRotationX;
    public float bodyRotationY;
    public float headRotationX;
    public float headRotationY;
    public float standRotationX;
    public float standRotationY;

    private boolean isDisplay;

    protected static final TrackedData<Integer> offsetType = DataTracker.registerData(StandEntity.class,
            TrackedDataHandlerRegistry.INTEGER);


    protected SoundEvent getSummonSound() {
        return ModSounds.SUMMON_SOUND_EVENT;
    }

    public void playSummonSound() {
        this.getWorld().playSound(null, this.getBlockPos(), getSummonSound(), SoundCategory.PLAYERS, 1F, 1F);
    } //Plays the Summon sound. Happens when stand is summoned with summon key.

    public final int getMoveForward() {
        return this.dataTracker.get(MOVE_FORWARD);
    } //returns leaning direction

    public final int getOffsetType() {
        return this.dataTracker.get(offsetType);
    } //returns leaning direction

    public final void setOffsetType(Integer oft) {
        this.dataTracker.set(offsetType, oft);
    }

    /**
     * Presently, this is how the stand knows to lean in any direction based on player movement.
     * Creates the illusion of floaty movement within the stand.
     * Relevant in stand model code:
     *
     * @see StandModel#setCustomAnimations
     */
    public final void setMoveForward(Integer MF) {
        this.dataTracker.set(MOVE_FORWARD, MF);
    } //sets leaning direction

    public int getMaxFade() {
        return MaxFade;
    }

    public final void setFadeOut(Integer FadeOut) {
        this.dataTracker.set(FADE_OUT, FadeOut);
    } //sets leaning direction
    public int getFadeOut() {
        return this.dataTracker.get(FADE_OUT);
    }

    public final int getAnchorPlace() {
        return this.dataTracker.get(ANCHOR_PLACE);
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
        this.dataTracker.set(ANCHOR_PLACE, degrees);
    }


    /**
     * These functions tell the game if the stand's user is Swimming, Crawling, or Elytra Flying.
     * Currently used for idle animation variants.
     *
     * @see StandModel#setCustomAnimations
     */
    @Override
    public boolean isSwimming() {
        if (this.getSelfData().getUser() != null) {
            return this.getSelfData().getUser().isSwimming();
        } else {
            return false;
        }
    }

    @Override
    public boolean isCrawling() {
        if (this.getSelfData().getUser() != null) {
            return this.getSelfData().getUser().isCrawling();
        } else {
            return false;
        }
    }

    @Override
    public boolean isFallFlying() {
        if (this.getSelfData().getUser() != null) {
            return (this.getSelfData().getUser()).isFallFlying();
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
     * @see StandEntityRenderer#getStandOpacity
     * <p>
     * When a stand hits negative opacity, it automatically despawns
     * @see #TickDown
     */
    public void incFadeOut(int inc) {
        this.setFadeOut(this.getFadeOut()+inc);
    }

    /**
     * These are not components as they are simpler variables to store/load than entities.
     * It would be nice if move forward were to be less packet intensive.
     */
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ANCHOR_PLACE, 55);
        this.dataTracker.startTracking(FADE_OUT, 0);
        this.dataTracker.startTracking(MOVE_FORWARD, 0);
        this.dataTracker.startTracking(offsetType, 0);
    }

    private final AnimatableInstanceCache cache =
            GeckoLibUtil.createInstanceCache(this);


    /**
     * Initialize Stands
     */
    protected StandEntity(EntityType<? extends MobEntity> entityType, World world) {
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
    public boolean hasVehicle() {
        int ot = this.getOffsetType();
            return this.getVehicle() != null;
    }

    /**This override prevents an infinite loop when an entity is riding itself*/
    @Override
    public Entity getRootVehicle() {
        Entity entity = this;
        while (entity.hasVehicle() && Objects.requireNonNull(entity.getVehicle()).getUuid() != entity.getUuid()) {
            entity = entity.getVehicle();
        }
        return entity;
    }

    /**Chooses which offset animation types override stand direction rendering*/
    @Override
    public LivingEntity getVehicle() {
       int ot = this.getOffsetType();
       if (ot == 1) {
           return this;
       } else {
           LivingEntity follower = this.getFollowing();
           if (follower != null && !follower.isRemoved()) {
               StandUserComponent follower2 = MyComponents.STAND_USER.get(follower);
               //this will be changed to getfollower
               if (follower2.getStand() != null) {
                   if (follower2.getStand() != this) {
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
    }


    public boolean hasMaster() {
        return this.getMaster() != null;
    } //returns IF stand has a master


    public StandUserComponent getUserData(LivingEntity User) {
        return MyComponents.STAND_USER.get(User);
    }


    /**
     * Returns the Component of StandData on the entity,
     * Use this to get the User, who the stand is following, etc
     *
     * @see net.hydra.jojomod.networking.component.StandData
     */
    public StandComponent getSelfData() {
        return MyComponents.STAND.get(this);
    }

    /**
     * Sets stand User, the mob who "owns" the stand
     */
    public void setMaster(LivingEntity Master) {
        this.getSelfData().setUser(Master);
    }

    /**
     * Unused, will be used to lock a stand onto another mob.
     * Use case example: Killer Queen BTD following someone else
     * Code to tick on follower will be needed in client world mixin
     */
    public void setFollowing(LivingEntity Following) {
        this.getSelfData().setFollowing(Following);
    }

    public LivingEntity getFollowing() {
        return this.getSelfData().getFollowing();
    }

    /**
     * Sets stand User, the mob who "owns" the stand
     */
    public LivingEntity getMaster() {
        return this.getSelfData().getUser();
    }

    /**
     * When this is called, sets the User's owned stand to this one. Both the Stand and the User store
     * each other, and this is for setting the User's storage.
     *
     * @see net.hydra.jojomod.networking.component.StandUserData#setStand
     */
    public boolean startStandRiding(LivingEntity entity, boolean force) {
        StandUserComponent UD = getUserData(entity);
        UD.setStand(this);
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
        this.setVelocity(Vec3d.ZERO);
        this.tick();
        if (this.getFollowing() == null) {
            //RoundaboutMod.LOGGER.info("MF No Master");
            return;
        }
        StandUserComponent UD = getUserData(this.getFollowing());
        //RoundaboutMod.LOGGER.info("MF Update Pos");
        UD.updateStandOutPosition(this);
    }

    /** Stand does not take damage under normal circumstances.*/
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    /** This happens every tick. Basic stand movement/fade code, also see vex code for turning on noclip.*/
    @Override
    public void tick() {
        this.noClip = true;

        super.tick();
            if (this.isAlive() && !this.dead){
                if (this.getNeedsUser() && !this.isDisplay) {
                    if (this.getSelfData().getUser() != null) {
                        boolean userActive = this.getUserData(this.getMaster()).getActive();
                        LivingEntity thisStand = this.getUserData(this.getMaster()).getStand();
                        if (this.getSelfData().getUser().isAlive() && userActive && (thisStand != null && thisStand.getId() == this.getId())) {

                            //Make it fade in
                            if (this.getFadeOut() < MaxFade) {
                                if (!this.getWorld().isClient()) {
                                    this.incFadeOut(1);
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
        if (!this.getWorld().isClient()) {
            if (currFade > 0) {
                this.incFadeOut(-1);
            }
        }
        if (currFade <= 0) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    /** Math to determine the position of the stand floating away from its user.
     * Based on Jojovein donut code with great help from Urbancase.*/
    public Vec3d getStandOffsetVector(Entity standUser){
        int ot = this.getOffsetType();
        if (ot == 0) {
            return getIdleOffset(standUser);
        } else if (ot == 1) {
            return getAttackOffset(standUser,true);
        }
        return new Vec3d(this.getX(),this.getY(),this.getZ());
    }

    /** The offset that can potentially can be used for rushes, punches, blocking, etc.
     * Involves the stand being in an L shape away from the user,
     * with the StandModel.java handling the inward rotation*/
    public Vec3d getAttackOffset(Entity standUser, boolean capped) {
        StandUserComponent UD = getUserData((LivingEntity) standUser);
        float distanceFront = UD.getDistanceOut(standUser,UD.getStandReach(),true);

        Vec3d frontVectors = FrontVectors(standUser, 0, distanceFront);

        float standrotDir = (float) getPunchYaw(this.getAnchorPlace(),
                        1);
        float standrotDir2 = 0;
        if (standrotDir >0){standrotDir2=90;} else if (standrotDir < 0) {standrotDir2=-90;}
        Vec3d vec3d2 = DamageHandler.getRotationVector(0, standUser.getHeadYaw()+ standrotDir2);
        frontVectors = frontVectors.add(vec3d2.x * 0.7F, 0, vec3d2.z * 0.7F);
        return new Vec3d(frontVectors.x,frontVectors.y + +0.3,
                frontVectors.z);
    }

    /** Uses rotation to grab a point in front of an entity, with DR being an optional pitch offset*/
    public Vec3d FrontVectors(Entity standUser, double dr, float distance) {

        Vec3d vec3d = new Vec3d(standUser.getX(),standUser.getY(),standUser.getZ());
        Vec3d vec3d2 = DamageHandler.getRotationVector(standUser.getPitch(), (float) (standUser.getYaw()+dr));
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
    public Vec3d getIdleOffset(Entity standUser) {
        int vis = this.getFadeOut();
        double r = (((double) vis / MaxFade) * 1.37);
        if (r < 0.5) {
            r = 0.5;
        }
        double yawfix = standUser.getYaw();
        yawfix += this.getAnchorPlace();
        if (yawfix > 360) {
            yawfix -= 360;
        } else if (yawfix < 0) {
            yawfix += 360;
        }
        double ang = (yawfix - 180) * Math.PI;

        double mcap = 0.3;
        Vec3d xyz = standUser.getVelocity();
        double yy = xyz.getY() * 0.3;
        if (yy > mcap) {
            yy = mcap;
        } else if (yy < -mcap) {
            yy = -mcap;
        }
        if (isSwimming() || isCrawling() || isFallFlying()) {
            yy += 1;
        }

        double x1 = standUser.getX() - -1 * (r * (Math.sin(ang / 180)));
        double y1 = standUser.getY() + 0.1 - yy;
        double z1 = standUser.getZ() - (r * (Math.cos(ang / 180)));

        return new Vec3d(x1, y1, z1);
    }

    /** Builds Minecraft entity attributes like speed and health.
     * Admittedly, I think these numbers are arbitrary given how stands work.
     * The most notable thing about a stand is its hitbox size but that's factored in
     * @see ModEntities for now. */
    public static DefaultAttributeContainer.Builder createStandAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                0.2F).add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
    }

    public float getLookYaw(double maxDistance){
        Vec3d pointVec = DamageHandler.getRayPoint(this.getMaster(), maxDistance);
        if (pointVec != null) {
            double d = pointVec.x - this.getX();
            double e = pointVec.y - this.getY();
            double f = pointVec.z - this.getZ();
            double g = Math.sqrt(d * d + f * f);
            return (MathHelper.wrapDegrees((float) ((MathHelper.atan2(e, g) * 57.2957763671875))));
        } else {
            return 0;
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                DefaultAnimations.genericIdleController(this)
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    // Create the animation handler for the body segment
    protected PlayState poseBody(AnimationState<DynamicExampleEntity> state) {
        state.setAnimation(DefaultAnimations.IDLE);
        return PlayState.CONTINUE;
    }
}
