package net.hydra.jojomod.access;

import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public interface IPlayerEntityAbstractClient {

    boolean roundabout$getSwitched();
    void roundabout$setSwitched(boolean switched);
    PlayerModel roundabout$getOGModel();
    void roundabout$setOGModel(PlayerModel switched);

    void roundabout$setShapeShiftTemp(Mob shift);
    Mob roundabout$getShapeShiftTemp();
    void roundabout$setSwappedModel(Mob swap);
    Mob roundabout$getSwappedModel();
    void roundabout$setVisageData(VisageData data);
    VisageData roundabout$getVisageData();
    void roundabout$setLastVisage(ItemStack stack);
    ItemStack roundabout$getLastVisage();
}
