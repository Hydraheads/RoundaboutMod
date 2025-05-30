package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SoftAndWetKingRenderer extends SoftAndWetBaseRenderer<SoftAndWetEntity>{
    public SoftAndWetKingRenderer(EntityRendererProvider.Context context) {
        super(context, new SoftAndWetKingModel<>(context.bakeLayer(ModEntityRendererClient.SOFT_AND_WET_KING_LAYER)));
    }
}
