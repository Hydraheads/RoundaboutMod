package net.hydra.jojomod.client.models.minions.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.minions.ChickenMinionModel;
import net.hydra.jojomod.entity.zombie_minion.BaseMinion;
import net.hydra.jojomod.entity.zombie_minion.ChickenMinion;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ChickenMinionRenderer extends MobRenderer<ChickenMinion, ChickenMinionModel<ChickenMinion>> {
    private static final ResourceLocation VINDICATOR = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/minions/chicken.png");

    public ChickenMinionRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new ChickenMinionModel<>($$0.bakeLayer(ModEntityRendererClient.CHICKEN_MINION_LAYER)), 0.3F);
        this.addLayer(new ChimeraHeadLayer<>($$0, this));
    }
    @Override
    protected void scale(ChickenMinion $$0, PoseStack $$1, float $$2) {
        $$1.scale(0.9375F, 0.9375F, 0.9375F);
        if ($$0.clientDigProg > 0 && $$0.getDigProg() <= -1){
            float perc = 1f-(($$0.clientDigProg-$$2)/((float) BaseMinion.digProgTick));
            $$1.scale(1F, perc, 1F);
        }
    }
    @Override
    public void render(ChickenMinion minion, float $$1, float partialTicks, PoseStack stack,
                       MultiBufferSource bufferSource, int packedLight) {
        getModel().head.visible = false;
        getModel().beak.visible = false;
        getModel().redThing.visible = false;
        super.render(minion, $$1, partialTicks, stack, bufferSource, packedLight);
    }
    public ResourceLocation getTextureLocation(ChickenMinion $$0) {
        return VINDICATOR;
    }

    protected float getBob(ChickenMinion $$0, float $$1) {
        float $$2 = Mth.lerp($$1, $$0.oFlap, $$0.flap);
        float $$3 = Mth.lerp($$1, $$0.oFlapSpeed, $$0.flapSpeed);
        return (Mth.sin($$2) + 1.0F) * $$3;
    }
    @Override
    public boolean shouldRender(ChickenMinion $$0, Frustum $$1, double $$2, double $$3, double $$4) {
        if ($$0.getDiedInSun()){
            return false;
        }
        return super.shouldRender($$0,$$1,$$2,$$3,$$4);
    }
}