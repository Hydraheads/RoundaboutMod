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
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class ChickenMinionRenderer extends MobRenderer<ChickenMinion, ChickenMinionModel<ChickenMinion>> {
    private static final ResourceLocation VINDICATOR = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/minions/villager.png");

    public ChickenMinionRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new ChickenMinionModel<>($$0.bakeLayer(ModEntityRendererClient.CHICKEN_MINION_LAYER)), 0.5F);
        this.addLayer(new ItemInHandLayer<ChickenMinion, ChickenMinionModel<ChickenMinion>>(this, $$0.getItemInHandRenderer()) {
            public void render(PoseStack $$0, MultiBufferSource $$1, int $$2, ChickenMinion $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
                if ($$3.isAggressive()) {
                    super.render($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7, $$8, $$9);
                }
            }
        });
    }
    @Override
    protected void scale(ChickenMinion $$0, PoseStack $$1, float $$2) {
        $$1.scale(0.9375F, 0.9375F, 0.9375F);
        if ($$0.clientDigProg > 0 && $$0.getDigProg() <= -1){
            float perc = 1f-(($$0.clientDigProg-$$2)/((float) BaseMinion.digProgTick));
            $$1.scale(1F, perc, 1F);
        }
    }
    public ResourceLocation getTextureLocation(ChickenMinion $$0) {
        return VINDICATOR;
    }

    @Override
    public boolean shouldRender(ChickenMinion $$0, Frustum $$1, double $$2, double $$3, double $$4) {
        if ($$0.getDiedInSun()){
            return false;
        }
        return super.shouldRender($$0,$$1,$$2,$$3,$$4);
    }
}