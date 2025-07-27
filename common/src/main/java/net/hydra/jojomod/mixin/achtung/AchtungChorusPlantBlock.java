package net.hydra.jojomod.mixin.achtung;

import net.hydra.jojomod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChorusPlantBlock.class)
public class AchtungChorusPlantBlock {

    /**The invisible blocks of achtung baby should not be breaking chorus plants*/
    @Inject(method = "canSurvive(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z", at = @At("HEAD"), cancellable = true)
    public void roundabout$canSurvive(BlockState $$0, LevelReader $$1, BlockPos $$2, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockstate = $$1.getBlockState($$2.below());
        if (blockstate.is(ModBlocks.INVISIBLOCK)){
            cir.setReturnValue(true);
        }
    }
}
