package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.stand.CinderellaModel;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.stand.CinderellaEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class CinderellaRenderer extends StandRenderer<CinderellaEntity> {

    private static final ResourceLocation PART_4_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/cinderella/anime.png");
    private static final ResourceLocation MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/cinderella/manga.png");
    private static final ResourceLocation ZOMBIE_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/cinderella/zombie.png");
    private static final ResourceLocation JACK_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/cinderella/jack.png");
    private static final ResourceLocation BUSINESS_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/cinderella/business.png");

    public CinderellaRenderer(EntityRendererProvider.Context context) {
        super(context, new CinderellaModel<>(context.bakeLayer(ModEntityRendererClient.CINDERELLA_LAYER)), 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(CinderellaEntity entity) {
        switch (entity.getSkin())
        {
            case (CinderellaEntity.MANGA_SKIN):
                return MANGA_SKIN;
            case (CinderellaEntity.ZOMBIE_SKIN):
                return ZOMBIE_SKIN;
            case (CinderellaEntity.JACK_SKIN):
                return JACK_SKIN;
            case (CinderellaEntity.BUSINESS_SKIN):
                return BUSINESS_SKIN;
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
