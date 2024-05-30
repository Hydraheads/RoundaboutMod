package net.hydra.jojomod.mixin;

import com.google.common.collect.ImmutableList;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public class WorldTickServer {
    private ImmutableList<Entity> passengers = ImmutableList.of();

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
            ci.cancel();
        }
    }
    @Inject(method = "tickBlock", at = @At(value = "Head"), cancellable = true)
    private void roundaboutBlockTick(BlockPos $$0x, Block $$1x, CallbackInfo ci) {
        if (((TimeStop) this).inTimeStopRange($$0x)){
            ci.cancel();
        }
    }
    @Inject(method = "tickNonPassenger", at = @At(value = "HEAD"), cancellable = true)
    private void roundaboutTickEntity2(Entity $$0, CallbackInfo ci) {
        if (!$$0.isRemoved()) {
            if (((TimeStop) this).CanTimeStopEntity($$0)){
                ci.cancel();
            }
        }
    }
    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void roundaboutTickEntity3(BooleanSupplier $$0, CallbackInfo ci) {
        ((TimeStop) this).tickTimeStoppingEntity();
    }

}
