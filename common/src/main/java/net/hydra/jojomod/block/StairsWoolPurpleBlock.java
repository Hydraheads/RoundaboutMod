package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StairsWoolPurpleBlock extends Block {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.box(0.0, 0.0, 8.0, 16.0, 16.0, 16.0)
    );

    private static final VoxelShape NORTH_SHAPE = SHAPE;
    private static final VoxelShape EAST_SHAPE = rotateShape(SHAPE, Direction.EAST);
    private static final VoxelShape SOUTH_SHAPE = rotateShape(SHAPE, Direction.SOUTH);
    private static final VoxelShape WEST_SHAPE = rotateShape(SHAPE, Direction.WEST);

    public StairsWoolPurpleBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean skipRendering(BlockState p_53972_, BlockState p_53973_, Direction p_53974_) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOcclusionShape(BlockState $$0, BlockGetter $$1, BlockPos $$2) {
        return Shapes.empty();
    }

    private static VoxelShape rotateShape(VoxelShape shape, Direction to) {
        VoxelShape rotated = shape;
        int times = switch (to) {
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> 0;
        };

        for (int i = 0; i < times; i++) {
            VoxelShape next = Shapes.empty();
            for (AABB box : rotated.toAabbs()) {
                double minX = box.minX, minY = box.minY, minZ = box.minZ;
                double maxX = box.maxX, maxY = box.maxY, maxZ = box.maxZ;
                double nMinX = 1.0 - maxZ;
                double nMinZ = minX;
                double nMaxX = 1.0 - minZ;
                double nMaxZ = maxX;

                next = Shapes.or(next, Block.box(
                        nMinX * 16.0, minY * 16.0, nMinZ * 16.0,
                        nMaxX * 16.0, maxY * 16.0, nMaxZ * 16.0
                ));
            }
            rotated = next.optimize();
        }

        return rotated;
    }
}
