package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class KingBedBlock extends BedBlock {
    public KingBedBlock(DyeColor $$0, Properties $$1) {
        super($$0, $$1);
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos $$0, BlockState $$1) {
        return new KingBedBlockEntity($$0, $$1);
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level $$0, BlockState $$1, BlockEntityType<T> $$2) {
        return !$$0.isClientSide() ? createTickerHelper($$2, ModBlocks.KING_BED_BLOCK_ENTITY, KingBedBlockEntity::tickThis) : null;
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> $$0, BlockEntityType<E> $$1, BlockEntityTicker<? super E> $$2
    ) {
        return $$1 == $$0 ? (BlockEntityTicker<A>) $$2 : null;
    }
}
