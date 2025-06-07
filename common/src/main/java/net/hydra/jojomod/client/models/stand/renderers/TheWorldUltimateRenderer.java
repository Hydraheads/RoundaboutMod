package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.stand.TheWorldModel;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TheWorldUltimateRenderer extends TheWorldBaseRenderer<TheWorldEntity>{

    public TheWorldUltimateRenderer(EntityRendererProvider.Context context) {
        super(context, new TheWorldModel<>(context.bakeLayer(ModEntityRendererClient.THE_WORLD_ULTIMATE_LAYER)));
    }
}
