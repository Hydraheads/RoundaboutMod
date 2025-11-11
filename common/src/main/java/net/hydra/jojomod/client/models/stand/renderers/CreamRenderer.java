package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.CreamModel;
import net.hydra.jojomod.client.models.stand.StarPlatinumModel;
import net.hydra.jojomod.entity.stand.CreamEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class CreamRenderer<T extends StandEntity> extends CreamBaseRenderer<CreamEntity> {
    public CreamRenderer(EntityRendererProvider.Context context) {
        super(context, new CreamModel<>(context.bakeLayer(ModEntityRendererClient.CREAM_LAYER)),0f);
    }
}
