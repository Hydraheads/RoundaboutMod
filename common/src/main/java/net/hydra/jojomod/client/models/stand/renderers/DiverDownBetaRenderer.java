package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.DiverDownBetaModel;
import net.hydra.jojomod.entity.stand.DiverDownEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class DiverDownBetaRenderer extends DiverDownBaseRenderer {
    public DiverDownBetaRenderer(EntityRendererProvider.Context context) {
        super(context, new DiverDownBetaModel<>(context.bakeLayer(ModEntityRendererClient.DIVER_DOWN_BETA_LAYER)), 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(DiverDownEntity entity) {
        return BETA_DIVER;
    }
}