package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.MaskItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
public class ZHumanoidArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {

    /**This class is targetted so the last piece of equipment worn before a timestop is rendered in the timestop*/

    public ZHumanoidArmorLayer(RenderLayerParent<T, M> $$0) {
        super($$0);
    }

    @Unique
    public int roundabout$ArmorPhase;
    @Unique
    public boolean roundabout$ModifyEntity;
    @Unique
    public @Nullable ItemStack roundabout$RenderChest;
    @Unique
    public @Nullable ItemStack roundabout$RenderLegs;
    @Unique
    public @Nullable ItemStack roundabout$RenderBoots;
    @Unique
    public @Nullable ItemStack roundabout$RenderHead;
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
            at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$Render(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9, CallbackInfo ci) {
        roundabout$ArmorPhase = 0;
        IEntityAndData entityAndData = ((IEntityAndData) $$3);
        if (entityAndData.roundabout$getTrueInvisibility() > -1){
            ci.cancel();
            return;
        }

        if ($$3 instanceof Player PE) {
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            if (ShapeShifts.getShiftFromByte(ipe.roundabout$getShapeShift()) == ShapeShifts.EERIE ||
                    ShapeShifts.getShiftFromByte(ipe.roundabout$getShapeShift()) == ShapeShifts.OVA){
                ci.cancel();
                return;
            }

            roundabout$ModifyEntity = ((TimeStop) $$3.level()).CanTimeStopEntity($$3) || ClientUtil.getScreenFreeze();
            if (roundabout$ModifyEntity) {
                if (entityAndData.roundabout$getRoundaboutRenderChest() == null){
                    entityAndData.roundabout$setRoundaboutRenderChest($$3.getItemBySlot(EquipmentSlot.CHEST).copy());
                }
                if (entityAndData.roundabout$getRoundaboutRenderLegs() == null){
                    entityAndData.roundabout$setRoundaboutRenderLegs($$3.getItemBySlot(EquipmentSlot.LEGS).copy());
                }
                if (entityAndData.roundabout$getRoundaboutRenderBoots() == null){
                    entityAndData.roundabout$setRoundaboutRenderBoots($$3.getItemBySlot(EquipmentSlot.FEET).copy());
                }
                if (entityAndData.roundabout$getRoundaboutRenderHead() == null){
                    entityAndData.roundabout$setRoundaboutRenderHead($$3.getItemBySlot(EquipmentSlot.HEAD).copy());
                }
                roundabout$RenderChest = entityAndData.roundabout$getRoundaboutRenderChest();
                roundabout$RenderLegs = entityAndData.roundabout$getRoundaboutRenderLegs();
                roundabout$RenderBoots = entityAndData.roundabout$getRoundaboutRenderBoots();
                roundabout$RenderHead = entityAndData.roundabout$getRoundaboutRenderHead();
            } else {
                if (entityAndData.roundabout$getRoundaboutRenderChest() != null){
                    entityAndData.roundabout$setRoundaboutRenderChest(null);
                }
                if (entityAndData.roundabout$getRoundaboutRenderLegs() != null){
                    entityAndData.roundabout$setRoundaboutRenderLegs(null);
                }
                if (entityAndData.roundabout$getRoundaboutRenderBoots() != null){
                    entityAndData.roundabout$setRoundaboutRenderBoots(null);
                }
                if (entityAndData.roundabout$getRoundaboutRenderHead() != null){
                    entityAndData.roundabout$setRoundaboutRenderHead(null);
                }
            }

            if (!((IPlayerEntity)PE).roundabout$getMaskSlot().isEmpty()
                    && ((IPlayerEntity)PE).roundabout$getMaskSlot().getItem() instanceof MaskItem ME
            && !ME.visageData.generateVisageData(PE).rendersArmor()){
                ci.cancel();
            }

        } else {
            roundabout$ModifyEntity = false;
        }
    }
    @ModifyVariable(method = "renderArmorPiece", at = @At(value = "STORE"), ordinal = 0)
    private ItemStack roundabout$renderArmorPiece(ItemStack $$0) {
        if (roundabout$ModifyEntity) {
            ItemStack rewrite;
            roundabout$ArmorPhase++;
            if (roundabout$ArmorPhase == 1){
                if (roundabout$RenderChest != null) {
                    return roundabout$RenderChest;
                }
            } else if (roundabout$ArmorPhase == 2){
                if (roundabout$RenderLegs != null) {
                    return roundabout$RenderLegs;
                }
            } else if (roundabout$ArmorPhase == 3){
                if (roundabout$RenderBoots != null) {
                    return roundabout$RenderBoots;
                }
            } else {
                if (roundabout$RenderHead != null) {
                    return roundabout$RenderHead;
                }
            }
            return $$0;
        } else {
            return $$0;
        }
    }

    @Shadow
    public void render(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {

    }
}
