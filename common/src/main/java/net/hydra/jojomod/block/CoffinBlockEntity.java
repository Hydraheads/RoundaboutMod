package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CoffinBlockEntity extends BlockEntity {

    public CoffinBlockEntity(BlockPos $$0, BlockState $$1) {
        super(ModBlocks.COFFIN_BLOCK_ENTITY, $$0, $$1);
    }

    public CoffinBlockEntity(BlockPos $$0, BlockState $$1, DyeColor $$2) {
        super(ModBlocks.COFFIN_BLOCK_ENTITY, $$0, $$1);
    }
}
