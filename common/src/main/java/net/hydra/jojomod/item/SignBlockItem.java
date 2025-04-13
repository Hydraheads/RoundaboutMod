package net.hydra.jojomod.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class SignBlockItem extends BlockItem {
    public SignBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }
}
