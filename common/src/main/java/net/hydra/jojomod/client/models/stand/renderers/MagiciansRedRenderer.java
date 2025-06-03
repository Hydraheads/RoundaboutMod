package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.stand.MagiciansRedModel;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.stand.MagiciansRedEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class MagiciansRedRenderer extends MagiciansRedBaseRenderer<MagiciansRedEntity> {
    public MagiciansRedRenderer(EntityRendererProvider.Context context) {
        super(context, new MagiciansRedModel<>(context.bakeLayer(ModEntityRendererClient.MAGICIANS_RED_LAYER)));
    }
}
