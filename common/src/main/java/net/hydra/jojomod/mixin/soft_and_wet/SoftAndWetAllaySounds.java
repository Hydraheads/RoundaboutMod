package net.hydra.jojomod.mixin.soft_and_wet;

import net.hydra.jojomod.access.ILevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/world/entity/animal/allay/Allay$VibrationUser")
public class SoftAndWetAllaySounds {

    /**This mixin is in relation to plundering sound. When sound is plundered from an area, allays cannot hear it.*/
    @Inject(method = "canReceiveVibration(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/gameevent/GameEvent;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)Z", at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$canReceiveVibration(ServerLevel $$0, BlockPos $$1, GameEvent $$2, GameEvent.Context $$3, CallbackInfoReturnable<Boolean> cir) {
        if (((ILevelAccess)$$0).roundabout$isSoundPlundered($$1)){
            cir.setReturnValue(false);
        }
    }
}
