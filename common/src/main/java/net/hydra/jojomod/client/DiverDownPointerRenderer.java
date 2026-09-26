package net.hydra.jojomod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersDiverDown;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public final class DiverDownPointerRenderer {
    private static final ResourceLocation POINTER_ICON = new ResourceLocation(
            Roundabout.MOD_ID, "textures/particle/pointer_diver_down.png");

    private DiverDownPointerRenderer() {}

    public static void render(LivingEntity entity, PoseStack poseStack, MultiBufferSource bufferSource) {
        LocalPlayer viewer = Minecraft.getInstance().player;
        if (viewer == null || entity == viewer) {
            return;
        }

        if (((StandUser) viewer).roundabout$getStandPowers() instanceof PowersDiverDown dd) {
            if (dd.isDiveActive() && !dd.isSelfDive() && dd.submergedTarget == entity) {
                float size = 0.35F;

                poseStack.pushPose();
                poseStack.translate(0, entity.getBbHeight() + 0.5F, 0);
                poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

                VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.text(POINTER_ICON));
                Matrix4f matrix = poseStack.last().pose();
                Matrix3f normal = poseStack.last().normal();

                vertexConsumer.vertex(matrix, -size, -size, 0.0F).color(255, 255, 255, 255).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, -1).endVertex();
                vertexConsumer.vertex(matrix, size, -size, 0.0F).color(255, 255, 255, 255).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, -1).endVertex();
                vertexConsumer.vertex(matrix, size, size, 0.0F).color(255, 255, 255, 255).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, -1).endVertex();
                vertexConsumer.vertex(matrix, -size, size, 0.0F).color(255, 255, 255, 255).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, -1).endVertex();

                RenderSystem.enableDepthTest();
                poseStack.popPose();
            }
        }
    }
}