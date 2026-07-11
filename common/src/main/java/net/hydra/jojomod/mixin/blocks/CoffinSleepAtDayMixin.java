package net.hydra.jojomod.mixin.blocks;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Unit;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.CoffinBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(value = ServerPlayer.class, priority = 99)
public abstract class CoffinSleepAtDayMixin extends Player {

    public CoffinSleepAtDayMixin(Level $$0, BlockPos $$1, float $$2, GameProfile $$3) {
        super($$0, $$1, $$2, $$3);
    }

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

    @Inject(method = "setRespawnPosition", at = @At(value = "HEAD"), cancellable = true,  require = 0)
    public void rdbt$setRespawnPosition(ResourceKey<Level> $$0, BlockPos $$1, float $$2, boolean $$3, boolean $$4, CallbackInfo ci) {
        if (this.level().getBlockState($$1).is(ModBlocks.KING_BED_BLOCK)){
            ci.cancel();
            Roundabout.LOGGER.info("1");
        }
    }
}

