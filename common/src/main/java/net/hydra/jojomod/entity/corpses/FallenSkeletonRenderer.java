package net.hydra.jojomod.entity.corpses;

import net.hydra.jojomod.Roundabout;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class FallenSkeletonRenderer extends HumanoidMobRenderer<FallenSkeleton, FallenSkeletonModel<FallenSkeleton>> {
    private static final ResourceLocation FALLEN_SKELETON_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_skeleton.png");
    private static final ResourceLocation FALLEN_SKELETON_LOCATION_2 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_skeleton_holes.png");

    public FallenSkeletonRenderer(EntityRendererProvider.Context $$0) {
        this($$0, ModelLayers.SKELETON, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
    }

    @Override
    public ResourceLocation getTextureLocation(FallenSkeleton var1) {
        if (var1.getTurned()){
            return FALLEN_SKELETON_LOCATION_2;
        } else {
            return FALLEN_SKELETON_LOCATION;
        }
    }

    protected FallenSkeletonRenderer(EntityRendererProvider.Context $$0, FallenSkeletonModel<FallenSkeleton> $$1,
                                     FallenSkeletonModel<FallenSkeleton> $$2, FallenSkeletonModel<FallenSkeleton> $$3) {
        super($$0, $$1, 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, $$2, $$3, $$0.getModelManager()));
    }

    public FallenSkeletonRenderer(EntityRendererProvider.Context $$0, ModelLayerLocation $$1, ModelLayerLocation $$2, ModelLayerLocation $$3) {
        this($$0, new FallenSkeletonModel<>($$0.bakeLayer($$1)), new FallenSkeletonModel<>($$0.bakeLayer($$2)),
                new FallenSkeletonModel<>($$0.bakeLayer($$3)));
    }
}