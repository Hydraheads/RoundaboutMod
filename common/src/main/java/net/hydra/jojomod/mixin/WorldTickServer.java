package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFishingRodAccess;
import net.hydra.jojomod.access.IItemEntityAccess;
import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.material.Fluid;
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

    @Inject(method = "tickNonPassenger", at = @At(value = "TAIL"))
    private void tickEntity2(Entity $$0, CallbackInfo ci) {
        if ($$0.showVehicleHealth()) {
            LivingEntity livingEntity = (LivingEntity) $$0;
            if (((StandUser) $$0).hasStandOut()) {
                this.tickStandIn(livingEntity, Objects.requireNonNull(((StandUser) $$0).getStand()));
            }
        }
    }


    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tickTimeStopList(BooleanSupplier $$0, CallbackInfo ci) {
        ((TimeStop) this).tickAllTimeStops();
    }


    private void tickStandIn(LivingEntity entity, StandEntity passenger) {
        if (passenger == null || passenger.isRemoved() || passenger.getUser() != entity) {
            return;
        }
        byte ot = passenger.getOffsetType();
        if (OffsetIndex.OffsetStyle(ot) != OffsetIndex.LOOSE_STYLE) {
            passenger.setOldPosAndRot();
            ++passenger.tickCount;
            passenger.tickStandOut();
        }
    }

    /**Time stop code*/
    @Inject(method = "tickFluid", at = @At(value = "Head"), cancellable = true)
    private void roundaboutFluidTick(BlockPos $$0x, Fluid $$1x, CallbackInfo ci) {
        if (((TimeStop) this).inTimeStopRange($$0x)){
                ((LevelAccessor) this).scheduleTick($$0x, $$1x, $$1x.getTickDelay(((LevelAccessor) this)));
            ci.cancel();
        }
    }
    @Inject(method = "tickBlock", at = @At(value = "Head"), cancellable = true)
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
            if (((TimeStop) this).CanTimeStopEntity($$0)){
                if ($$0 instanceof LivingEntity) {
                    ((StandUser)$$0).getStandPowers().timeTick();
                    ((ILivingEntityAccess) $$0).roundaboutPushEntities();
                } else if ($$0 instanceof ItemEntity) {
                    ((IItemEntityAccess)$$0).RoundaboutTickPickupDelay();
                } else if ($$0 instanceof FishingHook){
                    ((IFishingRodAccess)$$0).roundaboutUpdateRodInTS();
                }

                for (Entity $$2 : $$0.getPassengers()) {
                    this.tickPassenger($$0, $$2);
                }
                ci.cancel();
            }
        }
    }
    @Inject(method = "tickPassenger", at = @At(value = "HEAD"), cancellable = true)
    private void roundaboutTickEntity5(Entity $$0, Entity $$1, CallbackInfo ci) {
        if ($$1.isRemoved() || $$1.getVehicle() != $$0) {
            $$1.stopRiding();
        } else if ($$1 instanceof Player || this.entityTickList.contains($$1)) {
            if (((TimeStop) this).CanTimeStopEntity($$1)){
                if ($$1 instanceof LivingEntity) {
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

}
