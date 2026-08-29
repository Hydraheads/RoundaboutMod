package net.hydra.jojomod.mixin.silver_chariot;

import net.hydra.jojomod.client.SilverChariotClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MultiPlayerGameMode.class, priority = 2000)
public abstract class SilverChariotControlMiningTargetMixin {
    @Inject(method = "startDestroyBlock", at = @At("HEAD"), cancellable = true)
    private void roundaboutSilverChariot$rejectBodyTarget(BlockPos pos, Direction direction,
                                                       CallbackInfoReturnable<Boolean> cir) {
        if (SilverChariotClient.rejectsBodyMiningTarget(Minecraft.getInstance(), pos)) {
            cir.setReturnValue(false);
        }
    }
}
