package net.hydra.jojomod.access;

import net.minecraft.world.item.ItemStack;

public interface IInventoryAccess {
    ItemStack roundabout$getItem(int $$0);
    int roundabout$findSlotMatchingUnusedItem(ItemStack $$0);
    int roundabout$findSlotMatchingItem(ItemStack $$0);
    void roundabout$pickSlot(int $$0);
    void roundabout$setPickedItem(ItemStack $$0);
    int roundabout$getFreeSlot();
    boolean roundabout$hasRemainingSpaceForItem(ItemStack $$0, ItemStack $$1);
    ItemStack roundabout$getSelected();
}
