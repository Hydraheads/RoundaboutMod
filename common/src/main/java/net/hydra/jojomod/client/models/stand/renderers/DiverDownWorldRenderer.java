package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.DiverDownWorldModel;
import net.hydra.jojomod.entity.stand.DiverDownEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class DiverDownWorldRenderer extends DiverDownBaseRenderer {
    public DiverDownWorldRenderer(EntityRendererProvider.Context context) {
        super(context, new DiverDownWorldModel<>(context.bakeLayer(ModEntityRendererClient.DIVER_DOWN_WORLD_LAYER)), 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(DiverDownEntity entity) {
        return WORLD_DIVER;
    }
}