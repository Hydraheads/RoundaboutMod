package net.hydra.jojomod.event.powers;

import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public interface StandUserClientPlayer {
    long sba$getClashDisplayExtraTimestamp();
    float sba$getLastClashPower();
    int sba$getMenuTicks();
    void sba$setMenuTicks(int menuTicks);
    void sba$setClashDisplayExtraTimestamp(long set);
    void sba$setLastClashPower(float set);
    int sba$getSBANoPlaceTSTicks();

}
