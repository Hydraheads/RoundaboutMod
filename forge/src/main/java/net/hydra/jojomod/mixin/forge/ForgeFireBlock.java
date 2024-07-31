package net.hydra.jojomod.mixin.forge;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.GasolineBlock;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireBlock.class)
public class ForgeFireBlock {
    @Inject(method = "tryCatchFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 1, shift = At.Shift.AFTER), cancellable = true)
    public void roundabout$checkBurnOut(Level $$0, BlockPos $$1, int p_53434_, RandomSource p_53435_, int p_53436_, Direction face, CallbackInfo ci) {
        BlockState blkSt = $$0.getBlockState($$1);
        Block blk = blkSt.getBlock();
        if (blk instanceof GasolineBlock){
            MainUtil.gasExplode(blkSt, (ServerLevel) $$0, $$1, 0, 2, 4, Roundabout.gasDamage*10);
            ci.cancel();
        }
    }
}