package net.hydra.jojomod.client.models.paintings;

import net.hydra.jojomod.Roundabout;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class MonaLisaPaintingRenderer extends CustomPaintingRenderer {
    public MonaLisaPaintingRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }

   @Override
    public int getWidth(){
        return 16;
    }
    @Override
    public int getHeight(){
        return 32;
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Roundabout.MOD_ID,"textures/entity/painting/mona_lisa.png");
    }
}
