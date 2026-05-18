package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.redstone.Redstone;

public class AjaBlock extends AjaOreBlock {

    public static final IntegerProperty POWER =
            BlockStateProperties.POWER;

    public AjaBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(LIT, false)
                        .setValue(POWER, 0)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWER);
    }
    @Override
    public void neighborChanged(BlockState state,
                                Level level,
                                BlockPos pos,
                                Block block,
                                BlockPos fromPos,
                                boolean isMoving) {

        if (!level.isClientSide) {

            updateState(level, pos, state);

            // schedule a recheck next tick
            level.scheduleTick(pos, this, 1);
        }

        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }
    private void updateState(Level level, BlockPos pos, BlockState state) {

        int power = getNeighborLight(level, pos);

        if (state.getValue(POWER) != power) {

            BlockState newState = state
                    .setValue(POWER, power);

            level.setBlock(pos, newState, Block.UPDATE_CLIENTS);

            level.updateNeighborsAt(pos, this);

            for (Direction dir : Direction.values()) {
                level.updateNeighborsAt(pos.relative(dir), this);
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        int power = getNeighborLight(level, pos);

        return this.defaultBlockState()
                .setValue(LIT, power > 0)
                .setValue(POWER, power);
    }

    public int getNeighborLight(Level level, BlockPos pos) {

        int strongest = 0;

        for (Direction direction : Direction.values()) {

            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = level.getBlockState(neighborPos);

            // Ignore other AjaBlocks
            if (neighborState.getBlock() instanceof AjaBlock) {
                continue;
            }

            // Ignore redstone wire
            if (neighborState.is(Blocks.REDSTONE_WIRE)) {
                continue;
            }

            int light = neighborState.getLightEmission();

            if (light > strongest) {
                strongest = light;
            }
        }

        return strongest;
    }

    // =========================
    // REDSTONE OUTPUT
    // =========================

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state,
                         BlockGetter level,
                         BlockPos pos,
                         Direction direction) {

        return state.getValue(POWER);
    }

    @Override
    public int getDirectSignal(BlockState state,
                               BlockGetter level,
                               BlockPos pos,
                               Direction direction) {

        return state.getValue(POWER);
    }

    @Override
    public boolean hasLitNeighbor(Level level, BlockPos pos) {

        for (Direction direction : Direction.values()) {

            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = level.getBlockState(neighborPos);

            // Check if neighbor has a LIT property
            if (!(neighborState.getBlock() instanceof AjaOreBlock) &&
                    !(neighborState.is(Blocks.REDSTONE_WIRE)) &&
                    neighborState.hasProperty(BlockStateProperties.LIT)) {

                if (neighborState.getValue(BlockStateProperties.LIT)) {
                    return true;
                }
            }
        }

        return false;
    }
}