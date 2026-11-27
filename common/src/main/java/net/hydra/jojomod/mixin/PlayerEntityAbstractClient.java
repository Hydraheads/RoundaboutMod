package net.hydra.jojomod.mixin;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.SBA;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerEntityAbstractClient;
import net.hydra.jojomod.access.IPlayerModel;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.util.SkinUtil;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class PlayerEntityAbstractClient extends Player implements IPlayerEntityAbstractClient {


    @Shadow
    public abstract ResourceLocation getSkinTextureLocation();

    @Shadow
    protected abstract @Nullable PlayerInfo getPlayerInfo();

    public boolean sba$switched = false;

    @Override
    @Unique
    public boolean sba$getSwitched(){
        return this.sba$switched;
    }
    @Override
    @Unique
    public void sba$setSwitched(boolean switched){
        this.sba$switched = switched;
    }

    @Unique
    public PlayerModel sba$playerModel = null;

    @Override
    @Unique
    public PlayerModel sba$getOGModel(){
        return this.sba$playerModel;
    }
    @Override
    @Unique
    public void sba$setOGModel(PlayerModel switched){
            this.sba$playerModel = switched;
    }
    @Unique
    Mob sba$shapeShift = null;
    @Unique
    Mob sba$swappedModel = null;
    @Unique
    VisageData sba$visageData = null;
    @Unique
    ItemStack sba$lastVisage = null;

    @Unique
    @Override
    public void sba$setShapeShiftTemp(Mob shift){
        sba$shapeShift = shift;
    }

    @Unique
    @Override
    public Mob sba$getShapeShiftTemp(){
        return sba$shapeShift;
    }
    @Unique
    @Override
    public void sba$setSwappedModel(Mob swap){
        sba$swappedModel = swap;
    }
    @Unique
    @Override
    public Mob sba$getSwappedModel(){
        return sba$swappedModel;
    }
    @Unique
    @Override
    public void sba$setVisageData(VisageData data){
        sba$visageData = data;
    }
    @Unique
    @Override
    public VisageData sba$getVisageData(){
        return sba$visageData;
    }
    @Unique
    @Override
    public void sba$setLastVisage(ItemStack stack){
        sba$lastVisage = stack;
    }
    @Unique
    @Override
    public ItemStack sba$getLastVisage(){
        return sba$lastVisage;
    }
    @Inject(method = "getSkinTextureLocation",
            at = @At(value = "HEAD"), cancellable = true)
    public void sba$getTextureLocation(CallbackInfoReturnable<ResourceLocation> cir) {
        if (this instanceof StandUser su && su.sba$isDisguised()) {
            GameProfile profile = su.sba$getDisguiseProfile();
            if (profile != null) {
                cir.setReturnValue(SkinUtil.getSkin(profile).texture());
                return;
            }
        }
        IPlayerEntity ple = ((IPlayerEntity) this);
        byte shape = ple.sba$getShapeShift();
        ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
        if (shift == ShapeShifts.OVA) {
            cir.setReturnValue(StandIcons.OVA_ENYA_SKIN);
        } else if (shift == ShapeShifts.EERIE){
            cir.setReturnValue(StandIcons.EERIE_SKIN);
        } else {
            ItemStack visage = ple.sba$getMaskSlot();
            if (visage != null && !visage.isEmpty()) {
                if (visage.getItem() instanceof MaskItem MI) {
                    VisageData vd = MI.visageData.generateVisageData(this);
                    if (vd.isCharacterVisage()) {
                        if (FateTypes.isUndisguisedZombie(this)) {
                            // 37 67 -34
                            cir.setReturnValue(new ResourceLocation(SBA.MOD_ID, "textures/entity/visage/zombie_skins/" + vd.getSkinPath() + ".png"));

                        } else {
                            cir.setReturnValue(new ResourceLocation(SBA.MOD_ID, "textures/entity/visage/player_skins/" + vd.getSkinPath() + ".png"));

                        }
                        return;
                    } else if (visage.is(ModItems.RAT_MASK) && !isSpectator()){
                        cir.setReturnValue(new ResourceLocation(SBA.MOD_ID, "textures/entity/visage/rat/rat_skin.png"));
                        return;
                    }
                }
            }

            if (FateTypes.isUndisguisedZombie(this)) {
                PlayerModel pm = ClientUtil.getPlayerModel(this);
                if (pm != null && (((IPlayerModel)pm).sba$getSlim())){
                    cir.setReturnValue(StandIcons.ZOMBIE_SKIN_SLIM);
                    return;
                } else {
                    cir.setReturnValue(StandIcons.ZOMBIE_SKIN);
                    return;
                }
            }
        }
    }

    @Unique
    @Override
    public ResourceLocation sba$getTextureLocation2() {
        IPlayerEntity ple = ((IPlayerEntity) this);
        byte shape = ple.sba$getShapeShift();
        ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
        if (shift == ShapeShifts.OVA) {
            return StandIcons.OVA_ENYA_SKIN;
        } else if (shift == ShapeShifts.EERIE){
            return StandIcons.EERIE_SKIN;
        } else {
            ItemStack visage = ple.sba$getMaskSlot();
            if (visage != null && !visage.isEmpty()) {
                if (visage.getItem() instanceof MaskItem MI) {
                    VisageData vd = MI.visageData.generateVisageData(this);
                    if (vd.isCharacterVisage()) {
                        if (FateTypes.isUndisguisedZombie(this)) {
                            // 37 67 -34
                            return (new ResourceLocation(SBA.MOD_ID, "textures/entity/visage/zombie_skins/" + vd.getSkinPath() + ".png"));
                        } else {
                            return (new ResourceLocation(SBA.MOD_ID, "textures/entity/visage/player_skins/" + vd.getSkinPath() + ".png"));

                        }
                    }
                }
            }

            if (FateTypes.isUndisguisedZombie(this)) {
                PlayerModel pm = ClientUtil.getPlayerModel(this);
                if (pm != null && (((IPlayerModel)pm).sba$getSlim())){
                    return (StandIcons.ZOMBIE_SKIN_SLIM);
                } else {
                    return (StandIcons.ZOMBIE_SKIN);
                }
            }
        }

        return rdbt$getSkinTextureLocation3();
    }


    public ResourceLocation rdbt$getSkinTextureLocation3() {
        if (this instanceof StandUser su && su.sba$isDisguised()) {
            GameProfile profile = su.sba$getDisguiseProfile();
            if (profile != null) {
                return SkinUtil.getSkin(profile).texture();
            }
        }
        PlayerInfo $$0 = this.getPlayerInfo();
        return $$0 == null ? DefaultPlayerSkin.getDefaultSkin(this.getUUID()) : $$0.getSkinLocation();
    }

    public PlayerEntityAbstractClient(Level $$0, BlockPos $$1, float $$2, GameProfile $$3) {
        super($$0, $$1, $$2, $$3);
    }
}
