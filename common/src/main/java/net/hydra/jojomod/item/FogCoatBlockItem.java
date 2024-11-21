package net.hydra.jojomod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class FogCoatBlockItem extends FogBlockItem {
    public FogCoatBlockItem(Block $$0, Properties $$1, Block mimicking) {
        super($$0, $$1, mimicking);
    }
    @Override
    public Component getName(ItemStack $$0) {
        if (mimicking != null) {
            return Component.translatable("block.roundabout.fog_block.title").append(" ").
                    append(Component.translatable(mimicking.asItem().getDescriptionId(mimicking.asItem().getDefaultInstance())))
                    .append(" ").append(Component.translatable("block.roundabout.fog_block_coating.title"));
        } else {
            return super.getName($$0);
        }
    }

}