package net.hydra.jojomod.block;

import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ManorChairBlock extends Block implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape LOWER_BASE = Shapes.or(
            Block.box(14,      0,   0, 16, 10,  2), // leg right bottom
            Block.box(14,     12,   0, 16, 16,  2), // leg right top
            Block.box( 0,      0,   0,  2, 10,  2), // leg left bottom
            Block.box( 0,     12,   0,  2, 16,  2), // leg left top
            Block.box( 0,  13.95,1.875,  2,15.95,13.875), // left side rail
            Block.box( 0,     12,  14, 16, 16, 16), // back rail
            Block.box( 0,     10,   0, 16, 12, 16), // seat support
            Block.box(14.025, 14,1.975, 16, 16,13.975),  // right side rail
            Block.box( 0,      0,  14,  2, 10, 16), // back-left leg bottom
            Block.box(14,      0,  14, 16, 10, 16) // back-right leg bottom
    );

    private static final VoxelShape UPPER_BASE =
            Block.box(0, 0, 14, 16, 14, 16);

    private static final VoxelShape LOWER_N = LOWER_BASE;
    private static final VoxelShape LOWER_E = rotateShape(LOWER_BASE, Direction.EAST);
    private static final VoxelShape LOWER_S = rotateShape(LOWER_BASE, Direction.SOUTH);
    private static final VoxelShape LOWER_W = rotateShape(LOWER_BASE, Direction.WEST);

    private static final VoxelShape UPPER_N = UPPER_BASE;
    private static final VoxelShape UPPER_E = rotateShape(UPPER_BASE, Direction.EAST);
    private static final VoxelShape UPPER_S = rotateShape(UPPER_BASE, Direction.SOUTH);
    private static final VoxelShape UPPER_W = rotateShape(UPPER_BASE, Direction.WEST);

    public ManorChairBlock(Properties properties) {
        super(properties.pushReaction(PushReaction.BLOCK));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(HALF, DoubleBlockHalf.LOWER).setValue(WATERLOGGED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        if (pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(pos.above()).canBeReplaced(context)) {
            FluidState fluid = level.getFluidState(pos);
            return this.defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(HALF, DoubleBlockHalf.LOWER)
                    .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
        }
        return null;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            BlockPos above = pos.above();
            if (level.getBlockState(above).canBeReplaced()) {
                FluidState fluid = level.getFluidState(above);
                level.setBlock(above, state.setValue(HALF, DoubleBlockHalf.UPPER).setValue(WATERLOGGED, fluid.getType() == Fluids.WATER), 3);
            }
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        DoubleBlockHalf half = state.getValue(HALF);
        BlockPos otherPos = (half == DoubleBlockHalf.LOWER) ? pos.above() : pos.below();
        BlockState otherState = level.getBlockState(otherPos);

        if (otherState.is(this) && otherState.getValue(HALF) != half) {
            level.destroyBlock(otherPos, !player.isCreative());
        }

        super.playerWillDestroy(level, pos, state, player);
    }



    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        Direction f = state.getValue(FACING);
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return switch (f) {
                case NORTH -> LOWER_N;
                case EAST -> LOWER_E;
                case SOUTH -> LOWER_S;
                case WEST -> LOWER_W;
                default -> LOWER_N;
            };
        } else {
            return switch (f) {
                case NORTH -> UPPER_N;
                case EAST -> UPPER_E;
                case SOUTH -> UPPER_S;
                case WEST -> UPPER_W;
                default -> UPPER_N;
            };
        }
    }


    @SuppressWarnings("deprecation")
    @Override
    public boolean skipRendering(BlockState state, BlockState neighbor, Direction side) {
        if (neighbor.getBlock() == this && state.getValue(FACING) == neighbor.getValue(FACING)) {
            if (state.getValue(HALF) == DoubleBlockHalf.LOWER
                    && neighbor.getValue(HALF) == DoubleBlockHalf.UPPER
                    && side == Direction.UP) return true;
            if (state.getValue(HALF) == DoubleBlockHalf.UPPER
                    && neighbor.getValue(HALF) == DoubleBlockHalf.LOWER
                    && side == Direction.DOWN) return true;
        }
        return super.skipRendering(state, neighbor, side);
    }


    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockPos targetPos = pos;
        BlockState targetState = state;

        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            targetPos = pos.below();
            targetState = level.getBlockState(targetPos);

            if (!(targetState.getBlock() == state.getBlock() &&
                    targetState.getValue(HALF) == DoubleBlockHalf.LOWER)) {
                return InteractionResult.PASS;
            }
        }

        if (((IGravityEntity) player).roundabout$getGravityDirection() != Direction.DOWN) {
            return InteractionResult.PASS;
        }

        if (player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        if (hit.getDirection() == Direction.DOWN) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 1.0;
            double z = pos.getZ() + 0.5;

            float yaw = state.getValue(FACING).toYRot();

            if (player instanceof ServerPlayer sp) {
                if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
                    y -= 1.0;
                }

                sp.connection.teleport(x, y, z, yaw, sp.getXRot());
                sp.setYHeadRot(yaw);
                sp.setYBodyRot(yaw);
                IPlayerEntity ipe = (IPlayerEntity) player;
                ipe.roundabout$SetPoseEmote((byte) 11);
            } else {
                if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
                    y -= 1.0;
                }

                player.teleportTo(x, y, z);
                player.setYRot(yaw);
                player.setYHeadRot(yaw);
                player.setYBodyRot(yaw);
            }
        }
        return InteractionResult.CONSUME;
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

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        DoubleBlockHalf half = state.getValue(HALF);
        Direction facing = state.getValue(FACING);

        if (half == DoubleBlockHalf.UPPER) {
            BlockState below = level.getBlockState(pos.below());
            if (!(below.is(this) && below.getValue(HALF) == DoubleBlockHalf.LOWER && below.getValue(FACING) == facing)) {
                return Blocks.AIR.defaultBlockState();
            }
        } else {
            BlockState above = level.getBlockState(pos.above());
            if (!(above.is(this) && above.getValue(HALF) == DoubleBlockHalf.UPPER && above.getValue(FACING) == facing)) {
                return Blocks.AIR.defaultBlockState();
            }
        }
        return super.updateShape(state, dir, neighbor, level, pos, neighborPos);
    }
}
