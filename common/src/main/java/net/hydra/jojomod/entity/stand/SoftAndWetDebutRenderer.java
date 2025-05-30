package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SoftAndWetDebutRenderer extends SoftAndWetBaseRenderer<SoftAndWetEntity>{
    public SoftAndWetDebutRenderer(EntityRendererProvider.Context context) {
        super(context, new SoftAndWetKingModel<>(context.bakeLayer(ModEntityRendererClient.SOFT_AND_WET_DEBUT_LAYER)));
    }
}
