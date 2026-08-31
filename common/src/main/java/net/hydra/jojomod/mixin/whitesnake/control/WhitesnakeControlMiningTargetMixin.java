package net.hydra.jojomod.mixin.whitesnake.control;

import net.hydra.jojomod.client.WhitesnakeControlClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MultiPlayerGameMode.class, priority = 2000)
public abstract class WhitesnakeControlMiningTargetMixin {
    @Inject(method = "startDestroyBlock", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$rejectBodyTarget(BlockPos pos, Direction direction,
                                                       CallbackInfoReturnable<Boolean> cir) {
        if (WhitesnakeControlClient.rejectsBodyMiningTarget(Minecraft.getInstance(), pos)) {
            cir.setReturnValue(false);
        }
    }
}
