package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class ChessBoardBlockEntity extends BlockEntity {
	public ChessBoardBlockEntity(BlockPos $$0, BlockState $$1) {
		super(ModBlocks.COFFIN_BLOCK_ENTITY, $$0, $$1);
		//super(ModBlocks.CHESSBOARD_BLOCK_ENTITY, $$0, $$1);
    }
	
}
