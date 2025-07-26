package net.hydra.jojomod.client.models.corpses.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.corpses.FallenPhantomModel;
import net.hydra.jojomod.entity.corpses.FallenPhantom;
import net.hydra.jojomod.entity.corpses.FallenSpider;
import net.hydra.jojomod.entity.corpses.FallenZombie;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.phys.Vec3;

public class FallenPhantomRenderer<T extends Phantom> extends MobRenderer<FallenPhantom, FallenPhantomModel<FallenPhantom>> {
    private static final ResourceLocation PHANTOM_LOCATION = new ResourceLocation("textures/entity/phantom.png");
    private static final ResourceLocation FALLEN_PHANTOM_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_phantom.png");
    private static final ResourceLocation FALLEN_PHANTOM_LOCATION_R = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_phantom_red.png");
    private static final ResourceLocation FALLEN_PHANTOM_LOCATION_G = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_phantom_green.png");
    private static final ResourceLocation FALLEN_PHANTOM_LOCATION_B = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_phantom_blue.png");
    private static final ResourceLocation FALLEN_PHANTOM_LOCATION_2 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_phantom_holes.png");


    @Override
    public Vec3 getRenderOffset(FallenPhantom $$0, float $$1) {
        return new Vec3(0,-1.2,0);
    }

    @Override
    public ResourceLocation getTextureLocation(FallenPhantom var1) {
        if (var1.getTurned()){
            if (var1.getActivated()){
                byte bt = var1.getJusticeTeamColor();
                if (bt == 1){
                    return FALLEN_PHANTOM_LOCATION_B;
                } else if (bt ==2){
                    return FALLEN_PHANTOM_LOCATION_R;
                } else if (bt==3){
                    return FALLEN_PHANTOM_LOCATION_G;
                } else if (bt==4){
                    return PHANTOM_LOCATION;
                }
            }
            return FALLEN_PHANTOM_LOCATION_2;

        } else {
            return FALLEN_PHANTOM_LOCATION;
        }


    }

    public FallenPhantomRenderer(EntityRendererProvider.Context $$0) {
        this($$0, ModelLayers.PHANTOM);
    }

    public FallenPhantomRenderer(EntityRendererProvider.Context $$0, ModelLayerLocation $$1) {
        super($$0, new FallenPhantomModel<>($$0.bakeLayer($$1)), 0.75F);
    }

    protected float getFlipDegrees(T $$0) {
        return 180.0F;
    }

    @Override
    protected void setupRotations(FallenPhantom FM, PoseStack pose, float $$2, float $$3, float $$4) {
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
            pose.translate(0,-$$5 *(6*FM.getBbHeight()),0);
    }

}
