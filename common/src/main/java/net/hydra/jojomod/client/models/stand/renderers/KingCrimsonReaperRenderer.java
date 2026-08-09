package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.KingCrimsonModel;
import net.hydra.jojomod.client.models.stand.KingCrimsonReaperModel;
import net.hydra.jojomod.entity.stand.KingCrimsonEntity;
import net.hydra.jojomod.entity.stand.ReaperKingCrimsonEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class KingCrimsonReaperRenderer extends KingCrimsonBaseRenderer<ReaperKingCrimsonEntity> {

    public KingCrimsonReaperRenderer(EntityRendererProvider.Context context) {
        super(context, new KingCrimsonReaperModel(context.bakeLayer(ModEntityRendererClient.KING_CRIMSON_REAPER_LAYER)));
    }
}
