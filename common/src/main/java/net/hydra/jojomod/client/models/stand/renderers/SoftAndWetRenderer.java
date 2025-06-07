package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.stand.SoftAndWetModel;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.stand.SoftAndWetEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SoftAndWetRenderer extends SoftAndWetBaseRenderer<SoftAndWetEntity> {
    public SoftAndWetRenderer(EntityRendererProvider.Context context) {
        super(context, new SoftAndWetModel<>(context.bakeLayer(ModEntityRendererClient.SOFT_AND_WET_LAYER)));
    }
}

