package net.hydra.jojomod.access;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public interface WhitesnakeInventoryAccess {
    NonNullList<ItemStack> roundaboutWhitesnake$getInventory();

    void roundaboutWhitesnake$setInventory(NonNullList<ItemStack> inventory);

    void roundaboutWhitesnake$syncInventory();
}
