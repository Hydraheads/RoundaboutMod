package net.hydra.jojomod.client.models.mobs.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.mobs.FalconModel;
import net.hydra.jojomod.entity.mobs.FalconEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FalconRenderer<T extends FalconEntity>
        extends MobRenderer<T, FalconModel<T>> {

    private static final ResourceLocation PET_SHOP =
            new ResourceLocation(Roundabout.MOD_ID,"textures/entity/falcon/pet_shop.png");
    private static final ResourceLocation APLOMADO =
            new ResourceLocation(Roundabout.MOD_ID,"textures/entity/falcon/aplomado.png");
    private static final ResourceLocation GERIFALTE =
            new ResourceLocation(Roundabout.MOD_ID,"textures/entity/falcon/gerifalte.png");
    private static final ResourceLocation CARACARA =
            new ResourceLocation(Roundabout.MOD_ID,"textures/entity/falcon/caracara.png");

    public FalconRenderer(EntityRendererProvider.Context context) {
        super(context,new FalconModel<>(context.bakeLayer(ModEntityRendererClient.FALCON_LAYER)),0.125F);
    }

    @Override
    public  ResourceLocation getTextureLocation(T falcon) {
        return getVariantTexture(falcon.getVariant());
    }

    public static ResourceLocation getVariantTexture(FalconEntity.Variant variant) {
        switch (variant) {
            case APLOMADO -> {return APLOMADO;}
            case GERIFALTE -> {return GERIFALTE;}
            case CARACARA -> {return CARACARA;}
            case PET_SHOP -> {return PET_SHOP;}
        }

        return PET_SHOP;
    }

    @Override
    public void render(T entity, float $$1, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, $$1, partialTicks, poseStack, bufferSource, packedLight);
    }
}
