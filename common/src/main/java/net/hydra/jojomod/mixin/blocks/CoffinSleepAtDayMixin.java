package net.hydra.jojomod.mixin.blocks;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Unit;
import net.hydra.jojomod.block.CoffinBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class CoffinSleepAtDayMixin {

    @Inject(method = "startSleepInBed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isDay()Z"), cancellable = true,  require = 0)
    private void allowCoffinSleepAtDay(BlockPos pos, CallbackInfoReturnable<Either<Player.BedSleepingProblem, Unit>> cir) {
        ServerPlayer player = (ServerPlayer)(Object)this;

        if (player.level().getBlockState(pos).getBlock() instanceof CoffinBlock) {
            if (player.level().isDay() || player.level().isThundering()) {
                player.startSleeping(pos);
                cir.setReturnValue(Either.right(Unit.INSTANCE));
            } else {
                cir.setReturnValue(Either.left(Player.BedSleepingProblem.NOT_POSSIBLE_HERE));
            }
            cir.cancel();
        }
    }
}

