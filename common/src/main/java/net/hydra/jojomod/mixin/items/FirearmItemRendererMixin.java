package net.hydra.jojomod.mixin.items;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.models.projectile.SnubnoseRevolverModel;
import net.hydra.jojomod.item.BowlerHatItem;
import net.hydra.jojomod.item.FirearmItem;
import net.hydra.jojomod.item.SnubnoseRevolverItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class FirearmItemRendererMixin {

    @Inject(method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V", at = @At("HEAD"), cancellable = true)

    private void hideFirearmSpriteWhenHeld(ItemStack stack, ItemDisplayContext transformType, boolean leftHanded, PoseStack matrices, MultiBufferSource buffer, int packedLight, int packedOverlay, BakedModel model, CallbackInfo ci) {

        if (!stack.isEmpty() && stack.getItem() instanceof FirearmItem) {

            if (transformType == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || transformType == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND || transformType == ItemDisplayContext.THIRD_PERSON_LEFT_HAND) {
                ci.cancel();
            }
        }
    }
}
