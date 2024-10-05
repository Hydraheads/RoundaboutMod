package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.*;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public class WorldTickServer {

    /** Called every tick on the Server. Checks if a mob has a stand out, and updates the position of the stand.
     * @see StandEntity#tickStandOut */


    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tickTimeStopList(BooleanSupplier $$0, CallbackInfo ci) {
        ((TimeStop) this).tickAllTimeStops();

        this.entityTickList.forEach($$0x -> {
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


    private void tickStandIn(LivingEntity entity, StandEntity stand) {
        if (stand == null || stand.isRemoved()) {
            return;
        }
        if (stand.getFollowing().getId() != entity.getId()) {
            ((StandUser)entity).roundabout$removeFollower(stand);
            return;
        }
        byte ot = stand.getOffsetType();
        stand.setOldPosAndRot();
        ++stand.tickCount;
        stand.tickStandOut();
        for (Entity $$2 : stand.getPassengers()) {
            this.tickPassenger(stand, $$2);
        }
    }

    /**Time stop code*/
    @Inject(method = "tickFluid", at = @At(value = "HEAD"), cancellable = true)
    private void roundaboutFluidTick(BlockPos $$0x, Fluid $$1x, CallbackInfo ci) {
        if (((TimeStop) this).inTimeStopRange($$0x)){
                ((LevelAccessor) this).scheduleTick($$0x, $$1x, $$1x.getTickDelay(((LevelAccessor) this)));
            ci.cancel();
        }
    }
    @Inject(method = "tickBlock", at = @At(value = "HEAD"), cancellable = true)
    private void roundaboutBlockTick(BlockPos $$0x, Block $$1x, CallbackInfo ci) {
        if (((TimeStop) this).inTimeStopRange($$0x) && !($$1x instanceof CommandBlock)){
            ((LevelAccessor) this).scheduleTick($$0x, $$1x, 1);
            ci.cancel();
        }
    }

    @Shadow
    @Final
    EntityTickList entityTickList;

    @Shadow
    private void tickPassenger(Entity $$0, Entity $$1){
    }
    @Inject(method = "tickNonPassenger", at = @At(value = "HEAD"), cancellable = true)
    private void roundaboutTickEntity2(Entity $$0, CallbackInfo ci) {
        if (!$$0.isRemoved()) {
            if ($$0 instanceof StandEntity SE) {
                if (SE.getFollowing() != null){
                    ci.cancel();
                }
            }
            roundaboutTickTSDamage($$0);
            if (((TimeStop) this).CanTimeStopEntity($$0)){
                if ($$0 instanceof LivingEntity) {
                    ((LivingEntity) $$0).hurtTime = 0;
                    $$0.invulnerableTime = 0;
                    ((StandUser)$$0).roundabout$getStandPowers().timeTick();
                    ((ILivingEntityAccess) $$0).roundaboutPushEntities();
                } else if ($$0 instanceof ItemEntity) {
                    ((IItemEntityAccess)$$0).RoundaboutTickPickupDelay();
                } else if ($$0 instanceof FishingHook){
                    ((IFishingRodAccess)$$0).roundaboutUpdateRodInTS();
                } else if ($$0 instanceof Boat){
                    ((IBoatAccess)$$0).roundaboutTickLerp();
                    $$0.lerpTo($$0.getX(),$$0.getY(),$$0.getZ(),$$0.getYRot(),$$0.getXRot(),3,false);
                    $$0.walkDistO = $$0.walkDist;
                    $$0.xRotO = $$0.getXRot();
                    $$0.yRotO = $$0.getYRot();
                }

                for (Entity $$2 : $$0.getPassengers()) {
                    this.tickPassenger($$0, $$2);
                }
                ci.cancel();
            }
        }
    }
    @Inject(method = "tickNonPassenger", at = @At(value = "TAIL"), cancellable = true)
    private void roundaboutTickEntityX(Entity $$0, CallbackInfo ci) {
        if (!$$0.isRemoved()) {
            if ($$0 instanceof LivingEntity LE) {
                for (StandEntity SE : ((StandUser) $$0).roundabout$getFollowers()) {
                    this.tickStandIn(LE, SE);
                }
            }
        }
    }

    @Inject(method = "tickPassenger", at = @At(value = "HEAD"), cancellable = true)
    private void roundaboutTickEntity5(Entity $$0, Entity $$1, CallbackInfo ci) {

        if ($$1 instanceof StandEntity SE) {
            if (SE.getFollowing() != null){
                ci.cancel();
            }
        }
        if ($$1 instanceof LivingEntity) {
            ((StandUser) $$1).roundabout$UniversalTick();
        }
        roundaboutTickTSDamage($$1);
        if ($$1.isRemoved() || $$1.getVehicle() != $$0) {
            $$1.stopRiding();
        } else if ($$1 instanceof Player || this.entityTickList.contains($$1)) {
            if (((TimeStop) this).CanTimeStopEntity($$1)){
                if ($$1 instanceof LivingEntity) {
                    $$1.invulnerableTime = 0;
                    ((LivingEntity) $$1).hurtTime = 0;
                    ((ILivingEntityAccess) $$1).roundaboutPushEntities();
                } else if ($$1 instanceof ItemEntity) {
                    ((IItemEntityAccess)$$1).RoundaboutTickPickupDelay();
                } else if ($$1 instanceof FishingHook){
                    ((IFishingRodAccess)$$1).roundaboutUpdateRodInTS();
                }

                for (Entity $$3 : $$1.getPassengers()) {
                    this.tickPassenger($$1, $$3);
                }
                ci.cancel();
            }
        }
    }

    @Inject(method = "tickPassenger", at = @At(value = "TAIL"), cancellable = true)
    private void roundaboutTickEntity6(Entity $$0, Entity $$1, CallbackInfo ci) {
        if ($$1 instanceof LivingEntity LE) {
            for (StandEntity SE : ((StandUser)$$1).roundabout$getFollowers()) {
                this.tickStandIn(LE, SE);
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void roundaboutTickEntity3(BooleanSupplier $$0, CallbackInfo ci) {
        ((TimeStop) this).tickTimeStoppingEntity();
    }
    @Inject(method = "tickChunk", at = @At(value = "HEAD"), cancellable = true)
    private void roundaboutTickChunk(LevelChunk $$0, int $$1, CallbackInfo ci) {
        ChunkPos $$2 = $$0.getPos();
        BlockPos BP = $$2.getWorldPosition();
        if (((TimeStop) this).inTimeStopRange(new Vec3i(BP.getX(),BP.getY(),BP.getZ()))){
            ci.cancel();
        }
    }

    private void roundaboutTickTSDamage(Entity entity){
        if (entity instanceof LivingEntity){
            ((StandUser)entity).roundabout$UniversalTick();
            if (!(((TimeStop) this).CanTimeStopEntity(entity)) && ((StandUser)entity).roundabout$getStoredDamage() > 0){

                if (DamageHandler.TimeDamageEntityAttack(entity,
                        ((StandUser)entity).roundabout$getStoredDamage(), 0, ((StandUser)entity).roundaboutGetStoredAttacker())){
                    entity.hurtMarked = true;
                    entity.setDeltaMovement(Objects.requireNonNull(((IEntityAndData) entity).getRoundaboutDeltaBuildupTS()));
                    int TSHurt = ((StandUser)entity).roundaboutGetTSHurtSound();
                    if (TSHurt != 0){
                        if (TSHurt == 1){
                            ((ServerLevel)(Object) this).playSound(null, entity, ModSounds.TIME_STOP_IMPACT2_EVENT, SoundSource.PLAYERS, 1F, (float) (1.5 + (Math.random() * 0.01) - 0.005));
                        } else if (TSHurt == 2){
                            ((ServerLevel)(Object) this).playSound(null, entity, ModSounds.TIME_STOP_IMPACT_EVENT, SoundSource.PLAYERS, 1F, (float) (1.2 + (Math.random() * 0.01) - 0.005));
                        } else if (TSHurt == 3){
                            ((ServerLevel)(Object) this).playSound(null, entity, ModSounds.TIME_STOP_IMPACT_EVENT, SoundSource.PLAYERS, 2F, (float) (0.95 + (Math.random() * 0.01) - 0.005));
                        }
                    }
                    ((StandUser)entity).roundaboutSetTSHurtSound(0);
                    entity.hasImpulse = true;
                }
                ((StandUser)entity).roundabout$setStoredDamage(0);
                ((StandUser)entity).roundaboutSetStoredAttacker(null);
                ((IEntityAndData)entity).setRoundaboutDeltaBuildupTS(new Vec3(0,0,0));
            }
        }
    }

}
