package net.hydra.jojomod.block;


import net.hydra.jojomod.Roundabout;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Objects;

public class ClimbingWireBlock extends HorizontalDirectionalBlock {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    protected static final VoxelShape EAST_OPEN_AABB = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
    protected static final VoxelShape WEST_OPEN_AABB = Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape SOUTH_OPEN_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
    protected static final VoxelShape NORTH_OPEN_AABB = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape TOP_AABB = Block.box(0.0, 15.0F, 0.0, 16.0, 16.0, 16.0);


    protected ClimbingWireBlock(Properties $$0) {
        super($$0);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, false));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        if (!blockState.getValue(OPEN).booleanValue()) {
            return TOP_AABB;
        }
        switch (blockState.getValue(FACING)) {
            default: {
                return NORTH_OPEN_AABB;
            }
            case SOUTH: {
                return SOUTH_OPEN_AABB;
            }
            case WEST: {
                return WEST_OPEN_AABB;
            }
            case EAST:
        }
        return EAST_OPEN_AABB;
    }

    public static final int DISTANCE = 7;

    private boolean shouldStay(BlockState $$0, LevelReader $$1, BlockPos $$2) {
        Direction dir = $$0.getValue(FACING);
        Direction opp = dir.getOpposite();
        if ($$0.getValue(OPEN)) {
            for(int i=0;i<DISTANCE;i++) {
                BlockPos offset = $$2.relative(Direction.UP,i);
                BlockState state = $$1.getBlockState(offset);

                BlockState behindState = $$1.getBlockState(offset.relative(opp));
                if (behindState.isSolid()) return true;

                if ( !state.is($$0.getBlock()) || !state.getValue(FACING).equals(dir) ) break;
            }
            return false;
        } else {
            boolean A = false;
            boolean B = false;
            for(int i=1;i<=DISTANCE;i++) {
                BlockPos offsetA = $$2.relative(dir,i);
                BlockState stateA = $$1.getBlockState(offsetA);
                if (!stateA.is($$0.getBlock()) || !stateA.getValue(FACING).equals(dir)) {
                    A = stateA.isSolid();
                    break;
                }
            }
            for(int i=1;i<=DISTANCE;i++) {
                BlockPos offsetB = $$2.relative(opp,i);
                BlockState stateB = $$1.getBlockState(offsetB);
                if (!stateB.is($$0.getBlock()) || !stateB.getValue(FACING).equals(dir)) {
                    B = stateB.isSolid();
                    break;
                }
            }
            return A && B;
        }
    }

    @Override
    public BlockState updateShape(BlockState $$0, Direction $$1, BlockState $$2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos $$5) {
        if (!levelAccessor.isClientSide()) {
            levelAccessor.scheduleTick(blockPos, this, 1);
        }

        return super.updateShape($$0, $$1, $$2, levelAccessor, blockPos, $$5);
    }

    @Override
    public void tick(BlockState $$0, ServerLevel $$1, BlockPos $$2, RandomSource $$3) {
        super.tick($$0, $$1, $$2, $$3);
        if (!shouldStay($$0,$$1,$$2)) {
            $$1.destroyBlock($$2, true);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState = this.defaultBlockState();
        Direction direction = blockPlaceContext.getClickedFace();
        if (direction.equals(Direction.DOWN)) {
            blockState.setValue(OPEN,true);
        }
        return blockPlaceContext.replacingClickedOnBlock() || !direction.getAxis().isHorizontal()
                ? blockState.setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite())
                : blockState.setValue(FACING, direction);
        }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        if (Objects.requireNonNull(pathComputationType) == PathComputationType.LAND) {
            return blockState.getValue(OPEN);
        }
        return false;
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
    }
}
