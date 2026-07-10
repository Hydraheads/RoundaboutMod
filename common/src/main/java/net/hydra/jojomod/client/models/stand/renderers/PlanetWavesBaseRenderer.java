package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.PlanetWavesModel;
import net.hydra.jojomod.client.models.stand.PlanetWavesSpartaModel;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.PlanetWavesEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class PlanetWavesBaseRenderer<M extends StandEntity> extends StandRenderer<PlanetWavesEntity> {

    private static final ResourceLocation PART_6_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/planet_waves/anime.png");
    private static final ResourceLocation MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/planet_waves/manga.png");
    private static final ResourceLocation BLUE_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/planet_waves/blue.png");
    private static final ResourceLocation PURPLE_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/planet_waves/purple.png");
    private static final ResourceLocation GREEN_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/planet_waves/green.png");
    private static final ResourceLocation OCEAN_WAVES = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/planet_waves/ocean_waves.png");
    private static final ResourceLocation SYMPHONY_WAVES = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/planet_waves/symphony_waves.png");
    private static final ResourceLocation SPARTA = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/planet_waves/sparta.png");
    private static final ResourceLocation SPARTA2 = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/planet_waves/sparta2.png");

    private final StandModel<PlanetWavesEntity> baseModel;
    private final StandModel<PlanetWavesEntity> spartaModel;

    public PlanetWavesBaseRenderer(EntityRendererProvider.Context context, StandModel<PlanetWavesEntity> entityModel, float f) {
        super(context, entityModel, f);
        this.baseModel = entityModel;
        this.spartaModel = new PlanetWavesSpartaModel<>(context.bakeLayer(ModEntityRendererClient.PLANET_WAVES_SPARTA_LAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(PlanetWavesEntity entity) {
        switch (entity.getSkin()) {
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
            case (PlanetWavesEntity.SPARTA):
                return SPARTA;
            case (PlanetWavesEntity.SPARTA2):
                return SPARTA2;
            default:
                return PART_6_SKIN;
        }
    }

    @Override
    public void render(PlanetWavesEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        byte skin = mobEntity.getSkin();
        if (skin == PlanetWavesEntity.SPARTA || skin == PlanetWavesEntity.SPARTA2) {
            this.model = spartaModel;
        } else {
            this.model = baseModel;
        }

        float factor = 0.5F + (mobEntity.getSizePercent() / 2);

        matrixStack.pushPose();
        if (mobEntity.isBaby()) {
            matrixStack.scale(0.5f * factor, 0.5f * factor, 0.5f * factor);
        } else {
            matrixStack.scale(0.8f * factor, 0.8f * factor, 0.8f * factor);
        }

        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);

        matrixStack.popPose();
    }
}