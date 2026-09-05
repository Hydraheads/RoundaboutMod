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
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class HallucinationIndicatorRenderer {
    private static final Map<Integer, Indicator> INDICATORS = new ConcurrentHashMap<>();

    private HallucinationIndicatorRenderer() {
    }

    public static void update(int entityId, int level) {
        Minecraft minecraft = Minecraft.getInstance();
        if (level <= 0 || minecraft.level == null) {
            INDICATORS.remove(entityId);
            return;
        }
        INDICATORS.put(entityId, new Indicator(Mth.clamp(level, 1, 3),
                minecraft.level.getGameTime() + 30));
    }

    public static void render(LivingEntity entity, PoseStack poseStack, MultiBufferSource bufferSource) {
        if (!Boolean.TRUE.equals(ConfigManager.getClientConfig()
                .whitesnakeSettings.hallucinationIndicator)) {
            return;
        }
        LocalPlayer viewer = Minecraft.getInstance().player;
        if (viewer == null || entity == viewer || entity instanceof StandEntity
                || viewer.distanceToSqr(entity) > 400) {
            return;
        }

        ItemStack standDisc = ((StandUser) viewer).roundabout$getStandDisc();
        if (!standDisc.is(ModItems.STAND_DISC_WHITESNAKE)
                && !standDisc.is(ModItems.MAX_STAND_DISC_WHITESNAKE)) {
            return;
        }

        Indicator indicator = INDICATORS.get(entity.getId());
        if (indicator == null) {
            return;
        }
        if (indicator.expiresAt() < entity.level().getGameTime()) {
            INDICATORS.remove(entity.getId());
            return;
        }

        ResourceLocation icon = switch (indicator.level()) {
            case 2 -> StandIcons.WHITESNAKE_HALLUCINATION_2;
            case 3 -> StandIcons.WHITESNAKE_HALLUCINATION_3;
            default -> StandIcons.WHITESNAKE_HALLUCINATION_1;
        };

        float size = 0.3F;
        poseStack.pushPose();
        poseStack.translate(0, entity.getBbHeight() + 0.43F, 0);
        poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.text(icon));
        Matrix4f matrix = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();
        vertexConsumer.vertex(matrix, -size, -size, 0.0F).color(255, 255, 255, 255).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, -1).endVertex();
        vertexConsumer.vertex(matrix, size, -size, 0.0F).color(255, 255, 255, 255).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, -1).endVertex();
        vertexConsumer.vertex(matrix, size, size, 0.0F).color(255, 255, 255, 255).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, -1).endVertex();
        vertexConsumer.vertex(matrix, -size, size, 0.0F).color(255, 255, 255, 255).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, -1).endVertex();

        RenderSystem.enableDepthTest();
        poseStack.popPose();
    }

    private record Indicator(int level, long expiresAt) {
    }
}
