package net.hydra.jojomod.access;

import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public interface IPlayerEntityAbstractClient {


    void roundabout$setShapeShiftTemp(Mob shift);
    Mob roundabout$getShapeShiftTemp();
    void roundabout$setSwappedModel(Mob swap);
    Mob roundabout$getSwappedModel();
    void roundabout$setVisageData(VisageData data);
    VisageData roundabout$getVisageData();
    void roundabout$setLastVisage(ItemStack stack);
    ItemStack roundabout$getLastVisage();
}
