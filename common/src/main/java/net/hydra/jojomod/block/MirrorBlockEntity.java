package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MirrorBlockEntity extends BlockEntity {

    @Nullable
    @Override
    public Level getLevel() {
        return super.getLevel();
    }

    public MirrorBlockEntity(BlockPos $$0, BlockState $$1) {
        super(ModBlocks.MIRROR_BLOCK_ENTITY, $$0, $$1);
    }
}
