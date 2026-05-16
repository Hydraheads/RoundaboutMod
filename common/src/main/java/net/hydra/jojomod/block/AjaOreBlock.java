package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class AjaOreBlock extends DropExperienceBlock {
    public static final BooleanProperty LIT = ModBlocks.LIT;
    public AjaOreBlock(Properties $$0) {
        super($$0);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.valueOf(false))
        );
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(LIT);
    }


    @Override
    public void neighborChanged(BlockState state,
                                Level level,
                                BlockPos pos,
                                Block block,
                                BlockPos fromPos,
                                boolean isMoving) {

        if (!level.isClientSide) {
            boolean shouldBeLit = hasLitNeighbor(level, pos);

            if (state.getValue(LIT) != shouldBeLit) {
                level.setBlock(pos, state.setValue(LIT, shouldBeLit), 3);
            }
        }

        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        return this.defaultBlockState()
                .setValue(LIT, hasLitNeighbor(level, pos));
    }

    private boolean hasLitNeighbor(Level level, BlockPos pos) {

        for (Direction direction : Direction.values()) {

            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = level.getBlockState(neighborPos);

            // Check if neighbor has a LIT property
            if (neighborState.hasProperty(BlockStateProperties.LIT)) {

                if (neighborState.getValue(BlockStateProperties.LIT)) {
                    return true;
                }
            }
        }

        return false;
    }
}
