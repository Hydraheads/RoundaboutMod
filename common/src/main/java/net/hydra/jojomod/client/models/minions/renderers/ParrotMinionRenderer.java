package net.hydra.jojomod.client.models.minions.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.minions.ParrotMinionModel;
import net.hydra.jojomod.entity.zombie_minion.AxolotlMinion;
import net.hydra.jojomod.entity.zombie_minion.BaseMinion;
import net.hydra.jojomod.entity.zombie_minion.ParrotMinion;
import net.hydra.jojomod.entity.zombie_minion.ParrotMinion;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Parrot;

public class ParrotMinionRenderer extends MobRenderer<ParrotMinion, ParrotMinionModel> {
    private static final ResourceLocation VINDICATOR = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/minions/parrot.png");

    public ParrotMinionRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new ParrotMinionModel($$0.bakeLayer(ModEntityRendererClient.PARROT_MINION_LAYER)), 0.5F);
        this.addLayer(new ChimeraHeadLayer<>($$0, this));
    }
    @Override
    protected void scale(ParrotMinion $$0, PoseStack $$1, float $$2) {
        $$1.scale(1F, 1F, 1F);
        if ($$0.clientDigProg > 0 && $$0.getDigProg() <= -1){
            float perc = 1f-(($$0.clientDigProg-$$2)/((float) BaseMinion.digProgTick));
            $$1.scale(1F, perc, 1F);
        }
    }
    public ResourceLocation getTextureLocation(ParrotMinion $$0) {
        return VINDICATOR;
    }

    @Override
    public void render(ParrotMinion minion, float $$1, float partialTicks, PoseStack stack,
                       MultiBufferSource bufferSource, int packedLight) {
        getModel().head.visible = false;
        super.render(minion, $$1, partialTicks, stack, bufferSource, packedLight);
    }
    @Override
    public boolean shouldRender(ParrotMinion $$0, Frustum $$1, double $$2, double $$3, double $$4) {
        if ($$0.getDiedInSun()){
            return false;
        }
        return super.shouldRender($$0,$$1,$$2,$$3,$$4);
    }

    @Override
    public float getBob(ParrotMinion $$0, float $$1) {
        float $$2 = Mth.lerp($$1, $$0.oFlap, $$0.flap);
        float $$3 = Mth.lerp($$1, $$0.oFlapSpeed, $$0.flapSpeed);
        return (Mth.sin($$2) + 1.0F) * $$3;
    }
}