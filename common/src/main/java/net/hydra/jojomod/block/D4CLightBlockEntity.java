package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class D4CLightBlockEntity extends BlockEntity {
    public D4CLightBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlocks.D4C_LIGHT_BLOCK_ENTITY, pos, state);
    }

    public D4CLightBlockEntity(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }
}
