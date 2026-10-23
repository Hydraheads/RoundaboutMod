package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.TrailRenderer;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.projectile.RattDartModel;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.RattDartEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class RattDartRenderer extends EntityRenderer<RattDartEntity> {
    private final RattDartModel model;

    public RattDartRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.model = new RattDartModel($$0.bakeLayer(ModEntityRendererClient.RATT_DART_LAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(RattDartEntity rattDartEntity) {return ModEntities.RATT_DART_TEXTURE;}

    public void render(RattDartEntity $$0, float $$1, float partialTicks, PoseStack poseStack, MultiBufferSource $$4, int $$5) {
        if (!ClientUtil.getScreenFreeze()) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, $$0.yRotO, $$0.getYRot()) - 90.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, $$0.xRotO, $$0.getXRot()) + 90.0F));
            poseStack.scale(1.1f, 1.1f, 1.1f);
            VertexConsumer $$6 = ItemRenderer.getFoilBufferDirect($$4, this.model.renderType(this.getTextureLocation($$0)), false, false);// $$0.isFoil());
            this.model.renderToBuffer(poseStack, $$6, $$5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
            super.render($$0, $$1, partialTicks, poseStack, $$4, $$5);

            TrailRenderer.Settings settings = TrailRenderer.Settings.RATT;
            /// bullet trail
            Vec3 anchor = $$0.getPosition(partialTicks);
            List<Vec3> points = $$0.getTrailPoints();
            if (points != null && points.size() >= 2) {
                poseStack.pushPose();
                TrailRenderer.drawTrailRaw(
                        anchor,
                        points,
                        poseStack.last(),
                        $$5,
                        partialTicks,
                        settings,
                        $$4
                );
                poseStack.popPose();
            }
        }
    }

}
