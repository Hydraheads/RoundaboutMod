package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GasolineBlock extends Block {
        public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;
        protected static final VoxelShape SHAPE = Block.box(0.0, 0.001, 0.0, 16.0, 0.1, 16.0);

        public GasolineBlock(BlockBehaviour.Properties $$0) {
            super($$0);
            this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, Integer.valueOf(0)));
        }
        public GasolineBlock(BlockBehaviour.Properties $$0, int stage) {
            super($$0);
            this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, Integer.valueOf(stage)));
        }

        @SuppressWarnings("deprecation")
        @Override
        public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
            return SHAPE;
        }


    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState $$0, Direction $$1, BlockState $$2, LevelAccessor $$3, BlockPos $$4, BlockPos $$5) {
        return !$$0.canSurvive($$3, $$4) ? Blocks.AIR.defaultBlockState() : super.updateShape($$0, $$1, $$2, $$3, $$4, $$5);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canSurvive(BlockState $$0, LevelReader $$1, BlockPos $$2) {
        return !$$1.isEmptyBlock($$2.below());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(LEVEL);
    }
}
