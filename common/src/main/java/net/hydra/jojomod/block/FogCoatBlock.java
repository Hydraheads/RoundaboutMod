package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FogCoatBlock extends FogBlock{
    protected static final VoxelShape EAST_AABB = Block.box(0.0, 0.0, 0.0, 0.01, 16.0, 16.0);
    protected static final VoxelShape WEST_AABB = Block.box(15.99, 0.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape SOUTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 0.01);
    protected static final VoxelShape NORTH_AABB = Block.box(0.0, 0.0, 15.99, 16.0, 16.0, 16.0);
    protected static final VoxelShape TOP_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 0.01, 16.0);
    protected static final VoxelShape BOTTOM_AABB = Block.box(0.0, 15.99, 0.0, 16.0, 16.0, 16.0);
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public FogCoatBlock(Properties $$0) {
        super($$0,true);
        this.registerDefaultState(this.stateDefinition.any().setValue(IN_FOG, Boolean.valueOf(true)).setValue(FACING, Direction.DOWN)
        );
    }


    @Override
    public boolean isPathfindable(BlockState $$0, BlockGetter $$1, BlockPos $$2, PathComputationType $$3) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
            switch ((Direction)$$0.getValue(FACING)) {
                case NORTH:
                default:
                    return NORTH_AABB;
                case SOUTH:
                    return SOUTH_AABB;
                case WEST:
                    return WEST_AABB;
                case EAST:
                    return EAST_AABB;
                case UP:
                    return TOP_AABB;
                case DOWN:
                    return BOTTOM_AABB;
            }
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext $$0) {
        BlockPos blockpos = $$0.getClickedPos();
        Direction[] adirection = $$0.getNearestLookingDirections();

        for(Direction direction : adirection) {
                Direction direction1 = direction.getOpposite();
                BlockState blockstate = this.defaultBlockState().setValue(FACING, direction1);
                LevelReader levelreader = $$0.getLevel();
                return blockstate;
        }
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        super.createBlockStateDefinition($$0);
        $$0.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState $$0, Rotation $$1) {
        return $$0.setValue(FACING, $$1.rotate($$0.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState $$0, Mirror $$1) {
        return $$0.rotate($$1.getRotation($$0.getValue(FACING)));
    }
}
