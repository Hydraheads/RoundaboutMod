package net.hydra.jojomod.access;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public interface WhitesnakeInventoryAccess {
    NonNullList<ItemStack> sbaWhitesnake$getInventory();

    void sbaWhitesnake$setInventory(NonNullList<ItemStack> inventory);

    void sbaWhitesnake$syncInventory();
}
