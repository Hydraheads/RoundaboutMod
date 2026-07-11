package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class KingBedBlock extends BedBlock {
    public KingBedBlock(DyeColor $$0, Properties $$1) {
        super($$0, $$1);
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos $$0, BlockState $$1) {
        return new KingBedBlockEntity($$0, $$1);
    }

}
