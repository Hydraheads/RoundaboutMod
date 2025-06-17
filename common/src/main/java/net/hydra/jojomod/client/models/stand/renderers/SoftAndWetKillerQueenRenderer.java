package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.KillerQueenModel;
import net.hydra.jojomod.client.models.stand.SoftAndWetKillerQueenModel;
import net.hydra.jojomod.client.models.stand.SoftAndWetKingModel;
import net.hydra.jojomod.entity.stand.SoftAndWetEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SoftAndWetKillerQueenRenderer extends SoftAndWetBaseRenderer<SoftAndWetEntity>{
    public SoftAndWetKillerQueenRenderer(EntityRendererProvider.Context context) {
        super(context, new SoftAndWetKillerQueenModel<>(context.bakeLayer(ModEntityRendererClient.SOFT_AND_WET_KILLER_QUEEN_LAYER)));
    }
}
