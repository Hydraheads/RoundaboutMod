package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.renderers.DiverDownBaseRenderer;
import net.hydra.jojomod.entity.substand.DiverKickEntity;
import net.hydra.jojomod.entity.stand.DiverDownEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.HashMap;
import java.util.Map;

public class DiverKickEntityRenderer extends EntityRenderer<DiverKickEntity> {
    private final Map<Byte, ModelPart> skinLegs = new HashMap<>();
    private final ModelPart defaultLeg;

    public DiverKickEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.defaultLeg = bakeLeg(context, ModEntityRendererClient.DIVER_DOWN_LAYER);
        this.skinLegs.put((byte) 0, this.defaultLeg);
        this.skinLegs.put(DiverDownEntity.BETA_DIVER, bakeLeg(context, ModEntityRendererClient.DIVER_DOWN_BETA_LAYER));
    }

    private static ModelPart bakeLeg(EntityRendererProvider.Context context, ModelLayerLocation layer) {
        ModelPart root = context.bakeLayer(layer);
        ModelPart legs = root.getChild("stand").getChild("stand2").getChild("body").getChild("body2").getChild("legs");
        ModelPart rLeg = legs.getChild("right_leg");
        rLeg.setPos(0.0F, -9.0F, 0.0F);
        return rLeg;
    }

    @Override
    public void render(DiverKickEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (!ClientUtil.canSeeStands(ClientUtil.getPlayer())) return;

        ClientUtil.pushPoseAndCooperate(poseStack, 7);

        // kick fast, stay for a while, go back in
        float progress = Mth.clamp((entity.tickCount + partialTick) / (float) DiverKickEntity.MAX_TICKS, 0.0F, 1.0F);
        float kickFactor;
        if (progress <= 0.20F) {
            kickFactor = progress / 0.2F;
        } else if (progress <= 0.80F) {
            kickFactor = 1.0F;
        } else {
            // for reference, the formula is: (progress - retract start F) / kick end F
            kickFactor = 1.0F - ((progress - 0.8F) / 0.2F);
        }

        float maxKickReach = 0.8F;
        float currentOffset = kickFactor * maxKickReach;

        Direction facing = entity.getFacing();
        double offsetX = facing.getStepX() * currentOffset;
        double offsetY = facing.getStepY() * currentOffset;
        double offsetZ = facing.getStepZ() * currentOffset;

        poseStack.translate(offsetX, offsetY, offsetZ);

        // Orient texture matching DiverLimbBlockEntityRenderer
        switch (facing) {
            case NORTH -> poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            case EAST -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            }
            case SOUTH -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            }
            case WEST -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            }
            case DOWN -> poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            default -> {}
        }

        poseStack.scale(1.0F, -1.0F, -1.0F);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(DiverDownBaseRenderer.getSkin(entity.getSkin())));
        ModelPart leg = this.skinLegs.getOrDefault(entity.getSkin(), this.defaultLeg);
        // Pitch black leg fix
        int realLight = LevelRenderer.getLightColor(entity.level(), entity.blockPosition().relative(facing));

        leg.render(poseStack, vertexConsumer, realLight, OverlayTexture.NO_OVERLAY);

        ClientUtil.popPoseAndCooperate(poseStack, 6);
    }

    @Override
    public ResourceLocation getTextureLocation(DiverKickEntity entity) {
        return DiverDownBaseRenderer.getSkin(entity.getSkin());
    }
}