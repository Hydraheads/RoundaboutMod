
package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AutumnLeavesBlock extends BushBlock {

    public static final DirectionProperty FACING =
            BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE =
            box(0, 0, 0, 16, 0.5, 16);

    private static final int TRAVEL_DISTANCE = 12;

    public static final BooleanProperty COOLDOWN =
            BooleanProperty.create("cooldown");

    private static final int COOLDOWN_TICKS = 60;
    public AutumnLeavesBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(COOLDOWN, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, COOLDOWN);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(COOLDOWN, true);
    }
    @Override
    public void onPlace(BlockState state, Level level,
                        BlockPos pos, BlockState oldState,
                        boolean isMoving) {

        super.onPlace(state, level, pos, oldState, isMoving);

        if (!level.isClientSide
                && !oldState.is(this)
                && state.getValue(COOLDOWN)) {

            level.scheduleTick(pos, this, COOLDOWN_TICKS);
        }
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level,
                               BlockPos pos, CollisionContext context) {
        return SHAPE;
    }



    @Override
    public void entityInside(BlockState state, Level level,
                             BlockPos pos, Entity entity) {

        if (level.isClientSide || state.getValue(COOLDOWN)) {
            return;
        }

        level.setBlock(
                pos,
                state.setValue(COOLDOWN, true),
                3
        );

        level.scheduleTick(pos, this, COOLDOWN_TICKS);

        Direction direction = state.getValue(FACING);

        double startX = entity.getX();
        double startY = entity.getY();
        double startZ = entity.getZ();

        double lastSafeX = startX;
        double lastSafeZ = startZ;

        double step = 0.25;

        for (double distance = step;
             distance <= 12.0;
             distance += step) {

            double nextX = startX + direction.getStepX() * distance;
            double nextZ = startZ + direction.getStepZ() * distance;

            AABB nextBox = entity.getBoundingBox().move(
                    nextX - startX,
                    0,
                    nextZ - startZ
            );

            if (!level.noCollision(entity, nextBox)) {
                break;
            }

            BlockPos groundPos = BlockPos.containing(
                    nextX,
                    startY + 0.01,
                    nextZ
            );

            BlockState groundState = level.getBlockState(groundPos);

            if (!groundPos.equals(pos)
                    && groundState.getBlock() instanceof AutumnLeavesBlock) {

                double targetX = groundPos.getX() + 0.5;
                double targetZ = groundPos.getZ() + 0.5;

                AABB targetBox = entity.getBoundingBox().move(
                        targetX - startX,
                        0,
                        targetZ - startZ
                );

                if (level.noCollision(entity, targetBox)) {
                    entity.teleportTo(
                            targetX,
                            startY,
                            targetZ
                    );
                } else {
                    entity.teleportTo(
                            lastSafeX,
                            startY,
                            lastSafeZ
                    );
                }

                return;
            }

            lastSafeX = nextX;
            lastSafeZ = nextZ;
        }

        if (lastSafeX != startX || lastSafeZ != startZ) {
            entity.teleportTo(lastSafeX, startY, lastSafeZ);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level,
                     BlockPos pos, RandomSource random) {

        if (state.getValue(COOLDOWN)) {
            level.setBlock(
                    pos,
                    state.setValue(COOLDOWN, false),
                    3
            );
        }
    }
}
