package net.hydra.jojomod.item;

import net.hydra.jojomod.block.HallucinatoryAcidBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public final class HallucinatoryAcidDebugItem extends BlockItem {
    private final int layers;

    public HallucinatoryAcidDebugItem(HallucinatoryAcidBlock block, int layers, Properties properties) {
        super(block, properties);
        this.layers = layers;
    }

    @Override
    protected @Nullable BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        return state == null ? null : state.setValue(HallucinatoryAcidBlock.LAYERS, layers);
    }
}
