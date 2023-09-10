package net.hydra.jojomod.entity.stands;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hydra.jojomod.entity.StandEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;


@Environment(value= EnvType.CLIENT)
public class StandStarPlatinumRenderer
        extends StandEntityRenderer {

    public StandStarPlatinumRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager);
    }
}
