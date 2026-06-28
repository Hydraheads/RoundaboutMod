package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.event.index.PlunderTypes;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class StrayCatAirBubbleRenderer extends EntityRenderer<StrayCatAirBubble> {
    private static final ResourceLocation PINK_BUBBLE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/killer_queen/projectiles/pink.png");

    private final float scale;

    public StrayCatAirBubbleRenderer(EntityRendererProvider.Context context, float scale) {
        super(context);
        this.scale = scale;
    }

    public StrayCatAirBubbleRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.scale = 1f;
    }

    @Override
    public void render(StrayCatAirBubble entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
            if (((TimeStop)entity.level()).inTimeStopRange(entity)){
                partialTicks = 0;
            }
            //if (!(entity instanceof StrayCatAirBubble sp)) {
                poseStack.pushPose();

                // Orient the texture
                poseStack.scale(this.scale, this.scale, this.scale);
                poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                poseStack.translate(0, entity.getBbHeight() / 2, 0);

                // Draw flat quad here
                VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
                Matrix4f matrix = poseStack.last().pose();
                Vector3f normal = Minecraft.getInstance().gameRenderer.getMainCamera().getLookVector();
                normal.normalize();

                /**This ome is good*/
                Vector3f coursecorrect = new Vector3f(0.577f, 0.577f, 0.577f);
                if (normal.y > 0) {
                    /**This ome needs serial fixing*/
                    coursecorrect = new Vector3f(0.01f, 1f, 0.01f);
                    if (normal.y > 0.95) {
                        coursecorrect = new Vector3f(-0.577f, -0.577f, -0.577f);
                    }
                }

                float scaleIt = 0.3f;


                float size = (float) Math.min(scaleIt, (((float) entity.tickCount) + partialTicks) * (scaleIt * 0.1)); // Adjust to your needs
                /*if (entity instanceof SoftAndWetExplosiveBubbleEntity seb || entity instanceof SoftAndWetItemLaunchingBubbleEntity itemn){
                    size = scaleIt;
                }*/
                vertexConsumer.vertex(matrix, -size, -size, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
                vertexConsumer.vertex(matrix, size, -size, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
                vertexConsumer.vertex(matrix, size, size, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
                vertexConsumer.vertex(matrix, -size, size, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();

                vertexConsumer.color(1.0f, 1.0f, 1.0f, 0.7f);

                poseStack.popPose();
                super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
            //}
        }
    }

    @Override
    public ResourceLocation getTextureLocation(StrayCatAirBubble entity) {

        return PINK_BUBBLE;
    }
}

