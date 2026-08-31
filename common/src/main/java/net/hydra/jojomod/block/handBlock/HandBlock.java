package net.hydra.jojomod.block.handBlock;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HandBlock extends AbstractHandBlock {

    public HandBlock(Properties p_56319_) {
        super(Types.PLAYER, p_56319_);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos p_151996_, BlockState p_151997_) {
        return new HandBlockEntity(p_151996_, p_151997_);
    }



}