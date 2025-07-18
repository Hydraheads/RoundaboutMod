package net.hydra.jojomod.mixin;

import net.hydra.jojomod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BushBlock.class)
public class ZBushBlock {

    /**blocks we don't want breaking grass*/
    @Inject(method = "mayPlaceOn(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z", at = @At("HEAD"), cancellable = true)
    public void roundabout$mayPlaceOn(BlockState $$0, BlockGetter $$1, BlockPos $$2, CallbackInfoReturnable<Boolean> cir) {
        if ($$0.is(ModBlocks.INVISIBLOCK)){
            cir.setReturnValue(true);
        }
    }
}
