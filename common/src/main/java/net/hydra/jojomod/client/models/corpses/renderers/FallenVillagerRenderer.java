package net.hydra.jojomod.client.models.corpses.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.corpses.FallenSkeleton;
import net.hydra.jojomod.entity.corpses.FallenVillager;
import net.hydra.jojomod.client.models.corpses.FallenVillagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;

public class FallenVillagerRenderer extends MobRenderer<FallenVillager, FallenVillagerModel<FallenVillager>> {
    private static final ResourceLocation FALLEN_VILLAGER_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_villager.png");
    private static final ResourceLocation FALLEN_VILLAGER_LOCATION_2 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_villager_holes.png");
    private static final ResourceLocation FALLEN_VILLAGER_LOCATION_B = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_villager_blue.png");
    private static final ResourceLocation FALLEN_VILLAGER_LOCATION_R = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_villager_red.png");
    private static final ResourceLocation FALLEN_VILLAGER_LOCATION_G = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_villager_green.png");

    private static final ResourceLocation VILLAGER_BASE_SKIN = new ResourceLocation("textures/entity/villager/villager.png");

    public FallenVillagerRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new FallenVillagerModel<>($$0.bakeLayer(ModelLayers.VILLAGER)), 0.5F);
        this.addLayer(new CustomHeadLayer<>(this, $$0.getModelSet(), $$0.getItemInHandRenderer()));
        this.addLayer(new CrossedArmsItemLayer<>(this, $$0.getItemInHandRenderer()));
    }


    @Override
    public ResourceLocation getTextureLocation(FallenVillager var1) {
        if (var1.getTurned()){
            if (var1.getActivated()){
                byte bt = var1.getJusticeTeamColor();
                if (bt == 1){
                    return FALLEN_VILLAGER_LOCATION_B;
                } else if (bt ==2){
                    return FALLEN_VILLAGER_LOCATION_R;
                } else if (bt==3){
                    return FALLEN_VILLAGER_LOCATION_G;
                } else if (bt==4){
                    return VILLAGER_BASE_SKIN;
                }
            }
            return FALLEN_VILLAGER_LOCATION_2;
        } else {
            return FALLEN_VILLAGER_LOCATION;
        }
    }

    protected void scale(FallenVillager $$0, PoseStack $$1, float $$2) {
        float $$3 = 0.9375F;
        if ($$0.isBaby()) {
            $$3 *= 0.5F;
            this.shadowRadius = 0.25F;
        } else {
            this.shadowRadius = 0.5F;
        }

        $$1.scale($$3, $$3, $$3);
    }

    @Override
    protected void setupRotations(FallenVillager FM, PoseStack pose, float $$2, float $$3, float $$4) {
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
        pose.mulPose(Axis.XP.rotationDegrees($$5 * 90));
        pose.translate(0,-$$5*(0.5*FM.getBbHeight()),-($$5*0.15));
    }
}
