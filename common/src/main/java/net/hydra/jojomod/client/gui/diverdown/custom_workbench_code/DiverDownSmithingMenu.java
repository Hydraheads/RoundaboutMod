package net.hydra.jojomod.client.gui.diverdown.custom_workbench_code;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;

public class DiverDownSmithingMenu extends SmithingMenu {

    //Constructor called on the client side by MenuType.create
    public DiverDownSmithingMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }

    //Constructor called on the server side when opening the menu
    public DiverDownSmithingMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(containerId, inventory, access);
    }

    @Override
    public MenuType<?> getType() {
        return ModMenus.DIVER_DOWN_SMITHING;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.isAlive();
    }
}