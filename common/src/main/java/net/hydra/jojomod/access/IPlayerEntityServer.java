package net.hydra.jojomod.access;

import net.hydra.jojomod.entity.D4CCloneEntity;
import net.hydra.jojomod.entity.stand.BlackSabbathEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface IPlayerEntityServer {
    void sba$initMenu(AbstractContainerMenu $$0);
    void sba$nextContainerCounter();
    int sba$getCounter();
    void sba$setInvincibleTicks(int ticks);
    void sba$openBlackSabbathInventory(BlackSabbathEntity $$0, Container $$1);
    void sba$openD4CCloneInventory(D4CCloneEntity $$0, Container $$1);
    void sba$setTemporaryGerSpawn(BlockPos deathPos);
    void sba$copySavedGerSpawnFrom(IPlayerEntityServer oldPlayer);
}
