package net.hydra.jojomod.mixin.forge;

import net.hydra.jojomod.block.CoffinBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ForgeEventFactory.class)
public abstract class ForgeCoffinSleepAtDayMixin {

    @Inject(method = "fireSleepingTimeCheck(Lnet/minecraft/world/entity/player/Player;Ljava/util/Optional;)Z", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private static void forgeAllowCoffinSleepAtDay(Player player, Optional<BlockPos> sleepingLocation, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayer serverPlayer = (ServerPlayer)(Object)player;

        if (sleepingLocation.isPresent()) {
            if (serverPlayer.level().getBlockState(sleepingLocation.get()).getBlock() instanceof CoffinBlock) {
                cir.setReturnValue(true);

                SleepingTimeCheckEvent evt = new SleepingTimeCheckEvent(player, sleepingLocation);
                MinecraftForge.EVENT_BUS.post(evt);

                Event.Result canContinueSleep = evt.getResult();
                if (serverPlayer.level().isDay() || serverPlayer.level().isThundering()) {
                    cir.setReturnValue(true);
                } else {
                    cir.setReturnValue(false);
                }
                return;
            }
        }
    }
}

