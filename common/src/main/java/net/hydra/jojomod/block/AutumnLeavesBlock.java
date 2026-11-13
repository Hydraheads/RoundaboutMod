
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

        level.setBlock(pos, state.setValue(COOLDOWN, true), 3);
        level.scheduleTick(pos, this, COOLDOWN_TICKS);

        Direction direction = state.getValue(FACING);

        double startX = entity.getX();
        double startY = entity.getY();
        double startZ = entity.getZ();

        double currentY = startY;

        double lastSafeX = startX;
        double lastSafeY = startY;
        double lastSafeZ = startZ;

        boolean shouldFall = false;

        for (int distance = 1; distance <= TRAVEL_DISTANCE; distance++) {

            double nextX = startX + direction.getStepX() * distance;
            double nextZ = startZ + direction.getStepZ() * distance;

            boolean foundValidPosition = false;
            double nextY = currentY;

            double[] heightOffsets = {0.0, 1.0, -1.0};

            for (double offset : heightOffsets) {

                double candidateY = currentY + offset;

                AABB candidateBox = entity.getBoundingBox().move(
                        nextX - startX,
                        candidateY - startY,
                        nextZ - startZ
                );

                if (!level.noCollision(entity, candidateBox)) {
                    continue;
                }

                AABB floorBox = candidateBox.move(0, -0.1, 0);

                boolean hasGround = !level.noCollision(entity, floorBox);

                BlockPos feetPos = BlockPos.containing(
                        nextX,
                        candidateY + 0.01,
                        nextZ
                );

                boolean isLeaves = level.getBlockState(feetPos)
                        .getBlock() instanceof AutumnLeavesBlock;

                if (hasGround || isLeaves) {
                    nextY = candidateY;
                    foundValidPosition = true;
                    break;
                }
            }

            if (!foundValidPosition) {

                AABB forwardBox = entity.getBoundingBox().move(
                        nextX - startX,
                        currentY - startY,
                        nextZ - startZ
                );

                if (level.noCollision(entity, forwardBox)) {
                    lastSafeX = nextX;
                    lastSafeY = currentY;
                    lastSafeZ = nextZ;
                    shouldFall = true;
                }

                break;
            }

            currentY = nextY;

            BlockPos leavesPos = BlockPos.containing(
                    nextX,
                    currentY + 0.01,
                    nextZ
            );

            if (!leavesPos.equals(pos)
                    && level.getBlockState(leavesPos)
                    .getBlock() instanceof AutumnLeavesBlock) {

                lastSafeX = leavesPos.getX() + 0.5;
                lastSafeY = currentY;
                lastSafeZ = leavesPos.getZ() + 0.5;

                break;
            }

            lastSafeX = nextX;
            lastSafeY = currentY;
            lastSafeZ = nextZ;
        }

        if (lastSafeX != startX
                || lastSafeY != startY
                || lastSafeZ != startZ) {

            entity.teleportTo(
                    lastSafeX,
                    lastSafeY,
                    lastSafeZ
            );

            if (shouldFall) {
                entity.setDeltaMovement(
                        entity.getDeltaMovement().x,
                        -0.1,
                        entity.getDeltaMovement().z
                );

                entity.setOnGround(false);
                entity.hurtMarked = true;
            }
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
