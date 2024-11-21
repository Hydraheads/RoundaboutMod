package net.hydra.jojomod.item;

import net.hydra.jojomod.block.FogBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class FogBlockItem extends BlockItem {
    private final Block mimicking;
    public FogBlockItem(Block $$0, Properties $$1, Block mimicking) {
        super($$0, $$1);
        this.mimicking = mimicking;
    }
    @Override
    public Component getName(ItemStack $$0) {
        if (mimicking != null) {
            return Component.translatable("block.roundabout.fog_block.title").append(" ").
                    append(Component.translatable(mimicking.asItem().getDescriptionId(mimicking.asItem().getDefaultInstance())));
        } else {
            return super.getName($$0);
        }
    }

}
