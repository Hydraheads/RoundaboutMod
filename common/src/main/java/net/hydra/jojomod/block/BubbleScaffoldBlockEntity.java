package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BubbleScaffoldBlockEntity extends BlockEntity {

    @Nullable
    @Override
    public Level getLevel() {
        return super.getLevel();
    }

    public BubbleScaffoldBlockEntity(BlockPos $$0, BlockState $$1) {
        super(ModBlocks.BUBBLE_SCAFFOLD_BLOCK_ENTITY, $$0, $$1);
    }
}
