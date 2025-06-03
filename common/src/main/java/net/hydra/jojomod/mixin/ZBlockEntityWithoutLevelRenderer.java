package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.access.IBlockEntityWithoutLevelRenderer;
import net.hydra.jojomod.client.ModItemModels;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.client.models.projectile.HarpoonModel;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class ZBlockEntityWithoutLevelRenderer implements IBlockEntityWithoutLevelRenderer {

    @Unique
    private HarpoonModel roundabout$harpoonModel;

    @Shadow
    @Final
    private EntityModelSet entityModelSet;

    @Unique
    @Override
    public void roundabout$bakeHarpoonModel(){
    }
    @Inject(method = "onResourceManagerReload", at = @At(value = "TAIL"))
    public void roundabout$render(ResourceManager $$0, CallbackInfo ci){
    }
    @Inject(method = "renderByItem", at = @At(value = "TAIL"))
    public void roundabout$render2(ItemStack $$0, ItemDisplayContext $$1, PoseStack $$2, MultiBufferSource $$3, int $$4, int $$5, CallbackInfo ci){

        if ($$0.is(ModItems.HARPOON)) {
            $$2.pushPose();

            $$2.scale(1.0F, -1.0F, -1.0F);
            if (ModItemModels.HARPOON_MODEL != null) {
                VertexConsumer vertexconsumer1 = ItemRenderer.getFoilBufferDirect($$3, ModItemModels.HARPOON_MODEL.renderType(ModEntities.HARPOON_TEXTURE), false, $$0.hasFoil());
                ModItemModels.HARPOON_MODEL.renderToBuffer($$2, vertexconsumer1, $$4, $$5, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            $$2.popPose();
        }
    }
}
