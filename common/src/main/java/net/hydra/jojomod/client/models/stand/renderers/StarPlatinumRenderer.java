package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.stand.StarPlatinumModel;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class StarPlatinumRenderer<T extends StandEntity> extends StarPlatinumBaseRenderer<StarPlatinumEntity> {
    public StarPlatinumRenderer(EntityRendererProvider.Context context) {
        super(context, new StarPlatinumModel<>(context.bakeLayer(ModEntityRendererClient.STAR_PLATINUM_LAYER)),0f);
    }
}
