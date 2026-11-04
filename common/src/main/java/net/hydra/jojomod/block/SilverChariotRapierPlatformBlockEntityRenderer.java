package net.hydra.jojomod.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.stand.renderers.SilverChariotBaseRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class SilverChariotRapierPlatformBlockEntityRenderer implements BlockEntityRenderer<SilverChariotRapierPlatformBlockEntity> {

    public SilverChariotRapierPlatformBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }

    @Override
    public void render(SilverChariotRapierPlatformBlockEntity silverChariotRapierPlatformBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
            ClientUtil.pushPoseAndCooperate(poseStack,7);

            if (silverChariotRapierPlatformBlockEntity.facing != null) {
                switch (silverChariotRapierPlatformBlockEntity.facing) {
                    case NORTH -> {
                        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                    }
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
                    case DOWN -> {
                        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                    }
                    default -> {}
                }
            }
            poseStack.scale(1.0F, -1.0F, -1.0F);
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(SilverChariotBaseRenderer.getSkin(silverChariotRapierPlatformBlockEntity.standSkin)));
            ClientUtil.popPoseAndCooperate(poseStack,6);
        }
    }
}
