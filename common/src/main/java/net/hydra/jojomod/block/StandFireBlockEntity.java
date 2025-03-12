package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class StandFireBlockEntity extends BlockEntity{
    public StandFireBlockEntity(BlockPos $$0, BlockState $$1)
    {
        super(ModBlocks.STAND_FIRE_BLOCK_ENTITY, $$0, $$1);
    }

    public StandFireBlockEntity(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }
}
