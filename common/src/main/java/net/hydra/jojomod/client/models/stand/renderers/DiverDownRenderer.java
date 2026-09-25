package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.DiverDownModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class DiverDownRenderer extends DiverDownBaseRenderer {
    public DiverDownRenderer(EntityRendererProvider.Context context) {
        super(context, new DiverDownModel<>(context.bakeLayer(ModEntityRendererClient.DIVER_DOWN_LAYER)), 0f);
    }
}