package net.hydra.jojomod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BitesTheDustIndicatorRenderer {

    private BitesTheDustIndicatorRenderer() {
    }


    public static void renderHost(LivingEntity LE, PoseStack matrixStack, MultiBufferSource bufferSource, EntityRenderDispatcher renderDispatcher) {
        matrixStack.pushPose();

        float height = (LE.getNameTagOffsetY() + 0.275F);

        // Orient the texture
        matrixStack.scale(1, 1, 1);
        matrixStack.translate(0, height, 0);
        matrixStack.mulPose(renderDispatcher.cameraOrientation());
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        // Draw flat quad here
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(StandIcons.BITES_THE_DUST_PLANTED)).color(1.0f, 1.0f, 1.0f, 1.0f);
        Matrix4f matrix = matrixStack.last().pose();
        Matrix3f normal = matrixStack.last().normal();

        vertexConsumer.vertex(matrix, -0.25f, -0.25f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, -1).endVertex();
        vertexConsumer.vertex(matrix, 0.25f, -0.25f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, -1).endVertex();
        vertexConsumer.vertex(matrix, 0.25f, 0.25f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, -1).endVertex();
        vertexConsumer.vertex(matrix, -0.25f, 0.25f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, -1).endVertex();


        RenderSystem.enableDepthTest();
        matrixStack.popPose();
    }
    public static void renderTarget(LivingEntity LE, PoseStack matrixStack, MultiBufferSource bufferSource, EntityRenderDispatcher renderDispatcher) {
        matrixStack.pushPose();

        float height = (LE.getNameTagOffsetY() + 0.275F);

        // Orient the texture
        matrixStack.scale(1, 1, 1);
        matrixStack.translate(0, height, 0);
        matrixStack.mulPose(renderDispatcher.cameraOrientation());
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        // Draw flat quad here
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(StandIcons.BITES_THE_DUST_TARGET)).color(1.0f, 1.0f, 1.0f, 1.0f);
        Matrix4f matrix = matrixStack.last().pose();
        Matrix3f normal = matrixStack.last().normal();

        vertexConsumer.vertex(matrix, -0.3f, -0.3f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, -1).endVertex();
        vertexConsumer.vertex(matrix, 0.3f, -0.3f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, -1).endVertex();
        vertexConsumer.vertex(matrix, 0.3f, 0.3f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, -1).endVertex();
        vertexConsumer.vertex(matrix, -0.3f, 0.3f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, -1).endVertex();


        RenderSystem.enableDepthTest();
        matrixStack.popPose();
    }

}
