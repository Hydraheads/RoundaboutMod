package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TheWorldRenderer extends TheWorldBaseRenderer<TheWorldEntity>{

    public TheWorldRenderer(EntityRendererProvider.Context context) {
        super(context, new TheWorldModel<>(context.bakeLayer(ModEntityRendererClient.THE_WORLD_LAYER)));
    }
}
