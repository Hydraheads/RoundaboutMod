package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class StarPlatinumBaseballRenderer<T extends StandEntity> extends StarPlatinumBaseRenderer<StarPlatinumEntity> {
    public StarPlatinumBaseballRenderer(EntityRendererProvider.Context context) {
        super(context, new StarPlatinumBaseballModel<>(context.bakeLayer(ModEntityRendererClient.STAR_PLATINUM_BASEBALL_LAYER)),0f);
    }

}
