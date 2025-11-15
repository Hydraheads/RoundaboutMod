package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.projectile.RattDartModel;
import net.hydra.jojomod.client.models.projectile.RoundaboutBulletModel;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.RoundaboutBulletEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RoundaboutBulletEntityRenderer extends EntityRenderer<RoundaboutBulletEntity> {
    private final RoundaboutBulletModel model;

    public RoundaboutBulletEntityRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.model = new RoundaboutBulletModel<>($$0.bakeLayer(ModEntityRendererClient.ROUNDABOUT_BULLET_LAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(RoundaboutBulletEntity roundaboutBulletEntity) {
        return ModEntities.ROUNDABOUT_BULLET_ENTITY_TEXTURE;
    }

    public void render(RoundaboutBulletEntity $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
        if (!ClientUtil.getScreenFreeze()) {
            $$3.pushPose();
            $$3.mulPose(Axis.YP.rotationDegrees(Mth.lerp($$2, $$0.yRotO, $$0.getYRot()) - 0.0F));
            $$3.mulPose(Axis.XP.rotationDegrees(-Mth.lerp($$2, $$0.xRotO, $$0.getXRot())));
            $$3.scale(1.1f, 1.1f, 1.1f);
            VertexConsumer $$6 = ItemRenderer.getFoilBufferDirect($$4, this.model.renderType(this.getTextureLocation($$0)), false, false);
            this.model.renderToBuffer($$3, $$6, $$5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            $$3.popPose();
            super.render($$0, $$1, $$2, $$3, $$4, $$5);
        }
    }

}
