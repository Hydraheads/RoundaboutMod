package net.hydra.jojomod.access;

import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public interface IPlayerEntityAbstractClient {

    boolean sba$getSwitched();
    void sba$setSwitched(boolean switched);
    PlayerModel sba$getOGModel();
    void sba$setOGModel(PlayerModel switched);

    void sba$setShapeShiftTemp(Mob shift);
    Mob sba$getShapeShiftTemp();
    void sba$setSwappedModel(Mob swap);
    Mob sba$getSwappedModel();
    void sba$setVisageData(VisageData data);
    VisageData sba$getVisageData();
    void sba$setLastVisage(ItemStack stack);
    ItemStack sba$getLastVisage();

    ResourceLocation sba$getTextureLocation2();
    ResourceLocation rdbt$getSkinTextureLocation3();
}
