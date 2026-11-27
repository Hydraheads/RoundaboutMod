package net.hydra.jojomod.access;

import net.minecraft.world.item.ItemStack;

public interface IInventoryAccess {
    ItemStack sba$getItem(int $$0);
    int sba$findSlotMatchingUnusedItem(ItemStack $$0);
    int sba$findSlotMatchingItem(ItemStack $$0);
    void sba$pickSlot(int $$0);
    void sba$setPickedItem(ItemStack $$0);
    int sba$getFreeSlot();
    boolean sba$hasRemainingSpaceForItem(ItemStack $$0, ItemStack $$1);
    ItemStack sba$getSelected();
}
