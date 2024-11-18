package net.hydra.jojomod.mixin.fabric;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.GasolineBlock;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
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
public class FabricFireBlock {
    @Inject(method = "checkBurnOut", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 1, shift = At.Shift.AFTER), cancellable = true)
    public void roundabout$checkBurnOut(Level $$0, BlockPos $$1, int $$2, RandomSource $$3, int $$4, CallbackInfo ci) {
        BlockState blkSt = $$0.getBlockState($$1);
        Block blk = blkSt.getBlock();
        if (blk instanceof GasolineBlock){
            MainUtil.gasExplode(blkSt, (ServerLevel) $$0, $$1, 0, 2, 4, MainUtil.gasDamageMultiplier()*14);
            ci.cancel();
        }
    }
}
