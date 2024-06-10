package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.event.powers.TimeStop;
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

    public int roundaboutArmorPhase;
    public boolean roundaboutModifyEntity;
    public @Nullable ItemStack roundaboutRenderChest;
    public @Nullable ItemStack roundaboutRenderLegs;
    public @Nullable ItemStack roundaboutRenderBoots;
    public @Nullable ItemStack roundaboutRenderHead;
    @Inject(method = "render", at = @At(value = "HEAD"))
    public void roundaboutRender(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9, CallbackInfo ci) {
        roundaboutArmorPhase = 0;
        if ($$3 instanceof Player) {
            roundaboutModifyEntity = ((TimeStop) $$3.level()).CanTimeStopEntity($$3);
            if (roundaboutModifyEntity) {
                if (((IEntityAndData) $$3).getRoundaboutRenderChest() == null){
                    ((IEntityAndData) $$3).setRoundaboutRenderChest($$3.getItemBySlot(EquipmentSlot.CHEST).copy());
                }
                if (((IEntityAndData) $$3).getRoundaboutRenderLegs() == null){
                    ((IEntityAndData) $$3).setRoundaboutRenderLegs($$3.getItemBySlot(EquipmentSlot.LEGS).copy());
                }
                if (((IEntityAndData) $$3).getRoundaboutRenderBoots() == null){
                    ((IEntityAndData) $$3).setRoundaboutRenderBoots($$3.getItemBySlot(EquipmentSlot.FEET).copy());
                }
                if (((IEntityAndData) $$3).getRoundaboutRenderHead() == null){
                    ((IEntityAndData) $$3).setRoundaboutRenderHead($$3.getItemBySlot(EquipmentSlot.HEAD).copy());
                }
                roundaboutRenderChest = ((IEntityAndData) $$3).getRoundaboutRenderChest();
                roundaboutRenderLegs = ((IEntityAndData) $$3).getRoundaboutRenderLegs();
                roundaboutRenderBoots = ((IEntityAndData) $$3).getRoundaboutRenderBoots();
                roundaboutRenderHead = ((IEntityAndData) $$3).getRoundaboutRenderHead();
            } else {
                if (((IEntityAndData) $$3).getRoundaboutRenderChest() != null){
                    ((IEntityAndData) $$3).setRoundaboutRenderChest(null);
                }
                if (((IEntityAndData) $$3).getRoundaboutRenderLegs() != null){
                    ((IEntityAndData) $$3).setRoundaboutRenderLegs(null);
                }
                if (((IEntityAndData) $$3).getRoundaboutRenderBoots() != null){
                    ((IEntityAndData) $$3).setRoundaboutRenderBoots(null);
                }
                if (((IEntityAndData) $$3).getRoundaboutRenderHead() != null){
                    ((IEntityAndData) $$3).setRoundaboutRenderHead(null);
                }
            }
        } else {
            roundaboutModifyEntity = false;
        }
    }

    @ModifyVariable(method = "renderArmorPiece", at = @At(value = "STORE"), ordinal = 0)
    private ItemStack renderArmorPiece(ItemStack $$0) {
        if (roundaboutModifyEntity) {
            ItemStack rewrite;
            roundaboutArmorPhase++;
            if (roundaboutArmorPhase == 1){
                if (roundaboutRenderChest != null) {
                    return roundaboutRenderChest;
                }
            } else if (roundaboutArmorPhase == 2){
                if (roundaboutRenderLegs != null) {
                    return roundaboutRenderLegs;
                }
            } else if (roundaboutArmorPhase == 3){
                if (roundaboutRenderBoots != null) {
                    return roundaboutRenderBoots;
                }
            } else {
                if (roundaboutRenderHead != null) {
                    return roundaboutRenderHead;
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
