package net.hydra.jojomod.entity.stand;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;


@Environment(value= EnvType.CLIENT)
public class StandStarPlatinumRenderer
        extends TheWorldRenderer {

    public StandStarPlatinumRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager);
    }
}
