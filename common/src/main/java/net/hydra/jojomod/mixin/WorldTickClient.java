package net.hydra.jojomod.mixin;


import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ClientLevel.class)
public class WorldTickClient {

    /** Called every tick on the Client. Checks if a mob has a stand out, and updates the position of the stand.
     * @see StandEntity#tickStandOut */

    @Shadow
    @Final
   EntityTickList tickingEntities;

    @Inject(method = "tickNonPassenger", at = @At(value = "TAIL"))
    private void roundaboutTickEntity(Entity $$0, CallbackInfo ci) {

        if (!$$0.isRemoved()) {
            if ($$0 instanceof LivingEntity) {
                ((StandUser) $$0).roundaboutUniversalTick();
            }
            this.standTickCheck($$0);
            for (Entity entity2 : $$0.getPassengers()) {
                if (!entity2.isRemoved()) {
                    this.standTickCheck(entity2);
                }
            }
        }
    }

    private void standTickCheck(Entity entity){
        if (entity.showVehicleHealth()) {
            LivingEntity livingEntity = (LivingEntity) entity;
            if (((StandUser) livingEntity).getStand() != null) {
                StandEntity stand = ((StandUser) livingEntity).getStand();
                if (stand.getFollowing() != null && stand.getFollowing().getId() == livingEntity.getId()){
                    this.tickStandIn(livingEntity, stand);
                }
            }
        }
    }

    private void updateStandTS(Entity entity){
        if (entity.showVehicleHealth()) {
            LivingEntity livingEntity = (LivingEntity) entity;
            if (((StandUser) livingEntity).getStand() != null) {
                StandEntity stand = ((StandUser) livingEntity).getStand();
                if (stand.getFollowing() != null && stand.getFollowing().getId() == livingEntity.getId()){
                    if (!(stand.isRemoved() || stand.getUser() != entity)) {
                        roundaboutTickStandTS(stand);
                    }
                }
            }
        }
    }

    private void tickStandIn(LivingEntity entity, StandEntity stand) {
        if (stand == null || stand.isRemoved() || stand.getUser() != entity) {
            return;
        }
        byte ot = stand.getOffsetType();
        ++stand.tickCount;
        if (OffsetIndex.OffsetStyle(ot) != OffsetIndex.LOOSE_STYLE) {
            stand.setOldPosAndRot();
            stand.tickStandOut();
        }
    }


    private void roundaboutTickStandTS (StandEntity stand){
        stand.setOldPosAndRot();
        stand.tickStandOut2();
    }
    private void roundaboutTickLivingEntityTS (LivingEntity livingEntity){
        if (livingEntity instanceof Player){
            Inventory inv = ((IPlayerEntity) livingEntity).roundaboutGetInventory();
            int idx = 0;
            for(NonNullList<ItemStack> nonnulllist : ((ZInventoryAccess)inv).roundaboutGetCompartments()) {
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


        ((ILivingEntityAccess) livingEntity).setAnimStepO(((ILivingEntityAccess) livingEntity).getAnimStep());
        livingEntity.setOldPosAndRot();
        livingEntity.yBodyRotO = livingEntity.yBodyRot;
        livingEntity.yHeadRotO = livingEntity.yHeadRot;
        livingEntity.oAttackAnim = livingEntity.attackAnim;
        //livingEntity.lastLimbDistance = livingEntity.limbDistance;

        int LS = ((ILivingEntityAccess) livingEntity).getLerpSteps();
        if (LS > 0) {
            double LX = livingEntity.getX() + (((ILivingEntityAccess) livingEntity).getLerpX() - livingEntity.getX()) / (double) LS;
            double LY = livingEntity.getY() + (((ILivingEntityAccess) livingEntity).getLerpY() - livingEntity.getY()) / (double) LS;
            double LZ = livingEntity.getZ() + (((ILivingEntityAccess) livingEntity).getLerpZ() - livingEntity.getZ()) / (double) LS;
            ((ILivingEntityAccess) livingEntity).setLerpSteps(LS-1);
            livingEntity.setPos(LX,LY,LZ);
        }

        ((ILivingEntityAccess) livingEntity).roundaboutPushEntities();
    }
                                               @Shadow
    private void tickPassenger(Entity $$0, Entity $$1) {
    }

    @Shadow @Final private Minecraft minecraft;

    public void roundaboutTSTickEntity(Entity $$0){
        if ($$0 instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) $$0;
            roundaboutTickLivingEntityTS(livingEntity);
            ((StandUser)livingEntity).getStandPowers().timeTick();
            updateStandTS(livingEntity);
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
                ((IBoatAccess)$$0).roundaboutTickLerp();
                $$0.lerpTo($$0.getX(),$$0.getY(),$$0.getZ(),$$0.getYRot(),$$0.getXRot(),3,false);
                $$0.walkDistO = $$0.walkDist;
                $$0.xRotO = $$0.getXRot();
                $$0.yRotO = $$0.getYRot();
            }
        }
        if ($$0 instanceof ItemEntity) {
            ((IItemEntityAccess)$$0).RoundaboutTickPickupDelay();
        } else if ($$0 instanceof FishingHook){
            ((IFishingRodAccess)$$0).roundaboutUpdateRodInTS();
        }
    }

    /**Time Stop Code*/
    @Inject(method = "tickNonPassenger", at = @At(value = "HEAD"), cancellable = true)
    private void roundaboutTickEntity2(Entity $$0, CallbackInfo ci) {
        if (!$$0.isRemoved()) {
            roundaboutStoreOldPositionsForTS($$0);
            if (((TimeStop) this).CanTimeStopEntity($$0)){
                roundaboutTSTickEntity($$0);
                for (Entity $$1 : $$0.getPassengers()) {
                    this.tickPassenger($$0, $$1);
                }
                ci.cancel();
            }
        }
    }

    public void roundaboutStoreOldPositionsForTS(Entity entity){
        ((IEntityAndData) entity).setRoundaboutPrevX(entity.getX());
        ((IEntityAndData) entity).setRoundaboutPrevY(entity.getY());
        ((IEntityAndData) entity).setRoundaboutPrevZ(entity.getZ());
    }

    @Inject(method = "tickPassenger", at = @At(value = "HEAD"), cancellable = true)
    private void roundaboutTickEntity5(Entity $$0, Entity $$1, CallbackInfo ci) {
        if ($$1 instanceof LivingEntity) {
            ((StandUser) $$1).roundaboutUniversalTick();
        }
        roundaboutStoreOldPositionsForTS($$1);
        if ($$1.isRemoved() || $$1.getVehicle() != $$0) {
            $$1.stopRiding();
        } else if ($$1 instanceof Player || this.tickingEntities.contains($$1)) {
            if (((TimeStop) this).CanTimeStopEntity($$1)) {
                $$1.setDeltaMovement(Vec3.ZERO);
                roundaboutTSTickEntity($$0);
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
    @Inject(method = "tickEntities", at = @At(value = "HEAD"), cancellable = true)
    private void roundaboutTickEntity3(CallbackInfo ci) {
        if (minecraft.player != null){
            if (((TimeStop) this).isTimeStoppingEntity(minecraft.player)) {
                ((StandUser) minecraft.player).getStandPowers().timeTickStopPower();
            }
        }
    }

    @Inject(method = "addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", at = @At("HEAD"), cancellable = true)
    private void roundaboutStopParticles(ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfo ci) {
        if (((TimeStop) this).inTimeStopRange(new Vec3i((int) x, (int) y, (int) z))){
            ci.cancel();
        }
    }

    @Inject(method = "playLocalSound", at = @At(value = "HEAD"), cancellable = true)
    private void roundaboutPlayLocalSoundCanceler(double $$0, double $$1, double $$2, SoundEvent $$3, SoundSource $$4, float $$5, float $$6, boolean $$7, CallbackInfo ci) {
        if (((TimeStop) this).inTimeStopRange(new Vec3i((int) $$0, (int) $$1, (int) $$2))){
            if (($$4 == SoundSource.WEATHER || $$4 == SoundSource.BLOCKS) && !$$3.getLocation().getPath().contains("break")) {
                ci.cancel();
            }
        }
    }
}
