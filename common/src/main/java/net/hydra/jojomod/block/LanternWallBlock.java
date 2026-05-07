package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LanternWallBlock extends Block {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(7.0, 3.0, 15.0, 9.0, 9.0, 16.0),
            Block.box(7.5, 4.375, 13.01, 8.5, 9.625, 14.01),
            Block.box(6.45, 9.65, 12.0, 9.45, 12.65, 15.0)
    );

    private static final VoxelShape SOUTH_SHAPE = SHAPE;
    private static final VoxelShape WEST_SHAPE = rotateShape(SHAPE, Direction.EAST);
    private static final VoxelShape NORTH_SHAPE = rotateShape(SHAPE, Direction.SOUTH);
    private static final VoxelShape EAST_SHAPE = rotateShape(SHAPE, Direction.WEST);

    public LanternWallBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public boolean canSurvive(BlockState $$0, LevelReader $$1, BlockPos $$2) {
        Direction $$3 = (Direction)$$0.getValue(FACING).getOpposite();
        BlockPos $$4 = $$2.relative($$3.getOpposite());
        BlockState $$5 = $$1.getBlockState($$4);
        return $$5.isFaceSturdy($$1, $$4, $$3);
    }

    public BlockState updateShape(BlockState $$0, Direction $$1, BlockState $$2, LevelAccessor $$3, BlockPos $$4, BlockPos $$5) {
        return $$1.getOpposite() == $$0.getValue(FACING).getOpposite() && !$$0.canSurvive($$3, $$4) ? Blocks.AIR.defaultBlockState() : $$0;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Direction dir = state.getValue(FACING);

        double baseY = 0.7;
        double offsetX = 0;
        double offsetZ = 0;

        switch (dir) {
            case NORTH -> {
                offsetX = 0.5;
                offsetZ = 0.15;
            }
            case SOUTH -> {
                offsetX = 0.5;
                offsetZ = 0.85;
            }
            case EAST -> {
                offsetX = 0.85;
                offsetZ = 0.5;
            }
            case WEST -> {
                offsetX = 0.15;
                offsetZ = 0.5;
            }
        }

        double x = pos.getX() + offsetX;
        double y = pos.getY() + baseY;
        double z = pos.getZ() + offsetZ;

        level.addParticle(ParticleTypes.SMALL_FLAME, x, y, z, 0, 0, 0);
    }



    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clickedFace = context.getClickedFace();
        if (clickedFace.getAxis().isHorizontal()) {
            return this.defaultBlockState().setValue(FACING, clickedFace.getOpposite());
        }
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
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
