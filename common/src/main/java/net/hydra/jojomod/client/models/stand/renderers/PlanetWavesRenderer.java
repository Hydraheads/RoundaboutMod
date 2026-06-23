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
    private static final ResourceLocation PURPLE_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/planet_waves/purple.png");
    private static final ResourceLocation GREEN_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/planet_waves/green.png");
    private static final ResourceLocation OCEAN_WAVES = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/planet_waves/ocean_waves.png");
    private static final ResourceLocation SYMPHONY_WAVES = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/planet_waves/symphony_waves.png");

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
            case (PlanetWavesEntity.PURPLE_SKIN):
                return PURPLE_SKIN;
            case (PlanetWavesEntity.GREEN_SKIN):
                return GREEN_SKIN;
            case (PlanetWavesEntity.OCEAN_WAVES):
                return OCEAN_WAVES;
            case (PlanetWavesEntity.SYMPHONY_WAVES):
                return SYMPHONY_WAVES;
            default:
                return PART_6_SKIN;
        }
    }

    @Override
    public void render(PlanetWavesEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        float factor = 0.5F + (mobEntity.getSizePercent()/2);

        matrixStack.pushPose();

        float rotX = mobEntity.getStandRotationX();
        if (rotX != 0.0F) {
            matrixStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(rotX));
        }

        if (mobEntity.isBaby()) {
            matrixStack.scale(0.5f*factor, 0.5f*factor, 0.5f*factor);
        } else {
            matrixStack.scale(0.8f * factor, 0.8f * factor, 0.8f * factor);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);

        matrixStack.popPose();
    }

    @Nullable
    @Override
    protected RenderType getRenderType(PlanetWavesEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}

