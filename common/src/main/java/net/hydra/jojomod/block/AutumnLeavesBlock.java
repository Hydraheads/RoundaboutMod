
package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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

    public AutumnLeavesBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(
                FACING,
                context.getHorizontalDirection()
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level,
                               BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void entityInside(BlockState state, Level level,
                             BlockPos pos, Entity entity) {

        if (level.isClientSide) {
            return;
        }

        Direction direction = state.getValue(FACING);

        double startX = entity.getX();
        double startY = entity.getY();
        double startZ = entity.getZ();

        double lastSafeX = startX;
        double lastSafeZ = startZ;

        double step = 0.25;

        for (double distance = step;
             distance <= TRAVEL_DISTANCE;
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

            lastSafeX = nextX;
            lastSafeZ = nextZ;
        }

        if (lastSafeX != startX || lastSafeZ != startZ) {
            entity.teleportTo(lastSafeX, startY, lastSafeZ);
        }
    }
}
