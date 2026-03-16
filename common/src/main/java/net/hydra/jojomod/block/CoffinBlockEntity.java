package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CoffinBlockEntity extends BlockEntity {

    public CoffinBlockEntity(BlockPos $$0, BlockState $$1) {
        super(ModBlocks.COFFIN_BLOCK_ENTITY, $$0, $$1);
    }

    public CoffinBlockEntity(BlockPos $$0, BlockState $$1, DyeColor $$2) {
        super(ModBlocks.COFFIN_BLOCK_ENTITY, $$0, $$1);
    }
    public float closed = 0;
    public float oClosed = 0;
    public boolean closing = false;
    public static void lidAnimateTick(Level $$0, BlockPos $$1, BlockState $$2, CoffinBlockEntity $$3) {
        $$3.oClosed = $$3.closed;
        if ($$2.hasProperty(CoffinBlock.OCCUPIED) && $$2.getValue(CoffinBlock.OCCUPIED)){
            $$3.closing = true;
            $$3.closed = Math.max($$3.closed - 0.1F, 0.0F);
        } else {
            $$3.closing = false;
            $$3.closed = Math.min($$3.closed + 0.1F, 1.0F);
        }
    }

}
