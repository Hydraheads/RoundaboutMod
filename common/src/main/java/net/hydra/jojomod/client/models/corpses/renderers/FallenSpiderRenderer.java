package net.hydra.jojomod.client.models.corpses.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.corpses.FallenPhantom;
import net.hydra.jojomod.entity.corpses.FallenSpider;
import net.hydra.jojomod.client.models.corpses.FallenSpiderModel;
import net.hydra.jojomod.entity.corpses.FallenZombie;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Spider;

public class FallenSpiderRenderer<T extends Spider> extends MobRenderer<FallenSpider, FallenSpiderModel<FallenSpider>> {
    private static final ResourceLocation SPIDER_LOCATION = new ResourceLocation("textures/entity/spider/spider.png");
    private static final ResourceLocation FALLEN_SPIDER_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_spider.png");
    private static final ResourceLocation FALLEN_SPIDER_LOCATION_2 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_spider_holes.png");
    private static final ResourceLocation FALLEN_SPIDER_LOCATION_B = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_spider_blue.png");
    private static final ResourceLocation FALLEN_SPIDER_LOCATION_R = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_spider_red.png");
    private static final ResourceLocation FALLEN_SPIDER_LOCATION_G = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_spider_green.png");


    @Override
    public ResourceLocation getTextureLocation(FallenSpider var1) {
        if (var1.getTurned()){
            if (var1.getActivated()){
                byte bt = var1.getJusticeTeamColor();
                if (bt == 1){
                    return FALLEN_SPIDER_LOCATION_B;
                } else if (bt ==2){
                    return FALLEN_SPIDER_LOCATION_R;
                } else if (bt==3){
                    return FALLEN_SPIDER_LOCATION_G;
                } else if (bt==4){
                    return SPIDER_LOCATION;
                }
            }
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

    @Override
    protected void setupRotations(FallenSpider FM, PoseStack pose, float $$2, float $$3, float $$4) {
        super.setupRotations(FM,pose,$$2,$$3,$$4);
        int tickTock = FM.ticksThroughPhases;
        if (FM.getPhasesFull()){
            tickTock = 10;
            FM.ticksThroughPhases = 10;
        }
        float yes = Math.min(10, tickTock + $$4);
        if (FM.getActivated()) {
            yes = Math.max(0,tickTock- $$4);
        }
        float $$5 = (yes /10);
            pose.mulPose(Axis.XP.rotationDegrees($$5 * 180));
            pose.translate(0, -$$5 * (1 * FM.getBbHeight()), 0);
    }

}
