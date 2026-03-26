package net.hydra.jojomod.mixin.blocks;

import net.hydra.jojomod.block.CoffinBlock;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.SleepStatus;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class CoffinSetTimeToNightMixin extends Level {

    @Shadow
    @Final
    private SleepStatus sleepStatus;
    @Shadow
    @Final
    private List<ServerPlayer> players;

    @Shadow
    protected abstract void resetWeatherCycle();

    private int coffinSleepDelayTicks = 0;
    private static final int TIMER_CAP = 100;

    public SleepStatus rdbt$coffinStatus;

    protected CoffinSetTimeToNightMixin(WritableLevelData $$0, ResourceKey<Level> $$1, RegistryAccess $$2, Holder<DimensionType> $$3, Supplier<ProfilerFiller> $$4, boolean $$5, boolean $$6, long $$7, int $$8) {
        super($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7, $$8);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void skipTimeToNight(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        ServerLevel level = (ServerLevel) (Object) this;

        var coffinSleepers = level.players().stream().filter(player -> player.isSleeping() && player.getSleepingPos().isPresent() && level.getBlockState(player.getSleepingPos().get()).getBlock() instanceof CoffinBlock).toList();

        if (coffinSleepers.isEmpty()) {
            coffinSleepDelayTicks = 0;
            return;
        }
        rdbt$coffinStatus = new SleepStatus();
        rdbt$coffinStatus.update(coffinSleepers);

        long dayTime = level.getDayTime() % 24000L;
        boolean isThundering = level.isThundering();

        coffinSleepDelayTicks++;
        if (coffinSleepDelayTicks < TIMER_CAP) return;

        int $$2 = this.getGameRules().getInt(GameRules.RULE_PLAYERS_SLEEPING_PERCENTAGE);
        if (dayTime < 13000L) {
            if (this.rdbt$coffinStatus.areEnoughSleeping($$2) && this.rdbt$coffinStatus.areEnoughDeepSleeping($$2, this.players)) {
                if (this.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
                    long timeToNight = 13000L - dayTime;
                    level.setDayTime(level.getDayTime() + timeToNight);
                }

                for (ServerPlayer player : coffinSleepers) {
                    player.stopSleepInBed(true, false);
                }
                if (this.getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE) && this.isRaining()) {
                    this.resetWeatherCycle();
                }
            }
        }

    }
}