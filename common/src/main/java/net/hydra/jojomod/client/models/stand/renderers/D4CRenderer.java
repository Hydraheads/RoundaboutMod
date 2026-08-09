package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.D4CModel;
import net.hydra.jojomod.client.models.stand.KingCrimsonModel;
import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.entity.stand.KingCrimsonEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class D4CRenderer extends D4CBaseRenderer<D4CEntity> {

    public D4CRenderer(EntityRendererProvider.Context context) {
        super(context, new D4CModel(context.bakeLayer(ModEntityRendererClient.D4C_LAYER)));
    }
}
