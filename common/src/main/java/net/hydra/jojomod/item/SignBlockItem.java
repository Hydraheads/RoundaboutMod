package net.hydra.jojomod.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class SignBlockItem extends BlockItem {
    public SignBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }


    @Override
    public boolean hurtEnemy(ItemStack $$0, LivingEntity $$1, LivingEntity $$2) {
        if (!$$1.level().isClientSide()) {
            CompoundTag ct = $$0.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            ctd++;
            if (ctd > 2) {
                $$0.shrink(1);
            } else {
                ct.putInt("damaged", ctd);
            }
        }
        return true;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }
}
