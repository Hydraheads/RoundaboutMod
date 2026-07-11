package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class KingBedBlockEntity extends BlockEntity {
    public KingBedBlockEntity(BlockPos $$0, BlockState $$1) {
        super(ModBlocks.KING_BED_BLOCK_ENTITY, $$0, $$1);
    }

    public KingBedBlockEntity(BlockPos $$0, BlockState $$1, DyeColor $$2) {
        super(ModBlocks.KING_BED_BLOCK_ENTITY, $$0, $$1);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
