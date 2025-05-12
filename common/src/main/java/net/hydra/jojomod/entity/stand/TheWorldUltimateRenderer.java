package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TheWorldUltimateRenderer extends TheWorldBaseRenderer<TheWorldEntity>{

    public TheWorldUltimateRenderer(EntityRendererProvider.Context context) {
        super(context, new TheWorldModel<>(context.bakeLayer(ModEntityRendererClient.THE_WORLD_ULTIMATE_LAYER)));
    }
}
