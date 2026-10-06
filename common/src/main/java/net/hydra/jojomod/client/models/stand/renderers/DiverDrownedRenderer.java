package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.DiverDrownedModel;
import net.hydra.jojomod.entity.stand.DiverDownEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class DiverDrownedRenderer extends DiverDownBaseRenderer {
    public DiverDrownedRenderer(EntityRendererProvider.Context context) {
        super(context, new DiverDrownedModel<>(context.bakeLayer(ModEntityRendererClient.DIVER_DROWNED_LAYER)), 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(DiverDownEntity entity) {
        return DIVER_DROWNED;
    }
}