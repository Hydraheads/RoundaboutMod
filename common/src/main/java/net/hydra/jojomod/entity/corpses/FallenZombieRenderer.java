package net.hydra.jojomod.entity.corpses;

import net.hydra.jojomod.Roundabout;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class FallenZombieRenderer extends HumanoidMobRenderer<FallenZombie, FallenZombieModel<FallenZombie>> {
    private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation("textures/entity/zombie/zombie.png");
    private static final ResourceLocation FALLEN_ZOMBIE_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_zombie.png");

    public FallenZombieRenderer(EntityRendererProvider.Context $$0) {
        this($$0, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_INNER_ARMOR, ModelLayers.ZOMBIE_OUTER_ARMOR);
    }

    @Override
    public ResourceLocation getTextureLocation(FallenZombie var1) {
        return FALLEN_ZOMBIE_LOCATION;
    }

    protected FallenZombieRenderer(EntityRendererProvider.Context $$0, FallenZombieModel<FallenZombie> $$1,
                                   FallenZombieModel<FallenZombie> $$2, FallenZombieModel<FallenZombie> $$3) {
        super($$0, $$1, 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, $$2, $$3, $$0.getModelManager()));
    }

    public FallenZombieRenderer(EntityRendererProvider.Context $$0, ModelLayerLocation $$1, ModelLayerLocation $$2, ModelLayerLocation $$3) {
        this($$0, new FallenZombieModel<>($$0.bakeLayer($$1)), new FallenZombieModel<>($$0.bakeLayer($$2)), new FallenZombieModel<>($$0.bakeLayer($$3)));
    }
}
