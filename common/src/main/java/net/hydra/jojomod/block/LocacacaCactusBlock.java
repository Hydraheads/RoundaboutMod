package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockState;

public class LocacacaCactusBlock extends CactusBlock {
    protected LocacacaCactusBlock(Properties $$0) {
        super($$0);
    }
    @Override
    public void entityInside(BlockState $$0, Level $$1, BlockPos $$2, Entity $$3) {
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canSurvive(BlockState $$0, LevelReader $$1, BlockPos $$2) {
        for (Direction $$3 : Direction.Plane.HORIZONTAL) {
            BlockState $$4 = $$1.getBlockState($$2.relative($$3));
            if ($$4.isSolid() || $$1.getFluidState($$2.relative($$3)).is(FluidTags.LAVA)) {
                return false;
            }
        }

        BlockState $$5 = $$1.getBlockState($$2.below());
        return ($$5.is(ModBlocks.LOCACACA_CACTUS) || $$5.is(BlockTags.SAND)) && !$$1.getBlockState($$2.above()).liquid();
    }
    /**Locacaca Cactus doesnt grow*/
    @Override
    public void randomTick(BlockState $$0, ServerLevel $$1, BlockPos $$2, RandomSource $$3) {
    }
}
