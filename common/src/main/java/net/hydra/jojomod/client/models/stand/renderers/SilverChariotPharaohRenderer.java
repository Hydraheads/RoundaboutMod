package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.SilverChariotPharaohModel;
import net.hydra.jojomod.entity.stand.SilverChariotEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SilverChariotPharaohRenderer extends SilverChariotBaseRenderer<SilverChariotEntity> {
    public SilverChariotPharaohRenderer(EntityRendererProvider.Context context) {
        super(context, new SilverChariotPharaohModel<>(context.bakeLayer(ModEntityRendererClient.SILVER_CHARIOT_PHARAOH_LAYER)));
    }
}
