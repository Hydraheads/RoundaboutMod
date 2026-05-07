package net.hydra.jojomod.client.models.projectile.renderers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.projectile.KnifeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class MetallicaKnifeRenderer extends KnifeRenderer {

    private static final ResourceLocation SCALPEL_TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/scalpel.png");

    public MetallicaKnifeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(KnifeEntity entity) {
        return SCALPEL_TEXTURE;
    }
}