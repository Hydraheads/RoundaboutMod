package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class ChessPieceBlockEntity extends BlockEntity {

    public ChessPieceBlockEntity(BlockPos $$0, BlockState $$1) {
        super(getBE($$0,$$1), $$0, $$1);
    }

    public static BlockEntityType getBE(BlockPos $$0, BlockState $$1){
        if ($$1.is(ModBlocks.BLACK_BISHOP)){
            return ModBlocks.BLACK_BISHOP_ENTITY;
        } if ($$1.is(ModBlocks.BLACK_QUEEN)){
            return ModBlocks.BLACK_QUEEN_ENTITY;
        } if ($$1.is(ModBlocks.BLACK_ROOK)){
            return ModBlocks.BLACK_ROOK_ENTITY;
        } if ($$1.is(ModBlocks.BLACK_KING)){
            return ModBlocks.BLACK_KING_ENTITY;
        } if ($$1.is(ModBlocks.BLACK_PAWN)){
            return ModBlocks.BLACK_PAWN_ENTITY;
        } if ($$1.is(ModBlocks.BLACK_KNIGHT)){
            return ModBlocks.BLACK_KNIGHT_ENTITY;
        }

        if ($$1.is(ModBlocks.WHITE_BISHOP)){
            return ModBlocks.WHITE_BISHOP_ENTITY;
        } if ($$1.is(ModBlocks.WHITE_QUEEN)){
            return ModBlocks.WHITE_QUEEN_ENTITY;
        } if ($$1.is(ModBlocks.WHITE_ROOK)){
            return ModBlocks.WHITE_ROOK_ENTITY;
        } if ($$1.is(ModBlocks.WHITE_KING)){
            return ModBlocks.WHITE_KING_ENTITY;
        } if ($$1.is(ModBlocks.WHITE_PAWN)){
            return ModBlocks.WHITE_PAWN_ENTITY;
        } if ($$1.is(ModBlocks.WHITE_KNIGHT)){
            return ModBlocks.WHITE_KNIGHT_ENTITY;
        }
        return ModBlocks.BLACK_PAWN_ENTITY;
    }

    private static Direction getNeighbourDirection(BedPart $$0, Direction $$1) {
        return $$0 == BedPart.FOOT ? $$1 : $$1.getOpposite();
    }
}
