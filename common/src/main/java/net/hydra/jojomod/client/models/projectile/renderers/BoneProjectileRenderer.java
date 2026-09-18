package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.projectile.BoneProjectileEntity;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BoneProjectileRenderer extends EntityRenderer<BoneProjectileEntity> {

    public static final ResourceLocation BONE_TEXTURE = new ResourceLocation(
            Roundabout.MOD_ID, "textures/entity/projectile/bone_projectile.png"
    );

    private final float scale;

    public BoneProjectileRenderer(EntityRendererProvider.Context context) {
        this(context, 1.5F);
    }

    public BoneProjectileRenderer(EntityRendererProvider.Context context, float scale) {
        super(context);
        this.scale = scale;
    }

    @Override
    public void render(BoneProjectileEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (!ClientUtil.getScreenFreeze()) {
            if (((TimeStop) entity.level()).inTimeStopRange(entity)) {
                partialTicks = 0;
            }

            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
            poseStack.scale(this.scale, this.scale, this.scale);

            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity)));

            float length = 0.4F;
            float width = 0.25F;

            // make a plus sign
            for (int i = 0; i < 2; i++) {
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

                PoseStack.Pose lastPose = poseStack.last();
                Matrix4f poseMatrix = lastPose.pose();
                Matrix3f normalMatrix = lastPose.normal();

                // Front side
                drawVertex(poseMatrix, normalMatrix, vertexConsumer, -length, -width, 0, 1.0F, 0.0F, packedLight);
                drawVertex(poseMatrix, normalMatrix, vertexConsumer, length, -width, 0, 0.0F, 0.0F, packedLight);
                drawVertex(poseMatrix, normalMatrix, vertexConsumer, length, width, 0, 0.0F, 1.0F, packedLight);
                drawVertex(poseMatrix, normalMatrix, vertexConsumer, -length, width, 0, 1.0F, 1.0F, packedLight);

                // Back side
                drawVertex(poseMatrix, normalMatrix, vertexConsumer, -length, width, 0, 1.0F, 1.0F, packedLight);
                drawVertex(poseMatrix, normalMatrix, vertexConsumer, length, width, 0, 0.0F, 1.0F, packedLight);
                drawVertex(poseMatrix, normalMatrix, vertexConsumer, length, -width, 0, 0.0F, 0.0F, packedLight);
                drawVertex(poseMatrix, normalMatrix, vertexConsumer, -length, -width, 0, 1.0F, 0.0F, packedLight);
            }

            poseStack.popPose();
            super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }

    private void drawVertex(Matrix4f pose, Matrix3f normal, VertexConsumer consumer, float x, float y, float z, float u, float v, int light) {
        consumer.vertex(pose, x, y, z)
                .color(255, 255, 255, 255)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(BoneProjectileEntity entity) {
        return BONE_TEXTURE;
    }
}