package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerEntityAbstractClient;
import net.hydra.jojomod.access.IPlayerModel;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.hydra.jojomod.item.MaskItem;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
    @Inject(method = "getSkinTextureLocation",
            at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$getTextureLocation(CallbackInfoReturnable<ResourceLocation> cir) {
        IPlayerEntity ple = ((IPlayerEntity) this);
        byte shape = ple.roundabout$getShapeShift();
        ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
        if (shift == ShapeShifts.OVA){
            cir.setReturnValue(StandIcons.OVA_ENYA_SKIN);
        } else {
            ItemStack visage = ple.roundabout$getMaskSlot();
            if (visage != null && !visage.isEmpty()) {
                if (visage.getItem() instanceof MaskItem MI) {
                    if (MI.visageData.isCharacterVisage()) {
                        cir.setReturnValue(new ResourceLocation(Roundabout.MOD_ID, "textures/entity/visage/player_skins/"+MI.visageData.getSkinPath()+".png"));
                    }
                }
            }
        }
    }
}
