package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class ChessPieceBlockEntity extends BlockEntity {

    public ChessPieceBlockEntity(BlockPos $$0, BlockState $$1) {
        super(ModBlocks.CHESS_PIECE_BLOCK_ENTITY, $$0, $$1);
    }

    public ChessPieceBlockEntity(BlockPos $$0, BlockState $$1, DyeColor $$2) {
        super(ModBlocks.CHESS_PIECE_BLOCK_ENTITY, $$0, $$1);
    }

    private static Direction getNeighbourDirection(BedPart $$0, Direction $$1) {
        return $$0 == BedPart.FOOT ? $$1 : $$1.getOpposite();
    }
}
