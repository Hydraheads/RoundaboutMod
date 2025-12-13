package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class BloodSplatterRenderer extends EntityRenderer<BloodSplatterEntity> {
    private static final ResourceLocation ITEM_1 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/projectile/blood_1.png");
    private static final ResourceLocation ITEM_2 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/projectile/blood_2.png");
    private static final ResourceLocation ITEM_3 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/projectile/blood_3.png");
    private static final ResourceLocation ITEM_4 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/projectile/blood_4.png");
    private static final ResourceLocation ITEM_5 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/projectile/blood_5.png");
    private static final ResourceLocation ITEM_6 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/projectile/blood_6.png");

    private final float scale;

    public BloodSplatterRenderer(EntityRendererProvider.Context context, float scale) {
        super(context);
        this.scale = scale;
    }

    public BloodSplatterRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.scale = 1f;
    }

    @Override
    public void render(BloodSplatterEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (((TimeStop)entity.level()).inTimeStopRange(entity)){
            partialTicks = 0;
        }
        if (entity != null) {
            poseStack.pushPose();

            // Orient the texture
            poseStack.scale(this.scale, this.scale, this.scale);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            poseStack.translate(0, entity.getBbWidth()/2, 0);

            // Draw flat quad here
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
            Matrix4f matrix = poseStack.last().pose();
            Vector3f normal = Minecraft.getInstance().gameRenderer.getMainCamera().getLookVector();
            normal.normalize();

            /**This one is good*/
            Vector3f coursecorrect = new Vector3f(0.577f, 0.577f, 0.577f);
            if (normal.y > 0) {
                /**This one needs serial fixing*/
                coursecorrect = new Vector3f(0.01f, 1f, 0.01f);
                if (normal.y > 0.95) {
                    coursecorrect = new Vector3f(-0.577f, -0.577f, -0.577f);
                }
            }


            float size = 0.4f; // Adjust to your needs

            vertexConsumer.vertex(matrix, -size, -size, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
            vertexConsumer.vertex(matrix, size, -size, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
            vertexConsumer.vertex(matrix, size, size, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
            vertexConsumer.vertex(matrix, -size, size, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();


            poseStack.popPose();
            super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(BloodSplatterEntity entity) {
        int div = entity.tickCount;
        return switch (div) {
            case 0,1 -> ITEM_1;
            case 2,3 -> ITEM_2;
            case 4,5 -> ITEM_3;
            case 6,7 -> ITEM_4;
            case 8,9 -> ITEM_5;
            default -> ITEM_6;
        };
    }
}