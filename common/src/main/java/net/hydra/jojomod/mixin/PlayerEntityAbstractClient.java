package net.hydra.jojomod.mixin;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerEntityAbstractClient;
import net.hydra.jojomod.access.IPlayerModel;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.hydra.jojomod.item.MaskItem;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class PlayerEntityAbstractClient extends Player implements IPlayerEntityAbstractClient {


    public boolean roundabout$switched = false;

    @Override
    @Unique
    public boolean roundabout$getSwitched(){
        return this.roundabout$switched;
    }
    @Override
    @Unique
    public void roundabout$setSwitched(boolean switched){
        this.roundabout$switched = switched;
    }

    @Unique
    public PlayerModel roundabout$playerModel = null;

    @Override
    @Unique
    public PlayerModel roundabout$getOGModel(){
        return this.roundabout$playerModel;
    }
    @Override
    @Unique
    public void roundabout$setOGModel(PlayerModel switched){
            this.roundabout$playerModel = switched;
    }
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
        if (shift == ShapeShifts.OVA) {
            cir.setReturnValue(StandIcons.OVA_ENYA_SKIN);
        } else if (shift == ShapeShifts.EERIE){
            cir.setReturnValue(StandIcons.EERIE_SKIN);
        } else {
            ItemStack visage = ple.roundabout$getMaskSlot();
            if (visage != null && !visage.isEmpty()) {
                if (visage.getItem() instanceof MaskItem MI) {
                    if (MI.visageData.isCharacterVisage()) {
                        if (FateTypes.isUndisguisedZombie(this)) {
                            // 37 67 -34
                            cir.setReturnValue(new ResourceLocation(Roundabout.MOD_ID, "textures/entity/visage/zombie_skins/" + MI.visageData.getSkinPath() + ".png"));
                        } else {
                            cir.setReturnValue(new ResourceLocation(Roundabout.MOD_ID, "textures/entity/visage/player_skins/" + MI.visageData.getSkinPath() + ".png"));

                        }
                        return;
                    }
                }
            }

            if (FateTypes.isUndisguisedZombie(this)) {
                PlayerModel pm = ClientUtil.getPlayerModel(this);
                if (pm != null && (((IPlayerModel)pm).roundabout$getSlim())){
                    cir.setReturnValue(StandIcons.ZOMBIE_SKIN_SLIM);
                } else {
                    cir.setReturnValue(StandIcons.ZOMBIE_SKIN);
                }
            }
        }
    }




    public PlayerEntityAbstractClient(Level $$0, BlockPos $$1, float $$2, GameProfile $$3) {
        super($$0, $$1, $$2, $$3);
    }
}
