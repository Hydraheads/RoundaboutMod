package net.hydra.jojomod.event.powers;

import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public interface StandUserClientPlayer {
    long roundabout$getClashDisplayExtraTimestamp();
    float roundabout$getLastClashPower();
    int roundabout$getMenuTicks();
    void roundabout$setMenuTicks(int menuTicks);
    void roundabout$setClashDisplayExtraTimestamp(long set);
    void roundabout$setLastClashPower(float set);
    int roundabout$getRoundaboutNoPlaceTSTicks();

    void roundabout$setShapeShiftTemp(Mob shift);
    Mob roundabout$getShapeShiftTemp();
    void roundabout$setSwappedModel(Mob swap);
    Mob roundabout$getSwappedModel();
    void roundabout$setVisageData(VisageData data);
    VisageData roundabout$getVisageData();
    void roundabout$setLastVisage(ItemStack stack);
    ItemStack roundabout$getLastVisage();
}
