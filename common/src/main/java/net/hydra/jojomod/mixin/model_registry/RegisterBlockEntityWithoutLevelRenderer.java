package net.hydra.jojomod.mixin.model_registry;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.access.IBlockEntityWithoutLevelRenderer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModItemModels;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class RegisterBlockEntityWithoutLevelRenderer implements IBlockEntityWithoutLevelRenderer {

    /**Difficult to explain the significance here, but the harpoon can exist without a harpoon entity
     * and needs this to render, similar to tridents
     * */
    @Inject(method = "renderByItem", at = @At(value = "TAIL"))
    public void roundabout$render2(ItemStack $$0, ItemDisplayContext $$1, PoseStack $$2, MultiBufferSource $$3, int $$4, int $$5, CallbackInfo ci){

        if ($$0.is(ModItems.HARPOON)) {
            ClientUtil.pushPoseAndCooperate($$2,19);

            $$2.scale(1.0F, -1.0F, -1.0F);
            if (ModItemModels.HARPOON_MODEL != null) {
                VertexConsumer vertexconsumer1 = ItemRenderer.getFoilBufferDirect($$3, ModItemModels.HARPOON_MODEL.renderType(ModEntities.HARPOON_TEXTURE), false, $$0.hasFoil());
                ModItemModels.HARPOON_MODEL.renderToBuffer($$2, vertexconsumer1, $$4, $$5, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            ClientUtil.popPoseAndCooperate($$2,19);
        }
        if ($$0.is(ModItems.SNUBNOSE_REVOLVER)) {
            ClientUtil.pushPoseAndCooperate($$2,19);

            $$2.scale(1.0F, -1.0F, -1.0F);
            if (ModItemModels.SNUBNOSE_MODEL != null) {
                VertexConsumer vertexconsumer1 = ItemRenderer.getFoilBufferDirect($$3, ModItemModels.SNUBNOSE_MODEL.renderType(ModEntities.SNUBNOSE_IN_HAND_TEXTURE), false, $$0.hasFoil());
                ModItemModels.SNUBNOSE_MODEL.renderToBuffer($$2, vertexconsumer1, $$4, $$5, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            ClientUtil.popPoseAndCooperate($$2,19);
        }
        if ($$0.is(ModItems.TOMMY_GUN)) {
            ClientUtil.pushPoseAndCooperate($$2,19);

            $$2.scale(1.0F, -1.0F, -1.0F);
            if (ModItemModels.TOMMY_GUN_MODEL != null) {
                VertexConsumer vertexconsumer1 = ItemRenderer.getFoilBufferDirect($$3, ModItemModels.TOMMY_GUN_MODEL.renderType(ModEntities.TOMMY_GUN_IN_HAND_TEXTURE), false, $$0.hasFoil());
                ModItemModels.TOMMY_GUN_MODEL.renderToBuffer($$2, vertexconsumer1, $$4, $$5, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            ClientUtil.popPoseAndCooperate($$2,19);
        }
       if ($$0.is(ModItems.ANUBIS_ITEM)) {
            ClientUtil.pushPoseAndCooperate($$2, 18);

            $$2.scale(1.0F, -1.0F, -1.0F);
            if (ModItemModels.ANUBIS_MODEL != null) {
                VertexConsumer vertexconsumer1 = ItemRenderer.getFoilBufferDirect($$3, ModItemModels.HARPOON_MODEL.renderType(ModEntities.HARPOON_TEXTURE), false, $$0.hasFoil());
                ModItemModels.ANUBIS_MODEL.renderToBuffer($$2, vertexconsumer1, $$4, $$5, 1, 1, 1, 1);
                ClientUtil.popPoseAndCooperate($$2, 18);

            }
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Unique
    @Override
    public void roundabout$bakeHarpoonModel(){
    }
}
