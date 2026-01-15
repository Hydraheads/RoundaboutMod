package net.hydra.jojomod.mixin.blocks;

import net.hydra.jojomod.block.CoffinBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class CoffinPreventWakeupMixin {

    @Inject(method = "stopSleepInBed", at = @At("HEAD"), cancellable = true)
    private void preventCoffinWakeup(boolean wakeImmediately, boolean updateLevelForSleepingPlayers, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer)(Object)this;
        if (player.getSleepingPos().isEmpty()) return;
        BlockPos pos = player.getSleepingPos().get();

        if (player.level().getBlockState(pos).getBlock() instanceof CoffinBlock && !wakeImmediately && player.level().isDay()
                || player.level().getBlockState(pos).getBlock() instanceof CoffinBlock && !wakeImmediately && player.level().isNight() && player.level().isThundering()) {
            ci.cancel();
        }
    }
}