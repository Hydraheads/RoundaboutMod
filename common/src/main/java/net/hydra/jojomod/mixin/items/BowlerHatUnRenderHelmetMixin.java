package net.hydra.jojomod.mixin.items;

import net.hydra.jojomod.event.powers.visagedata.DiegoVisage;
import net.hydra.jojomod.item.BowlerHatItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class BowlerHatUnRenderHelmetMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private BowlerHatUnRenderHelmetMixin() {
        super(null);
    }

    @Inject(method= "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;)V", at = @At("HEAD"), cancellable = true)
    private void cancelHelmetRendering(com.mojang.blaze3d.vertex.PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource bufferSource, T entity, EquipmentSlot slot, int packedLight, A model, CallbackInfo ci) {
        if (slot == EquipmentSlot.HEAD) {
            ItemStack hand = entity.getMainHandItem();
            ItemStack offHand = entity.getOffhandItem();
            if (hand.getItem() instanceof BowlerHatItem || offHand.getItem() instanceof BowlerHatItem) {
                ci.cancel();
            }
        }
    }
}