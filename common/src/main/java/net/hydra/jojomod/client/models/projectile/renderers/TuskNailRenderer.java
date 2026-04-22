package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.projectile.RattDartModel;
import net.hydra.jojomod.client.models.projectile.Tusk1NailModel;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.RattDartEntity;
import net.hydra.jojomod.entity.projectile.TuskNailEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TuskNailRenderer extends EntityRenderer<TuskNailEntity> {
    private final Tusk1NailModel model;

    public TuskNailRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.model = new Tusk1NailModel($$0.bakeLayer(ModEntityRendererClient.TUSK1_NAIL_MODEL));
    }

    private static final ResourceLocation ACT_1 = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/tusk_one.png");
    private static final ResourceLocation ACT_2 = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/tusk_two.png");

    @Override
    public ResourceLocation getTextureLocation(TuskNailEntity tne) {
        if (tne.getAct() == 1) {
            return ACT_1;
        }
        return ACT_2;
    }

    public void render(TuskNailEntity $$0, float $$1, float $$2, PoseStack poseStack, MultiBufferSource $$4, int $$5) {
        VertexConsumer $$6 = ItemRenderer.getFoilBufferDirect($$4, this.model.renderType(this.getTextureLocation($$0)), false, false);// $$0.isFoil());
        poseStack.pushPose();
        poseStack.scale(1.6F,1.6F,1.6F);
        poseStack.translate(0,-1.3,0);
        this.model.setupAnim($$0,$$0.tickCount+$$2);
        this.model.renderToBuffer(poseStack, $$6, $$5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render($$0, $$1, $$2, poseStack, $$4, $$5);
    }

}
