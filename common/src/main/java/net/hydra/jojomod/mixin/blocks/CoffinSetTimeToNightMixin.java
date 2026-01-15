package net.hydra.jojomod.mixin.blocks;

import net.hydra.jojomod.block.CoffinBlock;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public abstract class CoffinSetTimeToNightMixin {

    private int coffinSleepDelayTicks = 0;
    private static final int TIMER_CAP = 100;

    @Inject(method = "tick", at = @At("HEAD"))
    private void skipTimeToNight(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        ServerLevel level = (ServerLevel) (Object) this;

        var coffinSleepers = level.players().stream().filter(player -> player.isSleeping() && player.getSleepingPos().isPresent() && level.getBlockState(player.getSleepingPos().get()).getBlock() instanceof CoffinBlock).toList();
        if (coffinSleepers.isEmpty()) {
            coffinSleepDelayTicks = 0;
            return;
        }

        long dayTime = level.getDayTime() % 24000L;
        boolean isThundering = level.isThundering();

        coffinSleepDelayTicks++;
        if (coffinSleepDelayTicks < TIMER_CAP) return;

        if (dayTime < 13000L) {
            long timeToNight = 13000L - dayTime;
            level.setDayTime(level.getDayTime() + timeToNight);

            for (ServerPlayer player : coffinSleepers) {
                player.stopSleepInBed(true, false);
            }
        }

        if (isThundering || level.isRaining()) {
            int clearSecs = 1200;
            level.setWeatherParameters(clearSecs, 0, false, false);

            for (ServerPlayer player : coffinSleepers) {
                player.stopSleepInBed(true, false);
            }

            coffinSleepDelayTicks = 0;
        }
    }
}