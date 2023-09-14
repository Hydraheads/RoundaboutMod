package net.hydra.jojomod.entity;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.access.IStandUser;
import net.hydra.jojomod.mixin.EntityStandMixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

public abstract class StandEntity extends MobEntity implements GeoEntity {
    private static final TrackedData<Integer> FadeOut = DataTracker.registerData(StandEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(StandEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    protected static final TrackedData<Integer> OWNER_ID = DataTracker.registerData(StandEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final Integer MaxFade = 8;


    public Integer getMaxFade() {return MaxFade;}
    public Integer getFadeOut() {
        return this.dataTracker.get(FadeOut);
    }
    public Integer getOwnerID() {
        return this.dataTracker.get(OWNER_ID);
    }
    public void setOwnerID(Integer yeet){this.dataTracker.set(OWNER_ID, yeet);
    }
    public UUID getOwnerUuid() {
        return this.dataTracker.get(OWNER_UUID).orElse(null);
    }
    public void setOwnerUuid(@Nullable UUID uuid) {
        this.dataTracker.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    public void incFadeOut(Integer inc) {
        this.dataTracker.set(FadeOut, this.dataTracker.get(FadeOut)+inc);
    }
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(FadeOut, 1);
        this.dataTracker.startTracking(OWNER_UUID, Optional.empty());
        this.dataTracker.startTracking(OWNER_ID, -1);
    }

    private final AnimatableInstanceCache cache =
            GeckoLibUtil.createInstanceCache(this);


    protected StandEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }


    @Override
    public void tick() {
        this.noClip = true;

        if (!((IStandUser) this).hasMaster()){
            int id = getOwnerID();
            if (id > 0){
                Entity ep = this.getWorld().getEntityById(id);
                if (ep != null && ep.isAlive()){
                    ((IStandUser) ep).startStandRiding(this, true);
                }
            }
            // ((IStandUser) (PlayerEntity) player).startStandRiding(stand, true);
        }

        super.tick();

        if (!this.getWorld().isClient() && this.isAlive() && !this.dead) {
            Entity standUser = getStandUser();
            if (standUser != null && standUser.isAlive() && userActive()) {
                //Make it fade in
                if (getFadeOut() < MaxFade) {incFadeOut(1);}
                //Here is the Stand Movement Code

//                this.setPitch(standUser.getPitch());
//                this.setYaw(standUser.getYaw());
//                this.setBodyYaw(standUser.getBodyYaw());
//                this.setHeadYaw(standUser.getHeadYaw());
//
//                Direction test= this.getHorizontalFacing();
//
//                double r = 1.5;
//                double yawfix = standUser.getYaw(); yawfix+= 50; if (yawfix >360){yawfix-=360;}
//                double ang = (yawfix-180)*Math.PI;
//                double x1=standUser.getX() - -1*(r*(Math.sin(ang/180)));
//                double z1=standUser.getZ()- (r*(Math.cos(ang/180)));
//
//                this.setPosition(x1,standUser.getY(),z1);
            } else {
                incFadeOut(-1);
                if (getFadeOut() <= 0) {

                    this.remove(RemovalReason.DISCARDED);
                }
            }
        }
        //this.noClip = false;
        this.setNoGravity(true);
    }



    public Entity getStandUser(){
        UUID user = getOwnerUuid();
        return ((ServerWorld) this.getWorld()).getEntity(getOwnerUuid());
    }  public boolean userActive(){
        UUID user = getOwnerUuid();
        if (user != null){
        Entity user2 = ((ServerWorld) this.getWorld()).getEntity(getOwnerUuid());
        if (user2 != null){
            if (((IEntityDataSaver) user2).getPersistentData().get("active_stand") != null){
            UUID user3 = ((IEntityDataSaver) user2).getPersistentData().getUuid("active_stand");
            UUID user4 = this.getUuid();
            if (user3 != null && user4 != null) {
                return user3.equals(user4);
            }
        }}
        }
        return false;
    }

//    public void tickRiding() {
//        this.setVelocity(Vec3d.ZERO);
//        this.tick();
//        if (!this.hasVehicle()) {
//            return;
//        }
//        this.getVehicle().updatePassengerPosition(this);
//    }


    public static DefaultAttributeContainer.Builder createStandAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2F).add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
    }
    //
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", 5, state -> state.setAndContinue(DefaultAnimations.IDLE)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
