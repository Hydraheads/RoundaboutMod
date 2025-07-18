package net.hydra.jojomod.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.util.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class InvisiBlockEntityRenderer implements BlockEntityRenderer<InvisiBlockEntity> {
    public InvisiBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }
    public int bubbleCount = 7;

    /***
     *
     BlockState state = blockEntity.getOriginalState();

     // Render the original block as a ghost version (semi-transparent)
     ClientUtil.setThrowFadeToTheEther(0.4f);
     poseStack.pushPose();
     Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
     state,
     poseStack,
     bufferSource,
     packedLight,
     packedOverlay
     );
     ClientUtil.setThrowFadeToTheEther(1);
     poseStack.popPose();
     */
    @Override
    public void render(InvisiBlockEntity blockEntity, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        if (ClientUtil.checkIfClientCanSeeInvisAchtung()) {
            poseStack.pushPose();

            // Move to block center
            poseStack.translate(0.0, 0.0, 0.0);

            // Get a translucent buffer (cutout or translucent depending on goal)
            VertexConsumer buffer = bufferSource.getBuffer(RenderType.translucent());

            // Define cube coordinates (from 0 to 1, full block)
            AABB cube = new AABB(0.1, 0.1, 0.1, 0.9, 0.9, 0.9);

            renderCube(poseStack, buffer, cube, new Color(200, 200, 200, ClientConfig.getLocalInstance().invisibleBlockDepth), packedLight); // light gray with alpha

            poseStack.popPose();
        }
    }

    private void renderCube(PoseStack poseStack, VertexConsumer buffer, AABB box, Color color, int light) {
        Matrix4f pose = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();

        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        float a = color.getAlpha() / 255f;

        float x1 = (float) box.minX;
        float y1 = (float) box.minY;
        float z1 = (float) box.minZ;
        float x2 = (float) box.maxX;
        float y2 = (float) box.maxY;
        float z2 = (float) box.maxZ;


        // Helper function to write a quad
        BiConsumer<Vector3f[], Vector3f> writeQuad = (corners, faceNormal) -> {
            for (Vector3f corner : corners) {
                buffer.vertex(pose, corner.x(), corner.y(), corner.z())
                        .color(r, g, b, a)
                        .uv(0, 0)  // dummy UVs, not using a texture
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(light)
                        .normal(normal, faceNormal.x(), faceNormal.y(), faceNormal.z())
                        .endVertex();
            }
        };

        // Define the 6 cube faces
        writeQuad.accept(new Vector3f[]{
                new Vector3f(x1, y1, z2), new Vector3f(x2, y1, z2),
                new Vector3f(x2, y2, z2), new Vector3f(x1, y2, z2)
        }, new Vector3f(0, 0, 1)); // Front

        writeQuad.accept(new Vector3f[]{
                new Vector3f(x2, y1, z1), new Vector3f(x1, y1, z1),
                new Vector3f(x1, y2, z1), new Vector3f(x2, y2, z1)
        }, new Vector3f(0, 0, -1)); // Back

        writeQuad.accept(new Vector3f[]{
                new Vector3f(x1, y1, z1), new Vector3f(x1, y1, z2),
                new Vector3f(x1, y2, z2), new Vector3f(x1, y2, z1)
        }, new Vector3f(-1, 0, 0)); // Left

        writeQuad.accept(new Vector3f[]{
                new Vector3f(x2, y1, z2), new Vector3f(x2, y1, z1),
                new Vector3f(x2, y2, z1), new Vector3f(x2, y2, z2)
        }, new Vector3f(1, 0, 0)); // Right

        writeQuad.accept(new Vector3f[]{
                new Vector3f(x1, y2, z2), new Vector3f(x2, y2, z2),
                new Vector3f(x2, y2, z1), new Vector3f(x1, y2, z1)
        }, new Vector3f(0, 1, 0)); // Top

        writeQuad.accept(new Vector3f[]{
                new Vector3f(x1, y1, z1), new Vector3f(x2, y1, z1),
                new Vector3f(x2, y1, z2), new Vector3f(x1, y1, z2)
        }, new Vector3f(0, -1, 0)); // Bottom
    }

}
