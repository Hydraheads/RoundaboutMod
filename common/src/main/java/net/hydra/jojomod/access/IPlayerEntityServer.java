package net.hydra.jojomod.access;

import net.minecraft.world.inventory.AbstractContainerMenu;

public interface IPlayerEntityServer {
    void roundabout$initMenu(AbstractContainerMenu $$0);
    void roundabout$nextContainerCounter();
    int roundabout$getCounter();
    void roundabout$setInvincibleTicks(int ticks);
}
