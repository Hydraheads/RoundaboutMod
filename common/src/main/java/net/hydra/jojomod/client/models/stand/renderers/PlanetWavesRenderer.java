package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.stand.PlanetWavesModel;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.stand.CinderellaEntity;
import net.hydra.jojomod.entity.stand.PlanetWavesEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class PlanetWavesRenderer extends StandRenderer<PlanetWavesEntity> {

    private static final ResourceLocation PART_6_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/planet_waves/anime.png");
    private static final ResourceLocation MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/planet_waves/manga.png");
    private static final ResourceLocation BLUE_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/planet_waves/blue.png");

    public PlanetWavesRenderer(EntityRendererProvider.Context context) {
        super(context, new PlanetWavesModel<>(context.bakeLayer(ModEntityRendererClient.PLANET_WAVES_LAYER)), 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(PlanetWavesEntity entity) {
        switch (entity.getSkin())
        {
            case (PlanetWavesEntity.MANGA_SKIN):
                return MANGA_SKIN;
            case (PlanetWavesEntity.BLUE_SKIN):
                return BLUE_SKIN;
            default:
                return PART_6_SKIN;
        }
    }

    @Override
    public void render(PlanetWavesEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
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
    protected RenderType getRenderType(PlanetWavesEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}

