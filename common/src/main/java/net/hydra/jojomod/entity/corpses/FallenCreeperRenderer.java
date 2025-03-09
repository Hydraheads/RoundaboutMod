package net.hydra.jojomod.entity.corpses;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CreeperPowerLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Creeper;

public class FallenCreeperRenderer extends MobRenderer<FallenCreeper, FallenCreeperModel<FallenCreeper>> {
    private static final ResourceLocation CREEPER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper.png");
    private static final ResourceLocation FALLEN_CREEPER_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_creeper.png");
    private static final ResourceLocation FALLEN_CREEPER_LOCATION_2 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_creeper_holes.png");
    private static final ResourceLocation FALLEN_CREEPER_LOCATION_B = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_creeper_blue.png");
    private static final ResourceLocation FALLEN_CREEPER_LOCATION_R = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_creeper_red.png");
    private static final ResourceLocation FALLEN_CREEPER_LOCATION_G = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_creeper_green.png");

    public FallenCreeperRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new FallenCreeperModel<>($$0.bakeLayer(ModelLayers.CREEPER)), 0.5F);
        this.addLayer(new FallenCreeperPowerLayer(this, $$0.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(FallenCreeper var1) {
        if (var1.getTurned()){
            if (var1.getActivated()){
                byte bt = var1.getJusticeTeamColor();
                if (bt == 1){
                    return FALLEN_CREEPER_LOCATION_B;
                } else if (bt ==2){
                    return FALLEN_CREEPER_LOCATION_R;
                } else if (bt==3){
                    return FALLEN_CREEPER_LOCATION_G;
                } else if (bt == 4){
                    return CREEPER_LOCATION;
                }
            }
            return FALLEN_CREEPER_LOCATION_2;
        } else {
            return FALLEN_CREEPER_LOCATION;
        }
    }

    protected void scale(FallenCreeper $$0, PoseStack $$1, float $$2) {
        float $$3 = $$0.getSwelling($$2);
        float $$4 = 1.0F + Mth.sin($$3 * 100.0F) * $$3 * 0.01F;
        $$3 = Mth.clamp($$3, 0.0F, 1.0F);
        $$3 *= $$3;
        $$3 *= $$3;
        float $$5 = (1.0F + $$3 * 0.4F) * $$4;
        float $$6 = (1.0F + $$3 * 0.1F) / $$4;
        $$1.scale($$5, $$6, $$5);
    }

    protected float getWhiteOverlayProgress(FallenCreeper $$0, float $$1) {
        float $$2 = $$0.getSwelling($$1);
        return (int)($$2 * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp($$2, 0.5F, 1.0F);
    }
}
