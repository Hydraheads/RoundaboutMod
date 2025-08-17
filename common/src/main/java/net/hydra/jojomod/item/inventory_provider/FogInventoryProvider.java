package net.hydra.jojomod.item.inventory_provider;

import net.hydra.jojomod.client.gui.FogInventoryMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class FogInventoryProvider implements MenuProvider {
    @Override
    public Component getDisplayName() {
        return Component.literal("Fog Inventory");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new FogInventoryMenu(player.getInventory(), !player.level().isClientSide, player);
    }
}