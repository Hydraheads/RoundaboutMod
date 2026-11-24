package net.hydra.jojomod.client.models.paintings;

import net.hydra.jojomod.Roundabout;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ZombieHorseMuralRenderer extends CustomPaintingRenderer {
    public ZombieHorseMuralRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }

   @Override
    public int getWidth(){
        return 32;
    }
    @Override
    public int getHeight(){
        return 48;
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Roundabout.MOD_ID,"textures/entity/painting/zombie_horse_mural.png");
    }

    @Override
    public ResourceLocation getTextureBack() {
        return new ResourceLocation(Roundabout.MOD_ID,"textures/entity/painting/empty_back.png");
    }

    @Override
    protected boolean isSpecialPainting(){
        return true;
    }
}
