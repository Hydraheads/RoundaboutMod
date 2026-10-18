package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.CMoonModel;
import net.hydra.jojomod.entity.stand.CMoonEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class CMoonRenderer extends CMoonBaseRenderer<CMoonEntity> {

    public CMoonRenderer(EntityRendererProvider.Context context) {
        super(context, new CMoonModel<>(context.bakeLayer(ModEntityRendererClient.CMOON_LAYER)));
    }
}