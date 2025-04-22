package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class CinderellaRenderer extends StandRenderer<CinderellaEntity> {

    private static final ResourceLocation PART_4_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/cinderella.png");
    private static final ResourceLocation MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/cinderella_manga.png");

    public CinderellaRenderer(EntityRendererProvider.Context context) {
        super(context, new CinderellaModel<>(context.bakeLayer(ModEntityRendererClient.CINDERELLA_LAYER)), 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(CinderellaEntity entity) {
        switch (entity.getSkin())
        {
            case (CinderellaEntity.MANGA_SKIN):
                return MANGA_SKIN;
            default:
                return PART_4_SKIN;
        }
    }

    @Override
    public void render(CinderellaEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        float factor = 0.5F + (mobEntity.getSizePercent()/2);
        if (mobEntity.isBaby()) {
            matrixStack.scale(0.5f*factor, 0.5f*factor, 0.5f*factor);
        } else {
            matrixStack.scale(0.8f * factor, 0.8f * factor, 0.8f * factor);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(CinderellaEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
