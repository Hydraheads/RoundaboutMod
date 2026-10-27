package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.GoldExperienceModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class GoldExperienceRenderer extends GoldExperienceBaseRenderer {
    public GoldExperienceRenderer(EntityRendererProvider.Context context) {
        super(context, new GoldExperienceModel<>(context.bakeLayer(ModEntityRendererClient.GOLD_EXPERIENCE_LAYER)), 0f);
    }
}