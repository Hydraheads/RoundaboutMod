package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class MagiciansRedOVARenderer extends MagiciansRedBaseRenderer<MagiciansRedEntity> {
    public MagiciansRedOVARenderer(EntityRendererProvider.Context context) {
        super(context, new MagiciansRedOVAModel<>(context.bakeLayer(ModEntityRendererClient.MAGICIANS_RED_OVA_LAYER)));
    }
}
