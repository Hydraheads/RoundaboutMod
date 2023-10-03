package net.hydra.jojomod.entity;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.networking.component.StandComponent;
import net.hydra.jojomod.networking.component.StandUserComponent;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;
import java.util.UUID;

public abstract class StandEntity extends MobEntity implements GeoEntity {
    private int FadeOut; //The stand's current transparency
    private final int MaxFade = 8;  //The stand's maximum transparency

    protected static final TrackedData<Integer> ANCHOR_PLACE = DataTracker.registerData(StandEntity.class,
            TrackedDataHandlerRegistry.INTEGER); //The stand's position relative to the player. Between 0 and 360.

    protected static final TrackedData<Integer> MOVE_FORWARD = DataTracker.registerData(StandEntity.class,
            TrackedDataHandlerRegistry.INTEGER);
            //If the stand's user is moving forward or backwards, this value gets changed to make it lean

    public float bodyRotation;

    private boolean isDisplay;

    protected SoundEvent getSummonSound() {
            return ModSounds.SUMMON_SOUND_EVENT;
    }
    //Every stand has a Summon sound, so they should overwrite this function, or they will play the generic one.

    public void playSummonSound() {
        this.getWorld().playSound(null, this.getBlockPos(), getSummonSound(), SoundCategory.PLAYERS, 1F, 1F);
    } //Plays the Summon sound. Happens when stand is summoned with summon key.

    public final int getMoveForward() {
        return this.dataTracker.get(MOVE_FORWARD);
    } //returns leaning direction

    public final void setMoveForward(Integer MF) {
        this.dataTracker.set(MOVE_FORWARD, MF);
    } //sets leaning direction

    public int getMaxFade() {return MaxFade;}
    public int getFadeOut() {
        return this.FadeOut;
    }
    public final int getAnchorPlace() {
        return this.dataTracker.get(ANCHOR_PLACE);
    }


    public final boolean getNeedsUser(){
        return true;
    }

    public float getBodyRotation(){
        return this.bodyRotation;
    } public void setBodyRotation(float bodRot){
       this.bodyRotation = bodRot;
    }
    public final void setAnchorPlace(Integer degrees) {
        this.dataTracker.set(ANCHOR_PLACE, degrees);
    }


    @Override
    public boolean isSwimming() {
        if (this.getSelfData().getUser() != null){
            return this.getSelfData().getUser().isSwimming();
        } else {
            return false;
        }
    } //The stand is swimming only if its user is

    @Override
    public boolean isCrawling() {
        if (this.getSelfData().getUser() != null){
            return this.getSelfData().getUser().isCrawling();
        } else {
            return false;
        }
    } //The stand is crawling only if its user is

    @Override
    public boolean isFallFlying() {
        if (this.getSelfData().getUser() != null){
            return ((LivingEntity) this.getSelfData().getUser()).isFallFlying();
        } else {
            return false;
        }
    } //The stand is elytra flying only if its user is

    public boolean getDisplay() {
        return this.isDisplay;
    }
    public void setDisplay(boolean display) {
       this.isDisplay = display;
    }


    public void incFadeOut(int inc) {
        this.FadeOut+=inc;
    } //Positive values make the stand less see-through. Negative ones make it fade and eventually despawn.

    public void setFadeOut(int inc) {
        this.FadeOut=inc;
    } //Immediately makes the stand visible/invisible

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ANCHOR_PLACE, 55);
        this.dataTracker.startTracking(MOVE_FORWARD, 0);
    }

    private final AnimatableInstanceCache cache =
            GeckoLibUtil.createInstanceCache(this);


    protected StandEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
        //FadeOut = 10;
    } // Initialize Stand


    @Override
    public boolean hasVehicle() {
        return this.getFollowing() != null;
    } //Stand is always riding... in a sense

    @Override
    public LivingEntity getVehicle() {
        return this.getFollowing();
    } //returns master when vanilla calls for rider


    public boolean hasMaster() {
        return this.getMaster() != null;
    } //returns IF stand has a master

    public StandUserComponent getUserData (LivingEntity User){
        return MyComponents.STAND_USER.get(User);
    }
    public StandComponent getSelfData (){
        return MyComponents.STAND.get(this);
    }
    public void dismountMaster() {
        if (getSelfData().getUser() != null) {
        }
    } //takes stand's data off of its user

    public void setMaster(LivingEntity Master) {
        this.getSelfData().setUser(Master);
    } //Sets stand user

    public LivingEntity getFollowing() {
        return this.getSelfData().getFollowing();
    }

    public LivingEntity getMaster() {
        return this.getSelfData().getUser();
    } //Returns stand user

    public boolean startStandRiding(LivingEntity entity, boolean force) {
        StandUserComponent UD = getUserData(entity);
        UD.setStand(this);
        return true;
        //RoundaboutMod.LOGGER.info("MF");
    }

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

    @Override
    public void tick() {
        this.noClip = true;

        super.tick();
            if (this.isAlive() && !this.dead){
                if (this.getNeedsUser() && !this.isDisplay) {
                    if (this.getSelfData().getUser() != null) {
                        boolean userActive = this.getUserData(this.getMaster()).getActive();
                        if (this.getSelfData().getUser().isAlive() && userActive) {

                            //Make it fade in
                            if (this.getFadeOut() < MaxFade) {
                                this.incFadeOut(1);
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

    private void TickDown(){
        var currFade = this.getFadeOut();
        this.incFadeOut(-1);
        if (currFade <= 0) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    public Vec3d getStandOffsetVector(Entity standUser){
        int vis = this.getFadeOut();
        double r = (((double) vis /MaxFade)*1.37);
        if (r < 0.5) { r=0.5; }
        double yawfix = standUser.getYaw(); yawfix+=  this.getAnchorPlace(); if (yawfix >360){yawfix-=360;}else if (yawfix <0){yawfix+=360;}
        double ang = (yawfix-180)*Math.PI;

        double mcap = 0.3;
        Vec3d xyz = standUser.getVelocity();
        double yy = xyz.getY()*0.3; if (yy>mcap){yy=mcap;} else if (yy<-mcap){yy=-mcap;}
        if (isSwimming() || isCrawling() || isFallFlying()){yy+=1;}

        double x1=standUser.getX() - -1*(r*(Math.sin(ang/180)));
        double y1=standUser.getY()+0.1-yy;
        double z1=standUser.getZ()- (r*(Math.cos(ang/180)));


        return new Vec3d(x1, y1, z1);
    } //A math equation to determine where the stand floats relative to its user's body. Configurable.


    public boolean userActive(){
        if (this.getSelfData().getUser() != null){
            if (((IEntityDataSaver) getSelfData().getUser()).getPersistentData().get("active_stand") != null){
            UUID user3 = ((IEntityDataSaver) getSelfData().getUser()).getPersistentData().getUuid("active_stand");
            UUID user4 = this.getUuid();
            if (user3 != null && user4 != null) {
                return user3.equals(user4);
            }
        }
        }
        return false;
    } // Returns if stand is currently the user's summoned stand. Basically, if you resummon the old stand should go away.

    public static DefaultAttributeContainer.Builder createStandAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2F).add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
    } // Builds Minecraft entity attributes like speed and health

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", 5, state -> state.setAndContinue(DefaultAnimations.IDLE)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
