package net.hydra.jojomod.entity.corpses;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SpiderEyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Spider;

public class FallenSpiderRenderer<T extends Spider> extends MobRenderer<FallenSpider, FallenSpiderModel<FallenSpider>> {
    private static final ResourceLocation FALLEN_SPIDER_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_spider.png");
    private static final ResourceLocation FALLEN_SPIDER_LOCATION_2 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_spider_holes.png");


    @Override
    public ResourceLocation getTextureLocation(FallenSpider var1) {
        if (var1.getTurned()){
            return FALLEN_SPIDER_LOCATION_2;
        } else {
            return FALLEN_SPIDER_LOCATION;
        }
    }

    public FallenSpiderRenderer(EntityRendererProvider.Context $$0) {
        this($$0, ModelLayers.SPIDER);
    }

    public FallenSpiderRenderer(EntityRendererProvider.Context $$0, ModelLayerLocation $$1) {
        super($$0, new FallenSpiderModel<>($$0.bakeLayer($$1)), 0.8F);
    }

    protected float getFlipDegrees(T $$0) {
        return 180.0F;
    }

}
