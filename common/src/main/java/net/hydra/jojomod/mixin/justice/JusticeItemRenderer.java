package net.hydra.jojomod.mixin.justice;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.block.FogBlock;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class JusticeItemRenderer {

    /**Justice rendering the little star next to fog block items*/
    @Inject(method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V", at = @At(value = "TAIL"))
    public void roundabout$renderTail(ItemStack $$0, ItemDisplayContext $$1, boolean $$2, PoseStack $$3, MultiBufferSource $$4, int $$5, int $$6, BakedModel $$7, CallbackInfo ci) {
        if (!$$0.isEmpty() && $$0.getItem() instanceof BlockItem BE && BE.getBlock() instanceof FogBlock) {
            ClientUtil.applyJusticeFogBlockTextureOverlayInInventory($$0,$$1,$$2,$$3,$$4,$$5,$$6,this.itemModelShaper,this.blockEntityRenderer,
                    ((ItemRenderer)(Object)this));
        }
    }
    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    @Final
    private BlockEntityWithoutLevelRenderer blockEntityRenderer;
    @Shadow
    @Final
    private ItemModelShaper itemModelShaper;

    @Shadow
    public void render(ItemStack $$0, ItemDisplayContext $$1, boolean $$2, PoseStack $$3, MultiBufferSource $$4, int $$5, int $$6, BakedModel $$7) {
    }
}
