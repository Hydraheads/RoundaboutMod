package net.hydra.jojomod.event.powers.jumpingJackFlash;

import net.minecraft.world.item.ItemStack;

public interface JJFLauncherAccess {

    JJFLauncherState jjf$getLauncherState();

    int jjf$getLoadedAmmoCount();

    void jjf$setLoadedAmmoCount(int count);

    ItemStack jjf$getLoadedAmmoIcon();

    void jjf$setLoadedAmmoIcon(ItemStack item);
}
