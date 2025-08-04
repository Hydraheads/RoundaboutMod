package net.hydra.jojomod.mixin.model_registry;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.item.CustomItemRenderer;
import net.hydra.jojomod.client.item.CustomItemRendererRegistry;
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
/* Separate mixin for adding support for custom item renderers like how Block Entity Renderers work
* also see: RegisterItemRenderer */
public class CustomItemRendererMixin {
    @Inject(
            method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void roundabout$renderUsingCustomItemRenderer(ItemStack stack, ItemDisplayContext context, boolean isLeftHanded, PoseStack matrices, MultiBufferSource bufferSource, int light, int overlay, BakedModel model, CallbackInfo ci)
    {
        CustomItemRenderer renderer = CustomItemRendererRegistry.getItemRenderer(stack.getItem());
        if (renderer != null)
        {
            renderer.render(stack, context, isLeftHanded, matrices, bufferSource, light, overlay);
            ci.cancel(); // stop it from rendering the original way
        }
    }
}
