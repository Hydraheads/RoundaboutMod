package net.hydra.jojomod.client.models.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.gui.NoCancelInputScreen;
import net.hydra.jojomod.client.gui.PoseSwitcherScreen;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Map;

public class BigBubbleLayer<T extends LivingEntity, A extends EntityModel<T>> extends RenderLayer<T, A> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final EntityRenderDispatcher dispatcher;
    public BigBubbleLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    float scale = 1;
    private static final ResourceLocation TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/large_bubble.png");
    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float var5, float var6, float var7, float partialTicks, float var9, float var10) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
            if (((StandUser)entity).roundabout$isBubbleEncased() && ((IEntityAndData)entity).roundabout$getExclusiveLayers()) {

                if (Minecraft.getInstance().screen != null && !(Minecraft.getInstance().screen instanceof NoCancelInputScreen)
                        && !(Minecraft.getInstance().screen instanceof PoseSwitcherScreen)){
                    if (ClientUtil.getPlayer() != null && ClientUtil.getPlayer().is(entity)){
                        return;
                    }
                }
                poseStack.pushPose();
                float height = entity.getDimensions(Pose.STANDING).height;
                float width = entity.getDimensions(Pose.STANDING).width;
                if (ClientUtil.savedPose != null) {
                    poseStack.last().pose().set(ClientUtil.savedPose);
                }


                // Orient the texture
                poseStack.translate(0, height * 0.69F, 0);
                poseStack.scale(this.scale, this.scale, this.scale);
                poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

                // Draw flat quad here
                Vector3f normal = Minecraft.getInstance().gameRenderer.getMainCamera().getLookVector();
                normal.normalize();

                Vector3f coursecorrect = new Vector3f(0.577f, 0.577f, 0.577f);
                if (normal.y > 0) {
                    coursecorrect = new Vector3f(0.01f, 1f, 0.01f);
                }
                // Position bubble above entity's head
                float size = Math.max(height, width) * 0.75F;

                // Create vertices
                Matrix4f matrix = poseStack.last().pose();
                VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));

                // Use a simple normal (optional, mostly lighting-related)
                vertexConsumer.vertex(matrix, -size, -size, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
                vertexConsumer.vertex(matrix, size, -size, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
                vertexConsumer.vertex(matrix, size, size, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
                vertexConsumer.vertex(matrix, -size, size, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();


                poseStack.popPose();
            }
        }
    }
}
