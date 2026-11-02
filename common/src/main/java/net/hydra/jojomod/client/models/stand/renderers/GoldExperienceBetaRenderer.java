package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.GoldExperienceBetaModel;
import net.hydra.jojomod.entity.stand.GoldExperienceEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class GoldExperienceBetaRenderer extends GoldExperienceBaseRenderer {
    public GoldExperienceBetaRenderer(EntityRendererProvider.Context context) {
        super(context, new GoldExperienceBetaModel<>(context.bakeLayer(ModEntityRendererClient.GOLD_EXPERIENCE_BETA_LAYER)), 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(GoldExperienceEntity entity) {
        return BETA;
    }
}