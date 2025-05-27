package net.hydra.jojomod.entity.substand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.projectile.SoftAndWetBubbleEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.event.index.PlunderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class EncasementBubbleRenderer extends EntityRenderer<EncasementBubbleEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/large_bubble.png");

    private final float scale;
    private final ItemRenderer itemRenderer;

    public EncasementBubbleRenderer(EntityRendererProvider.Context context, float scale) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.scale = scale;
    }

    public EncasementBubbleRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.scale = 2.9f;
    }

    @Override
    public void render(EncasementBubbleEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
                poseStack.pushPose();

                // Orient the texture
                poseStack.translate(0, entity.getBbHeight() / 2, 0);
                poseStack.scale(this.scale, this.scale, this.scale);
                poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

                // Draw flat quad here
                VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
                Matrix4f matrix = poseStack.last().pose();
                Vector3f normal = Minecraft.getInstance().gameRenderer.getMainCamera().getLookVector();
                normal.normalize();

                Vector3f coursecorrect = new Vector3f(0.577f, 0.577f, 0.577f);
                if (normal.y > 0) {
                    coursecorrect = new Vector3f(0.01f, 1f, 0.01f);
                }

                float scaleIt = 0.23f;


                //poseStack.translate(0, 0.1, 0);

                float size = (float) Math.min(scaleIt, (((float) entity.tickCount) + partialTicks) * (scaleIt * 0.1)); // Adjust to your needs
                vertexConsumer.vertex(matrix, -size, -size, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
                vertexConsumer.vertex(matrix, size, -size, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
                vertexConsumer.vertex(matrix, size, size, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
                vertexConsumer.vertex(matrix, -size, size, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();


                poseStack.popPose();
                super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(EncasementBubbleEntity entity) {
            return TEXTURE;
    }
}

