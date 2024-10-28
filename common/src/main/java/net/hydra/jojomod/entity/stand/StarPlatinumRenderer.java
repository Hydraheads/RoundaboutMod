package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class StarPlatinumRenderer<T extends StandEntity> extends StarPlatinumBaseRenderer<StarPlatinumEntity> {
    public StarPlatinumRenderer(EntityRendererProvider.Context context) {
        super(context, new StarPlatinumModel<>(context.bakeLayer(ModEntityRendererClient.STAR_PLATINUM_LAYER)),0f);
    }
}
