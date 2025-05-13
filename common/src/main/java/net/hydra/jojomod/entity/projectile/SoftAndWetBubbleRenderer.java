package net.hydra.jojomod.entity.projectile;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class SoftAndWetBubbleRenderer extends EntityRenderer<SoftAndWetBubbleEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/bubble_plunder.png");

    private final float scale;

    public SoftAndWetBubbleRenderer(EntityRendererProvider.Context context, float scale) {
        super(context);
        this.scale = scale;
    }

    public SoftAndWetBubbleRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.scale = 1f;
    }

    @Override
    public void render(SoftAndWetBubbleEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
            poseStack.pushPose();

            // Orient the texture
            poseStack.scale(this.scale, this.scale, this.scale);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            poseStack.translate(0, entity.getBbHeight() / 2, 0);

            // Draw flat quad here
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));
            Matrix4f matrix = poseStack.last().pose();
            Vector3f normal = Minecraft.getInstance().gameRenderer.getMainCamera().getLookVector();
            normal.normalize();

            Vector3f coursecorrect = new Vector3f(0.577f, 0.577f, 0.577f);
            if (normal.y > 0){
               coursecorrect = new Vector3f( 0.01f, 1f, 0.01f);
            }

            float size = (float) Math.min(0.24, (((float)entity.tickCount)+partialTicks)*0.024f); // Adjust to your needs
            vertexConsumer.vertex(matrix, -size, -size, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
            vertexConsumer.vertex(matrix, size, -size, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
            vertexConsumer.vertex(matrix, size, size, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
            vertexConsumer.vertex(matrix, -size, size, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();

            poseStack.popPose();
            super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(SoftAndWetBubbleEntity entity) {
        return TEXTURE;
    }
}

