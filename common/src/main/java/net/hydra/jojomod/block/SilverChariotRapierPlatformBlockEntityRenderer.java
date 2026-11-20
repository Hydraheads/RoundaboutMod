package net.hydra.jojomod.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.renderers.SilverChariotBaseRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class SilverChariotRapierPlatformBlockEntityRenderer implements BlockEntityRenderer<SilverChariotRapierPlatformBlockEntity> {

    private final ModelPart blade;

    public SilverChariotRapierPlatformBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super();
        this.blade = bakeSwordBlade(context, ModEntityRendererClient.SILVER_CHARIOT_SWORD_PROJECTILE_LAYER);
    }

    private static ModelPart bakeSwordBlade(BlockEntityRendererProvider.Context context, ModelLayerLocation layer) {
        return context.bakeLayer(layer).getChild("blade");
    }

    @Override
    public void render(SilverChariotRapierPlatformBlockEntity silverChariotRapierPlatformBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
            ClientUtil.pushPoseAndCooperate(poseStack,7);

            // float depth = ClientNetworking.getAppropriateConfig().silverChariotSettings.poseStack_Depth;
            float depth = 0.25F;

            Direction surDir = silverChariotRapierPlatformBlockEntity.facing != null ? silverChariotRapierPlatformBlockEntity.facing : Direction.NORTH;
            double offsetX = surDir.getStepX() * depth;
            double offsetY = surDir.getStepY() * depth;
            double offsetZ = surDir.getStepZ() * depth;

            // double x = ClientNetworking.getAppropriateConfig().silverChariotSettings.poseStack_Translate_x;
            // double y = ClientNetworking.getAppropriateConfig().silverChariotSettings.poseStack_Translate_y;
            // double z = ClientNetworking.getAppropriateConfig().silverChariotSettings.poseStack_Translate_z;

            double x = 0.5F;
            double y = 0.5F;
            double z = 0.5F;

            poseStack.translate(x + offsetX, y + offsetY, z + offsetZ);

            if (silverChariotRapierPlatformBlockEntity.facing != null) {
                switch (silverChariotRapierPlatformBlockEntity.facing) {
                    case NORTH -> {
                        // poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));

                        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                        poseStack.mulPose(Axis.XP.rotationDegrees(0.0F));

                        // poseStack.mulPose(Axis.YP.rotationDegrees(ClientNetworking.getAppropriateConfig().silverChariotSettings.poseStack_Axis_NORTH_YP));
                        // poseStack.mulPose(Axis.XP.rotationDegrees(ClientNetworking.getAppropriateConfig().silverChariotSettings.poseStack_Axis_NORTH_XP));
                    }
                    case EAST -> {
                        // poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
                        // poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));

                        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
                        poseStack.mulPose(Axis.XP.rotationDegrees(0.0F));

                        // poseStack.mulPose(Axis.YP.rotationDegrees(ClientNetworking.getAppropriateConfig().silverChariotSettings.poseStack_Axis_EAST_YP));
                        // poseStack.mulPose(Axis.XP.rotationDegrees(ClientNetworking.getAppropriateConfig().silverChariotSettings.poseStack_Axis_EAST_XP));
                    }
                    case SOUTH -> {
                        // poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                        // poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));

                        poseStack.mulPose(Axis.YP.rotationDegrees(0.0F));
                        poseStack.mulPose(Axis.XP.rotationDegrees(0.0F));

                        // poseStack.mulPose(Axis.YP.rotationDegrees(ClientNetworking.getAppropriateConfig().silverChariotSettings.poseStack_Axis_SOUTH_YP));
                        // poseStack.mulPose(Axis.XP.rotationDegrees(ClientNetworking.getAppropriateConfig().silverChariotSettings.poseStack_Axis_SOUTH_XP));
                    }
                    case WEST -> {
                        // poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
                        // poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));

                        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
                        poseStack.mulPose(Axis.XP.rotationDegrees(0.0F));

                        // poseStack.mulPose(Axis.YP.rotationDegrees(ClientNetworking.getAppropriateConfig().silverChariotSettings.poseStack_Axis_WEST_YP));
                        // poseStack.mulPose(Axis.XP.rotationDegrees(ClientNetworking.getAppropriateConfig().silverChariotSettings.poseStack_Axis_WEST_XP));
                    }
                    case DOWN -> {
                        // poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

                        poseStack.mulPose(Axis.YP.rotationDegrees(0.0F));
                        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

                        // poseStack.mulPose(Axis.YP.rotationDegrees(ClientNetworking.getAppropriateConfig().silverChariotSettings.poseStack_Axis_DOWN_YP));
                        // poseStack.mulPose(Axis.XP.rotationDegrees(ClientNetworking.getAppropriateConfig().silverChariotSettings.poseStack_Axis_DOWN_XP));
                    }
                    case UP -> {
                        poseStack.mulPose(Axis.YP.rotationDegrees(0.0F));
                        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));

                        // poseStack.mulPose(Axis.YP.rotationDegrees(ClientNetworking.getAppropriateConfig().silverChariotSettings.poseStack_Axis_UP_YP));
                        // poseStack.mulPose(Axis.XP.rotationDegrees(ClientNetworking.getAppropriateConfig().silverChariotSettings.poseStack_Axis_UP_XP));
                    }
                    default -> {}
                }
            }
            poseStack.scale(1.0F, -1.0F, -1.0F);
            // VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(SilverChariotBaseRenderer.getSkin(silverChariotRapierPlatformBlockEntity.standSkin)));
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityTranslucent(SilverChariotBaseRenderer.getSkin(silverChariotRapierPlatformBlockEntity.standSkin)));

            blade.render(poseStack, vertexConsumer, i, i1);

            ClientUtil.popPoseAndCooperate(poseStack,6);
        }
    }
}
