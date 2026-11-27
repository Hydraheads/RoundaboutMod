package net.hydra.jojomod.mixin.time_stop;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.powers.StandUserClient;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class TimeStopHumanoidArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {

    /**This class is targetted so the last piece of equipment worn before a timestop is rendered in the timestop*/

    @Unique
    public int sba$ArmorPhase;
    @Unique
    public boolean sba$ModifyEntity;
    @Unique
    public @Nullable ItemStack sba$RenderChest;
    @Unique
    public @Nullable ItemStack sba$RenderLegs;
    @Unique
    public @Nullable ItemStack sba$RenderBoots;
    @Unique
    public @Nullable ItemStack sba$RenderHead;


    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
            at = @At(value = "HEAD"),cancellable = true)
    public void sba$Render(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9, CallbackInfo ci) {
        sba$ArmorPhase = 0;
        StandUserClient userclient = ((StandUserClient) $$3);

        if ($$3 instanceof Player PE) {
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            if (ShapeShifts.getShiftFromByte(ipe.sba$getShapeShift()) == ShapeShifts.EERIE ||
                    ShapeShifts.getShiftFromByte(ipe.sba$getShapeShift()) == ShapeShifts.OVA){
                ci.cancel();
                return;
            }

            sba$ModifyEntity = ((TimeStop) $$3.level()).CanTimeStopEntity($$3) || ClientUtil.getScreenFreeze();
            if (sba$ModifyEntity) {
                if (userclient.sba$getSBARenderChest() == null){
                    userclient.sba$setSBARenderChest($$3.getItemBySlot(EquipmentSlot.CHEST).copy());
                }
                if (userclient.sba$getSBARenderLegs() == null){
                    userclient.sba$setSBARenderLegs($$3.getItemBySlot(EquipmentSlot.LEGS).copy());
                }
                if (userclient.sba$getSBARenderBoots() == null){
                    userclient.sba$setSBARenderBoots($$3.getItemBySlot(EquipmentSlot.FEET).copy());
                }
                if (userclient.sba$getSBARenderHead() == null){
                    userclient.sba$setSBARenderHead($$3.getItemBySlot(EquipmentSlot.HEAD).copy());
                }
                sba$RenderChest = userclient.sba$getSBARenderChest();
                sba$RenderLegs = userclient.sba$getSBARenderLegs();
                sba$RenderBoots = userclient.sba$getSBARenderBoots();
                sba$RenderHead = userclient.sba$getSBARenderHead();
            } else {
                if (userclient.sba$getSBARenderChest() != null){
                    userclient.sba$setSBARenderChest(null);
                }
                if (userclient.sba$getSBARenderLegs() != null){
                    userclient.sba$setSBARenderLegs(null);
                }
                if (userclient.sba$getSBARenderBoots() != null){
                    userclient.sba$setSBARenderBoots(null);
                }
                if (userclient.sba$getSBARenderHead() != null){
                    userclient.sba$setSBARenderHead(null);
                }
            }
        } else {
            sba$ModifyEntity = false;
        }
    }
    @ModifyVariable(method = "renderArmorPiece", at = @At(value = "STORE"), ordinal = 0)
    private ItemStack sba$renderArmorPiece(ItemStack $$0) {
        if (sba$ModifyEntity) {
            ItemStack rewrite;
            sba$ArmorPhase++;
            if (sba$ArmorPhase == 1){
                if (sba$RenderChest != null) {
                    return sba$RenderChest;
                }
            } else if (sba$ArmorPhase == 2){
                if (sba$RenderLegs != null) {
                    return sba$RenderLegs;
                }
            } else if (sba$ArmorPhase == 3){
                if (sba$RenderBoots != null) {
                    return sba$RenderBoots;
                }
            } else {
                if (sba$RenderHead != null) {
                    return sba$RenderHead;
                }
            }
            return $$0;
        } else {
            return $$0;
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    public void render(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {

    }

    @Shadow protected abstract ResourceLocation getArmorLocation(ArmorItem $$0, boolean $$1, @Nullable String $$2);

    public TimeStopHumanoidArmorLayer(RenderLayerParent<T, M> $$0) {
        super($$0);
    }
}
