package net.hydra.jojomod.menu.diverdown;

import net.hydra.jojomod.menu.ModMenus;
import net.hydra.jojomod.mixin.access.AnvilMenuAccessor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;

public class DiverDownAnvilMenu extends AnvilMenu {

    //Constructor called on the client side by MenuType.create
    public DiverDownAnvilMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }

    //Constructor called on the server side when opening the menu
    public DiverDownAnvilMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(containerId, inventory, access);
    }

    @Override
    public MenuType<?> getType() {
        return ModMenus.DIVER_DOWN_ANVIL;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.isAlive();
    }

    //to make levels cost 1 more when using this anvil
    //had to make a new mixin just for cost. ugh.
    @Override
    public void createResult() {
        super.createResult();
        DataSlot costSlot = ((AnvilMenuAccessor) this).roundabout$getCostSlot();
        // prevents the cost just appearing as 1, even when there's nothing
        if (costSlot.get() > 0) {
            costSlot.set(costSlot.get() + 1);
        }
    }
}