package net.hydra.jojomod.client.models.mobs.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.layers.anubis.AnubisMobLayer;
import net.hydra.jojomod.client.models.mobs.AnubisGuardianModel;
import net.hydra.jojomod.client.models.mobs.StrayCatEntityModel;
import net.hydra.jojomod.client.models.mobs.layers.AnubisGuardianLayer;
import net.hydra.jojomod.entity.mobs.AnubisGuardian;
import net.hydra.jojomod.entity.mobs.StrayCatEntity;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class StrayCatEntityRenderer<T extends StrayCatEntity>
        extends MobRenderer<T, StrayCatEntityModel<T>> {

    private static final ResourceLocation ANIME =
            new ResourceLocation(Roundabout.MOD_ID,"textures/entity/stray_cat/entity/anime.png");

    /// Bubbles Shields Render :0
    private static final ResourceLocation BIG_PINK =
            new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/projectiles/big_pink.png");
    private static final ResourceLocation BIG_GREEN =
            new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/projectiles/big_green.png");


    public StrayCatEntityRenderer(EntityRendererProvider.Context context) {
        super(context,new StrayCatEntityModel<>(context.bakeLayer(ModEntityRendererClient.STRAY_CAT_ENTITY_LAYER)),0.15F);
    }

    @Override
    public ResourceLocation getTextureLocation(StrayCatEntity strayCatEntity) {
        return ANIME;
    }

    @Override
    public void render(T entity, float $$1, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer()) && entity.getBubbleShield()) {
            poseStack.pushPose();

            float size = 0.8f;

            poseStack.translate(0, size * 0.69F, 0);
            //poseStack.scale(this.scale, this.scale, this.scale);
            Direction gravityDirection = GravityAPI.getGravityDirection(entity);
            if (gravityDirection != Direction.DOWN){
                poseStack.mulPose(RotationUtil.getWorldRotationQuaternion(gravityDirection));
                poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
            } else {
                poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
            }
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

            // Draw flat quad here
            Vector3f normal = Minecraft.getInstance().gameRenderer.getMainCamera().getLookVector();
            normal.normalize();

            Vector3f coursecorrect = new Vector3f(0.577f, 0.577f, 0.577f);
            if (normal.y > 0) {
                coursecorrect = new Vector3f(0.01f, 1f, 0.01f);
            }


            // Create vertices
            Matrix4f matrix = poseStack.last().pose();
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(BIG_PINK));

            // Use a simple normal (optional, mostly lighting-related)
            vertexConsumer.vertex(matrix, -size, -size, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
            vertexConsumer.vertex(matrix, size, -size, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
            vertexConsumer.vertex(matrix, size, size, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
            vertexConsumer.vertex(matrix, -size, size, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();

            poseStack.popPose();
        }
        super.render(entity, $$1, partialTicks, poseStack, bufferSource, packedLight);
    }
}
