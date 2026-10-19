package net.hydra.jojomod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.event.IVillagerAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Random;

public class LyinEyesHighlighterRenderer {
    private static final ResourceLocation[] ICONS = {
        new ResourceLocation("roundabout", "textures/particle/lyin_eyes_highlight_1.png"),
        new ResourceLocation("roundabout", "textures/particle/lyin_eyes_highlight_2.png"),
        new ResourceLocation("roundabout", "textures/particle/lyin_eyes_highlight_3.png"),
        new ResourceLocation("roundabout", "textures/particle/lyin_eyes_highlight_4.png")
    };
    private static final ResourceLocation[] ICONS_THICK = {
            new ResourceLocation("roundabout", "textures/particle/lyin_eyes_thick_1.png"),
            new ResourceLocation("roundabout", "textures/particle/lyin_eyes_thick_2.png"),
            new ResourceLocation("roundabout", "textures/particle/lyin_eyes_thick_3.png"),
            new ResourceLocation("roundabout", "textures/particle/lyin_eyes_thick_4.png")
    };

    /**
     * Billboarded circle
     */
    public static void render(LivingEntity entity, PoseStack poseStack, MultiBufferSource bufferSource,
                                           int r, int g, int b, int a, float radius) {
        poseStack.pushPose();
        // Position at entity center
        poseStack.translate(0, entity.getBbHeight() * 0.5F, 0);

        // Face the camera
        poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        // RenderType.textSeeThrough makes it visible through walls
        ResourceLocation icon = getNextIcon(entity);
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.textSeeThrough(icon));
        Matrix4f matrix = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();

        // Full brightness
        int light = 15728880;

        boolean isLowHealth = entity.getHealth() / entity.getMaxHealth() <= 0.3F;
        float pulse = (float) ((Math.sin(entity.tickCount * 0.15F) + 1.0) * 0.5);
        float brightness = isLowHealth ? 0.5F + 0.5F * pulse : 1.0F;

        consumer.vertex(matrix, -radius, -radius, 0.0F).color((int) (r * brightness), (int) (g * brightness), (int) (b * brightness), a).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, 0, -1).endVertex();
        consumer.vertex(matrix,  radius, -radius, 0.0F).color((int) (r * brightness), (int) (g * brightness), (int) (b * brightness), a).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, 0, -1).endVertex();
        consumer.vertex(matrix,  radius,  radius, 0.0F).color((int) (r * brightness), (int) (g * brightness), (int) (b * brightness), a).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, 0, -1).endVertex();
        consumer.vertex(matrix, -radius,  radius, 0.0F).color((int) (r * brightness), (int) (g * brightness), (int) (b * brightness), a).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, 0, -1).endVertex();

        poseStack.popPose();
    }

    private static ResourceLocation getNextIcon(LivingEntity entity) {
        // low health = 30% health left, slightly higher than the shaking hearts thing that happens
        boolean isLowHealth = (entity.getHealth() / entity.getMaxHealth()) <= 0.3F;

        // changes every tick if low health, otherwise every 3 ticks
        int interval = isLowHealth ? entity.tickCount : (entity.tickCount / 3);
        int round = interval / 4;
        int stepInRound = interval % 4;

        java.util.Random rnd = new java.util.Random((long) entity.getId() * 31L + round * 101L);
        int[] order = {0, 1, 2, 3};
        for (int i = 3; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int tmp = order[i];
            order[i] = order[j];
            order[j] = tmp;
        }

        // tense villager check
        if(entity instanceof Villager V && (((IVillagerAccess) V).roundabout$isTense() || ((IVillagerAccess) V).roundabout$isUnnerved()))
        {
            return ICONS_THICK[order[stepInRound]];
        }
        else return ICONS[order[stepInRound]];
    }
}