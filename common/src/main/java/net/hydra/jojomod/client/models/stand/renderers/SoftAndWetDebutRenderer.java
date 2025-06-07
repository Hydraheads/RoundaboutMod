package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.stand.SoftAndWetKingModel;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.stand.SoftAndWetEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SoftAndWetDebutRenderer extends SoftAndWetBaseRenderer<SoftAndWetEntity>{
    public SoftAndWetDebutRenderer(EntityRendererProvider.Context context) {
        super(context, new SoftAndWetKingModel<>(context.bakeLayer(ModEntityRendererClient.SOFT_AND_WET_DEBUT_LAYER)));
    }
}
