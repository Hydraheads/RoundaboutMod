package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Optional;

public class PoisonTrailMushroomBlock extends BushBlock {

    protected static final float AABB_OFFSET = 3.0F;
    protected static final VoxelShape SHAPE = Block.box((double)5.0F, (double)0.0F, (double)5.0F, (double)11.0F, (double)6.0F, (double)11.0F);

    public PoisonTrailMushroomBlock(BlockBehaviour.Properties $$0) {
        super($$0);
    }

    @Override
    public void tick(BlockState $$0, ServerLevel $$1, BlockPos $$2, RandomSource $$3) {
        $$1.removeBlock($$2, false);
    }

    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return SHAPE;
    }

    public void randomTick(BlockState $$0, ServerLevel $$1, BlockPos $$2, RandomSource $$3) {
        if ($$3.nextInt(25) == 0) {
            int $$4 = 5;
            int $$5 = 4;

            for(BlockPos $$6 : BlockPos.betweenClosed($$2.offset(-4, -1, -4), $$2.offset(4, 1, 4))) {
                if ($$1.getBlockState($$6).is(this)) {
                    --$$4;
                    if ($$4 <= 0) {
                        return;
                    }
                }
            }

            BlockPos $$7 = $$2.offset($$3.nextInt(3) - 1, $$3.nextInt(2) - $$3.nextInt(2), $$3.nextInt(3) - 1);

            for(int $$8 = 0; $$8 < 4; ++$$8) {
                if ($$1.isEmptyBlock($$7) && $$0.canSurvive($$1, $$7)) {
                    $$2 = $$7;
                }

                $$7 = $$2.offset($$3.nextInt(3) - 1, $$3.nextInt(2) - $$3.nextInt(2), $$3.nextInt(3) - 1);
            }

            if ($$1.isEmptyBlock($$7) && $$0.canSurvive($$1, $$7)) {
                $$1.setBlock($$7, $$0, 2);
            }
        }

    }

    protected boolean mayPlaceOn(BlockState $$0, BlockGetter $$1, BlockPos $$2) {
        return $$0.isSolidRender($$1, $$2);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canSurvive(BlockState $$0, LevelReader $$1, BlockPos $$2) {
        BlockPos $$3 = $$2.below();
        return $$1.getBlockState($$3).isFaceSturdy($$1, $$3, Direction.UP);
    }
}
