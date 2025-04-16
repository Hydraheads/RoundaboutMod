package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IPlayerEntityAbstractClient;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractClientPlayer.class)
public class PlayerEntityAbstractClient implements IPlayerEntityAbstractClient {


    @Unique
    Mob roundabout$shapeShift = null;
    @Unique
    Mob roundabout$swappedModel = null;
    @Unique
    VisageData roundabout$visageData = null;
    @Unique
    ItemStack roundabout$lastVisage = null;

    @Unique
    @Override
    public void roundabout$setShapeShiftTemp(Mob shift){
        roundabout$shapeShift = shift;
    }

    @Unique
    @Override
    public Mob roundabout$getShapeShiftTemp(){
        return roundabout$shapeShift;
    }
    @Unique
    @Override
    public void roundabout$setSwappedModel(Mob swap){
        roundabout$swappedModel = swap;
    }
    @Unique
    @Override
    public Mob roundabout$getSwappedModel(){
        return roundabout$swappedModel;
    }
    @Unique
    @Override
    public void roundabout$setVisageData(VisageData data){
        roundabout$visageData = data;
    }
    @Unique
    @Override
    public VisageData roundabout$getVisageData(){
        return roundabout$visageData;
    }
    @Unique
    @Override
    public void roundabout$setLastVisage(ItemStack stack){
        roundabout$lastVisage = stack;
    }
    @Unique
    @Override
    public ItemStack roundabout$getLastVisage(){
        return roundabout$lastVisage;
    }
}
