package net.hydra.jojomod.menu.diverdown;

import net.hydra.jojomod.menu.ModMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;

public class DiverDownCraftingMenu extends CraftingMenu {

    //Constructor called on the client side by MenuType.create
    public DiverDownCraftingMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }

    //Constructor called on the server side when opening the menu
    public DiverDownCraftingMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(containerId, inventory, access);
    }

    @Override
    public MenuType<?> getType() {
        return ModMenus.DIVER_DOWN_CRAFTING;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.isAlive();
    }
}