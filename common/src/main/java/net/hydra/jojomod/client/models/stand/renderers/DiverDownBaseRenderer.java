package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.BlackSabbathEntity;
import net.hydra.jojomod.entity.stand.DiverDownEntity;
import net.hydra.jojomod.entity.stand.KingCrimsonEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersDiverDown;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class DiverDownBaseRenderer extends StandRenderer<DiverDownEntity> {
    public static final ResourceLocation PART_6 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/base.png");
    public static final ResourceLocation BETA_DIVER = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/beta.png");
    public static final ResourceLocation HOLY_DIVER = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/beta.png");
    public static final ResourceLocation KELP = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/kelp.png");

    private static final ResourceLocation[] RIPPLE_FRAMES = new ResourceLocation[]{
            new ResourceLocation(Roundabout.MOD_ID, "textures/particle/energy_ripple4.png"),
            new ResourceLocation(Roundabout.MOD_ID, "textures/particle/energy_ripple5.png"),
            new ResourceLocation(Roundabout.MOD_ID, "textures/particle/energy_ripple6.png")
    };

    public DiverDownBaseRenderer(EntityRendererProvider.Context context, StandModel<DiverDownEntity> entityModel, float f) {
        super(context, entityModel, f);
        DiverDownDisguiseRenderer.INSTANCE = new DiverDownDisguiseRenderer(context);
    }

    public static ResourceLocation getSkin(byte bt) {
        if (bt == DiverDownEntity.BETA_DIVER) {
            return BETA_DIVER;
        }
        if (bt == DiverDownEntity.HOLY_DIVER) {
            return HOLY_DIVER;
        }
        if (bt == DiverDownEntity.KELP) {
            return KELP;
        }
        return PART_6;
    }

    @Override
    public void render(DiverDownEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        if (mobEntity.getUser() instanceof StandUser su && su.roundabout$getStandPowers() instanceof PowersDiverDown dd) {
            if (dd.isPiloting()) {
                LocalPlayer viewer = Minecraft.getInstance().player;
                boolean isOwner = viewer != null && mobEntity.getUser().is(viewer);

                if (isOwner && !dd.oreDetectionEnabled) {
                    renderHitboxCircle(mobEntity.tickCount, matrixStack, vertexConsumerProvider);
                }

                if (PowersDiverDown.MAX_DIVE_TICKS - dd.diveTicksLeft > 3) {
                    return;
                }
            }
        }

        float factor = 1;
        if(mobEntity.isBaby()){
            matrixStack.scale(0.50f * factor, 0.50f * factor, 0.50f * factor);
        } else {
            matrixStack.scale(0.87f * factor, 0.87f * factor, 0.87f * factor);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private void renderHitboxCircle(int tickCount, PoseStack matrixStack, MultiBufferSource bufferSource) {
        float radius = 1.8F;

        int ticksPerFrame = 3;

        int frameIndex = (tickCount / ticksPerFrame) % 3;

        ResourceLocation currentFrameTexture = RIPPLE_FRAMES[frameIndex];

        matrixStack.pushPose();
        matrixStack.translate(0.0D, 0.02D, 0.0D);
        matrixStack.mulPose(Axis.XP.rotationDegrees(90.0F));

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.energySwirl(currentFrameTexture, 0.0f, 0.0f));
        Matrix4f matrix = matrixStack.last().pose();
        Matrix3f normal = matrixStack.last().normal();

        consumer.vertex(matrix, -radius, -radius, 0.0F).color(110, 160, 255, 200).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, 1).endVertex();
        consumer.vertex(matrix, -radius, radius, 0.0F).color(110, 160, 255, 200).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, 1).endVertex();
        consumer.vertex(matrix, radius, radius, 0.0F).color(110, 160, 255, 200).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, 1).endVertex();
        consumer.vertex(matrix, radius, -radius, 0.0F).color(110, 160, 255, 200).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, 1).endVertex();

        matrixStack.popPose();
    }

    @Nullable
    @Override
    protected RenderType getRenderType(DiverDownEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }

    @Override
    public ResourceLocation getTextureLocation(DiverDownEntity entity) {
        return getSkin(entity.getSkin());
    }
}