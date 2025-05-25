package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BubbleScaffoldBlockEntity extends BlockEntity {

    public int tickCount = 100;

    public List<Vec3> bubbleList = new ArrayList<>();
    public int getTickCount(){
        return tickCount;
    }

    @Nullable
    @Override
    public Level getLevel() {
        return super.getLevel();
    }

    public BubbleScaffoldBlockEntity(BlockPos $$0, BlockState $$1) {
        super(ModBlocks.BUBBLE_SCAFFOLD_BLOCK_ENTITY, $$0, $$1);
    }
}
