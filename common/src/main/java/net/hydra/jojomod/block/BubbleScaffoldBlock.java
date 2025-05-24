package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BubbleScaffoldBlock extends BaseEntityBlock {

    protected BubbleScaffoldBlock(Properties $$0) {
        super($$0);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos $$0, BlockState $$1) {
        return new BubbleScaffoldBlockEntity($$0, $$1);
    }
}
