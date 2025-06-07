package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.stand.MagiciansRedOVAModel;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.stand.MagiciansRedEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class MagiciansRedOVARenderer extends MagiciansRedBaseRenderer<MagiciansRedEntity> {
    public MagiciansRedOVARenderer(EntityRendererProvider.Context context) {
        super(context, new MagiciansRedOVAModel<>(context.bakeLayer(ModEntityRendererClient.MAGICIANS_RED_OVA_LAYER)));
    }
}
