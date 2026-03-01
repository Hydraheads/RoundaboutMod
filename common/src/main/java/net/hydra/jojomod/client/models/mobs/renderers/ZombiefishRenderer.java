package net.hydra.jojomod.client.models.mobs.renderers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.Zombiefish;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Silverfish;

public class ZombiefishRenderer extends MobRenderer<Zombiefish, SilverfishModel<Zombiefish>> {
    private static final ResourceLocation SILVERFISH_LOCATION = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/zombiefish.png");

    public ZombiefishRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new SilverfishModel<>($$0.bakeLayer(ModEntityRendererClient.ZOMBIEFISH_LAYER)), 0.3F);
    }

    public ZombiefishRenderer(EntityRendererProvider.Context $$0, SilverfishModel<Zombiefish> $$1, float $$2) {
        super($$0, $$1, $$2);
    }

    protected float getFlipDegrees(Zombiefish $$0) {
        return 180.0F;
    }

    public ResourceLocation getTextureLocation(Zombiefish $$0) {
        return SILVERFISH_LOCATION;
    }
}

