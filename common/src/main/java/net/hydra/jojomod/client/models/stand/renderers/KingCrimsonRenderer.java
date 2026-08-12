package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.KingCrimsonModel;
import net.hydra.jojomod.entity.stand.KingCrimsonEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class KingCrimsonRenderer extends KingCrimsonBaseRenderer<KingCrimsonEntity> {

    public KingCrimsonRenderer(EntityRendererProvider.Context context) {
        super(context, new KingCrimsonModel<>(context.bakeLayer(ModEntityRendererClient.KING_CRIMSON_LAYER)));
    }
}
