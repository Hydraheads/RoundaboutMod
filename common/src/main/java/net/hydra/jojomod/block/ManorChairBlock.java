package net.hydra.jojomod.block;

import net.hydra.jojomod.access.IPlayerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ManorChairBlock extends Block {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 0, 0, 1, 11, 2),       // leg front-left
            Block.box(15, 16, 0, 16, 17, 14),   // top rail right
            Block.box(15, 13, 1, 16, 16, 2),    // small vertical bit
            Block.box(0, 16, 0, 1, 17, 14),     // top rail left
            Block.box(0, 13, 1, 1, 16, 2),      // small vertical bit left
            Block.box(0, 11, 0, 16, 13, 16),    // seat
            Block.box(4, 30, 14, 12, 31, 16),   // backrest slat
            Block.box(14, 30, 14, 16, 32, 16),  // back post right top
            Block.box(0, 30, 14, 2, 32, 16),    // back post left top
            Block.box(14, 20, 14, 16, 22, 16),  // mid back post right
            Block.box(0, 20, 14, 2, 22, 16),    // mid back post left
            Block.box(14, 22, 14, 16, 30, 16),  // long back post right
            Block.box(0, 22, 14, 2, 30, 16),    // long back post left
            Block.box(14, 13, 14, 16, 20, 16),  // back post bottom right
            Block.box(0, 13, 14, 2, 20, 16),    // back post bottom left
            Block.box(2, 13, 14, 14, 30, 16),   // big backboard
            Block.box(15, 0, 14, 16, 11, 16),   // back leg right
            Block.box(14, 0, 0, 15, 11, 1),     // front-right leg
            Block.box(15, 0, 0, 16, 11, 2),     // front-right leg adjacent
            Block.box(0, 0, 14, 1, 11, 16),     // back leg left
            Block.box(14, 0, 15, 15, 11, 16),   // inner back leg right
            Block.box(1, 0, 15, 2, 11, 16),     // inner back leg left
            Block.box(1, 0, 0, 2, 11, 1)
    );

    private static final VoxelShape NORTH_SHAPE = SHAPE;
    private static final VoxelShape EAST_SHAPE = rotateShape(SHAPE, Direction.EAST);
    private static final VoxelShape SOUTH_SHAPE = rotateShape(SHAPE, Direction.SOUTH);
    private static final VoxelShape WEST_SHAPE = rotateShape(SHAPE, Direction.WEST);

    public ManorChairBlock(Properties properties) {
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

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 1.0;
            double z = pos.getZ() + 0.5;

            float yaw = state.getValue(FACING).toYRot();
            BlockPos blockAbove = pos.above();
            if (level.getBlockState(blockAbove).isAir()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.teleport(x, y, z, yaw, serverPlayer.getXRot());
                    serverPlayer.setYHeadRot(yaw);
                    serverPlayer.setYBodyRot(yaw);
                    IPlayerEntity ipe = ((IPlayerEntity) player);
                    ipe.roundabout$SetPoseEmote((byte) 11);
                } else {
                    player.teleportTo(x, y, z);
                    player.setYRot(yaw);
                    player.setYHeadRot(yaw);
                    player.setYBodyRot(yaw);
                }
            }
        }
        return InteractionResult.FAIL;
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
