package net.hydra.jojomod.access;

import net.hydra.jojomod.entity.stand.BlackSabbathEntity;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface IPlayerEntityServer {
    void roundabout$initMenu(AbstractContainerMenu $$0);
    void roundabout$nextContainerCounter();
    int roundabout$getCounter();
    void roundabout$setInvincibleTicks(int ticks);
    void roundabout$openBlackSabbathInventory(BlackSabbathEntity $$0, Container $$1);
}
