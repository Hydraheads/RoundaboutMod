package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.*;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class WorldTickClient extends Level {

    /** Called every tick on the Client. Checks if a mob has a stand out, and updates the position of the stand.
     * @see StandEntity#tickStandOut */

    @Shadow
    @Final
   EntityTickList tickingEntities;

    protected WorldTickClient(WritableLevelData $$0, ResourceKey<Level> $$1, RegistryAccess $$2, Holder<DimensionType> $$3, Supplier<ProfilerFiller> $$4, boolean $$5, boolean $$6, long $$7, int $$8) {
        super($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7, $$8);
    }


    @Unique
    private void roundabout$standTickCheck(Entity entity){
        if (entity.showVehicleHealth()) {
            LivingEntity livingEntity = (LivingEntity) entity;
            if (((StandUser) livingEntity).roundabout$getStand() != null) {
                StandEntity stand = ((StandUser) livingEntity).roundabout$getStand();
                if (stand.getFollowing() != null && stand.getFollowing().getId() == livingEntity.getId()){

                    if (!(entity.getVehicle() != null && entity.getVehicle() == ((StandUser) entity).roundabout$getStand())) {
                        this.roundabout$tickStandIn(livingEntity, stand);
                    }
                }
            }
        }
    }

    @Unique
    private void roundabout$updateStandTS(Entity entity){
        if (entity.showVehicleHealth()) {
            LivingEntity livingEntity = (LivingEntity) entity;
            if (((StandUser) livingEntity).roundabout$getStand() != null) {
                StandEntity stand = ((StandUser) livingEntity).roundabout$getStand();
                if (stand.getFollowing() != null && stand.getFollowing().getId() == livingEntity.getId()){
                    if (!(stand.isRemoved() || stand.getUser() != entity)) {
                        roundabout$TickLivingEntityTS(stand);
                    }
                }
            }
        }
    }

    @Unique
    private void roundabout$tickStandIn(LivingEntity entity, StandEntity stand) {
        if (stand == null || stand.isRemoved()) {
            ((StandUser)entity).roundabout$removeFollower(stand);
            return;
        }
        if (stand.getFollowing() != null && stand.getFollowing().getId() != entity.getId()) {
            ((StandUser)entity).roundabout$removeFollower(stand);
            return;
        }
        byte ot = stand.getOffsetType();
        ++stand.tickCount;
        stand.setOldPosAndRot();
        stand.tickStandOut();
        for (Entity $$2 : stand.getPassengers()) {
            this.tickPassenger(stand, $$2);
        }
    }


    @Unique
    private void roundabout$TickStandTS(StandEntity stand){
        stand.setOldPosAndRot();
        stand.tickStandOut2();
    }
    @Unique
    private void roundabout$TickLivingEntityTS(LivingEntity livingEntity){
        if (livingEntity instanceof Player){
            Inventory inv = ((IPlayerEntity) livingEntity).roundabout$GetInventory();
            int idx = 0;
            for(NonNullList<ItemStack> nonnulllist : ((ZInventoryAccess)inv).roundabout$GetCompartments()) {
                for(int i = 0; i < nonnulllist.size(); ++i) {

                    if (!nonnulllist.get(i).isEmpty()) {
                        if (nonnulllist.get(i).getPopTime() > 0) {
                            nonnulllist.get(i).setPopTime(nonnulllist.get(i).getPopTime()-1);
                        }
                    }
                    idx++;
                }
            }
        }


        ((ILivingEntityAccess) livingEntity).roundabout$setAnimStepO(((ILivingEntityAccess) livingEntity).roundabout$getAnimStep());
        livingEntity.setOldPosAndRot();
        livingEntity.yBodyRotO = livingEntity.yBodyRot;
        livingEntity.yHeadRotO = livingEntity.yHeadRot;
        livingEntity.oAttackAnim = livingEntity.attackAnim;
        //livingEntity.lastLimbDistance = livingEntity.limbDistance;

        if (livingEntity instanceof StandEntity){
            livingEntity.setPos(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());

        } else {
            int LS = ((ILivingEntityAccess) livingEntity).roundabout$getLerpSteps();
            if (LS > 0) {
                double LX = livingEntity.getX() + (((ILivingEntityAccess) livingEntity).roundabout$getLerpX() - livingEntity.getX()) / (double) LS;
                double LY = livingEntity.getY() + (((ILivingEntityAccess) livingEntity).roundabout$getLerpY() - livingEntity.getY()) / (double) LS;
                double LZ = livingEntity.getZ() + (((ILivingEntityAccess) livingEntity).roundabout$getLerpZ() - livingEntity.getZ()) / (double) LS;
                ((ILivingEntityAccess) livingEntity).roundabout$setLerpSteps(LS - 1);
                livingEntity.setPos(LX, LY, LZ);
            }

            ((ILivingEntityAccess) livingEntity).roundabout$PushEntities();
        }
    }
    @Shadow
    private void tickPassenger(Entity $$0, Entity $$1) {
    }

    @Shadow @Final private Minecraft minecraft;

    @Unique
    public void roundabout$TSTickEntity(Entity $$0){
        if ($$0 instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) $$0;
            roundabout$TickLivingEntityTS(livingEntity);
            ((StandUser)livingEntity).roundabout$getStandPowers().timeTick();
            roundabout$updateStandTS(livingEntity);
            $$0.invulnerableTime = 0;
            ((LivingEntity) $$0).hurtTime = 0;
        } else {
            $$0.walkDistO = $$0.walkDist;

            $$0.xOld = $$0.getX();;
            $$0.yOld = $$0.getY();
            $$0.zOld = $$0.getZ();

            if ($$0 instanceof FishingHook){
                $$0.xo = $$0.getX();
                $$0.yo = $$0.getY();
                $$0.zo = $$0.getZ();
            } else if ($$0 instanceof Boat){
                $$0.xo = $$0.getX();
                $$0.yo = $$0.getY();
                $$0.zo = $$0.getZ();
                ((IBoatAccess)$$0).roundabout$TickLerp();
                $$0.lerpTo($$0.getX(),$$0.getY(),$$0.getZ(),$$0.getYRot(),$$0.getXRot(),3,false);
                $$0.walkDistO = $$0.walkDist;
                $$0.xRotO = $$0.getXRot();
                $$0.yRotO = $$0.getYRot();
            }
        }
        if ($$0 instanceof ItemEntity) {
            ((IItemEntityAccess)$$0).roundabout$TickPickupDelay();
        } else if ($$0 instanceof FishingHook){
            ((IFishingRodAccess)$$0).roundabout$UpdateRodInTS();
        }
    }

    /**Time Stop Code*/
    @Inject(method = "tickNonPassenger", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$TickEntity2(Entity $$0, CallbackInfo ci) {
        if (!$$0.isRemoved()) {
            if ($$0 instanceof StandEntity SE) {
                if (SE.getFollowing() != null){
                    ci.cancel();
                }
            }

            if ($$0 instanceof LivingEntity) {
                ((StandUser) $$0).roundabout$UniversalTick();
            }

            roundabout$StoreOldPositionsForTS($$0);
            if (((TimeStop) this).CanTimeStopEntity($$0)){
                roundabout$TSTickEntity($$0);
                for (Entity $$1 : $$0.getPassengers()) {
                    this.tickPassenger($$0, $$1);
                }
                ci.cancel();
            }
        }
    }
    @Inject(method = "tickNonPassenger", at = @At(value = "TAIL"), cancellable = true)
    private void roundabout$TickEntityX(Entity $$0, CallbackInfo ci) {
        if (!$$0.isRemoved()) {
            if ($$0 instanceof LivingEntity LE) {
                for (StandEntity SE : ((StandUser) $$0).roundabout$getFollowers()) {
                    this.roundabout$tickStandIn(LE, SE);
                }
            }
        }
    }

    @Unique
    public void roundabout$StoreOldPositionsForTS(Entity entity){
        ((IEntityAndData) entity).roundabout$setRoundaboutPrevX(entity.getX());
        ((IEntityAndData) entity).roundabout$setRoundaboutPrevY(entity.getY());
        ((IEntityAndData) entity).roundabout$setRoundaboutPrevZ(entity.getZ());
    }

    @Inject(method = "tickPassenger", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$TickEntity5(Entity $$0, Entity $$1, CallbackInfo ci) {
        if ($$1 instanceof StandEntity SE) {
            if (SE.getFollowing() != null){
                ci.cancel();
            }
        }
        if ($$1 instanceof LivingEntity) {
            ((StandUser) $$1).roundabout$UniversalTick();
        }

        roundabout$StoreOldPositionsForTS($$1);
        if ($$1.isRemoved() || $$1.getVehicle() != $$0) {
            $$1.stopRiding();
        } else if ($$1 instanceof Player || this.tickingEntities.contains($$1)) {
            if (((TimeStop) this).CanTimeStopEntity($$1)) {
                $$1.setDeltaMovement(Vec3.ZERO);
                roundabout$TSTickEntity($$1);
                if ($$1.isPassenger()) {
                    $$1.getVehicle().positionRider($$1);
                }
                for (Entity $$2 : $$1.getPassengers()) {
                    this.tickPassenger($$1, $$2);
                }
                ci.cancel();
            }
        }
    }
    @Inject(method = "tickPassenger", at = @At(value = "TAIL"), cancellable = true)
    private void roundabout$TickEntity6(Entity $$0, Entity $$1, CallbackInfo ci) {
        if ($$1 instanceof LivingEntity LE) {
            for (StandEntity SE : ((StandUser)$$1).roundabout$getFollowers()) {
                this.roundabout$tickStandIn(LE, SE);
            }
        }
    }

    @Shadow
    @Final
    private ClientLevel.ClientLevelData clientLevelData;

    @Shadow
    public void setGameTime(long L){}

    @Shadow
    public void setDayTime(long L) {}

    /**When time ticks amidst a time change, use the actual time rater
     * than the custom time to tick the proper time*/
    @Inject(method = "tickTime", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$TickTime(CallbackInfo ci) {
        if (((IClientLevelData) this.levelData).roundabout$getRoundaboutInterpolatingDaytime()) {
            this.setGameTime(this.levelData.getGameTime() + 1L);
            if (this.levelData.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
                this.setDayTime(((IClientLevelData)this.levelData).roundabout$getRoundaboutDayTimeMinecraft() + 1L);
                ci.cancel();
            }
        }
    }

    @Inject(method = "tickEntities", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$TickEntity3(CallbackInfo ci) {
        if (minecraft.player != null){
            if (((TimeStop) this).isTimeStoppingEntity(minecraft.player)) {
                ((StandUser) minecraft.player).roundabout$getStandPowers().timeTickStopPower();
            }
        }
        this.tickingEntities.forEach($$0x -> {
            if ($$0x instanceof StandEntity standEntity) {
                if (standEntity.getFollowing() != null){
                    LivingEntity LE = standEntity.getFollowing();
                    if (!((StandUser)LE).roundabout$hasFollower(standEntity)){
                        ((StandUser)LE).roundabout$addFollower(standEntity);
                    }
                }
            }
        });
    }

    @Inject(method = "addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", at = @At("HEAD"), cancellable = true)
    private void roundabout$StopParticles(ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfo ci) {
        if (((TimeStop) this).inTimeStopRange(new Vec3i((int) x, (int) y, (int) z))){
            ci.cancel();
        }
    }

    @Inject(method = "playLocalSound", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$PlayLocalSoundCanceler(double $$0, double $$1, double $$2, SoundEvent $$3, SoundSource $$4, float $$5, float $$6, boolean $$7, CallbackInfo ci) {
        if (((TimeStop) this).inTimeStopRange(new Vec3i((int) $$0, (int) $$1, (int) $$2))){
            if (($$4 == SoundSource.WEATHER || $$4 == SoundSource.BLOCKS) && !$$3.getLocation().getPath().contains("break")) {
                ci.cancel();
            }
        }
    }

}
