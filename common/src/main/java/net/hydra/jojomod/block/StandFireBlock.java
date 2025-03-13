package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class StandFireBlock extends BaseEntityBlock {
    @Override
    public void onRemove(BlockState $$0, Level $$1, BlockPos $$2, BlockState $$3, boolean $$4) {
        if (!$$0.is($$3.getBlock())) {
            if ($$1.getBlockEntity($$2) instanceof StandFireBlockEntity $$5) {
            }

            super.onRemove($$0, $$1, $$2, $$3, $$4);
        }
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos $$0, BlockState $$1) {
        return new StandFireBlockEntity($$0, $$1);
    }
    public StandFireBlock(Properties $$0) {
        super($$0);
    }
}
