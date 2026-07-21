package net.hydra.jojomod.mixin.achtung;

import net.hydra.jojomod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BambooSaplingBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SugarCaneBlock.class)
public class AchtungSugarCaneBlock {


    /**The invisible blocks of achtung baby should not be breaking crops*/
    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    public void roundabout$canSurvive(BlockState $$0, LevelReader $$1, BlockPos $$2, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockstate = $$1.getBlockState($$2.below());
        if (blockstate.is(ModBlocks.INVISIBLOCK)){
            cir.setReturnValue(true);
            return;
        }

        BlockState $$3 = $$1.getBlockState($$2.below());
        if ($$3.is((SugarCaneBlock)(Object)(this))) {
            cir.setReturnValue(true);
            return;
        } else {
            if ($$3.is(BlockTags.DIRT) || $$3.is(BlockTags.SAND)) {
                BlockPos $$4 = $$2.below();

                for(Direction $$5 : Direction.Plane.HORIZONTAL) {
                    BlockState $$6 = $$1.getBlockState($$4.relative($$5));
                    if ($$6.is(ModBlocks.WHITE_ALBUM_ICE_BLOCK)) {
                        cir.setReturnValue(true);
                        return;
                    }
                }
            }
        }
    }
}
