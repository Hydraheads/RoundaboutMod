package net.hydra.jojomod.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.StandFireType;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.util.config.ClientConfig;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
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

    private final BlockRenderDispatcher itemRenderer;
    public InvisiBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super();
        this.itemRenderer = context.getBlockRenderDispatcher();
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


    public float clampUnit(float unit){
        return ((unit - 0.5F)*0.80F)+0.05F;
    }

    public void renderCycle(int step, Level level, Vec3 posMiddle){
        double x = 0;
        double y = 0;
        double z = 0;
        double unit = 0.2; // 10 segments per edge
        double clampUnit = clampUnit((float) (((float) step%9) * 1.111 * unit));
        float modUnit = 0.45f;
        if (step < 9) {
            // ABOVE/Top left to right
            x = posMiddle.x() + clampUnit;
            z = posMiddle.z() - modUnit;
            y = posMiddle.y() + modUnit;
            level.addAlwaysVisibleParticle(
                    ModParticles.BRIEF_MAGIC_DUST, x, y, z, 0, 0, 0);
            // BELOW/TOP left to right
            y = posMiddle.y() - modUnit;


            x = posMiddle.x() + modUnit;
            z = posMiddle.z() - modUnit;
            y = posMiddle.y() + clampUnit;
            level.addAlwaysVisibleParticle(
                    ModParticles.BRIEF_MAGIC_DUST, x, y, z, 0, 0, 0);
            x = posMiddle.x() - modUnit;
            z = posMiddle.z() + modUnit;
            level.addAlwaysVisibleParticle(
                    ModParticles.BRIEF_MAGIC_DUST, x, y, z, 0, 0, 0);

        } else if (step < 18) {
            // ABOVE/Right top to bottom
            x = posMiddle.x() + modUnit;
            z = posMiddle.z() + clampUnit;
            y = posMiddle.y() + modUnit;
            level.addAlwaysVisibleParticle(
                    ModParticles.BRIEF_MAGIC_DUST, x, y, z, 0, 0, 0);
            // BELOW/Right top to bottom
            y = posMiddle.y() - modUnit;
            level.addAlwaysVisibleParticle(
                    ModParticles.BRIEF_MAGIC_DUST, x, y, z, 0, 0, 0);


            x = posMiddle.x() - modUnit;
            z = posMiddle.z() - modUnit;
            y = posMiddle.y() - clampUnit;
            level.addAlwaysVisibleParticle(
                    ModParticles.BRIEF_MAGIC_DUST, x, y, z, 0, 0, 0);
            x = posMiddle.x() + modUnit;
            z = posMiddle.z() + modUnit;
            level.addAlwaysVisibleParticle(
                    ModParticles.BRIEF_MAGIC_DUST, x, y, z, 0, 0, 0);
        } else if (step < 27) {
            // ABOVE/Bottom right to left
            x = posMiddle.x() - clampUnit;
            z = posMiddle.z() + modUnit;
            y = posMiddle.y() + modUnit;
            level.addAlwaysVisibleParticle(
                    ModParticles.BRIEF_MAGIC_DUST, x, y, z, 0, 0, 0);
            // BELOW/Bottom right to left
            y = posMiddle.y() - modUnit;
            level.addAlwaysVisibleParticle(
                    ModParticles.BRIEF_MAGIC_DUST, x, y, z, 0, 0, 0);



            x = posMiddle.x() + modUnit;
            z = posMiddle.z() - modUnit;
            y = posMiddle.y() + clampUnit;
            level.addAlwaysVisibleParticle(
                    ModParticles.BRIEF_MAGIC_DUST, x, y, z, 0, 0, 0);
            x = posMiddle.x() - modUnit;
            z = posMiddle.z() + modUnit;
            level.addAlwaysVisibleParticle(
                    ModParticles.BRIEF_MAGIC_DUST, x, y, z, 0, 0, 0);
        } else {
            // ABOVE/LEFT bottom to top
            x = posMiddle.x() - modUnit;
            z = posMiddle.z() - clampUnit;
            y = posMiddle.y() + modUnit;
            level.addAlwaysVisibleParticle(
                    ModParticles.BRIEF_MAGIC_DUST, x, y, z, 0, 0, 0);
            // BELOW/LEFT bottom to top
            y = posMiddle.y() - modUnit;
            level.addAlwaysVisibleParticle(
                    ModParticles.BRIEF_MAGIC_DUST, x, y, z, 0, 0, 0);


            x = posMiddle.x() - modUnit;
            z = posMiddle.z() - modUnit;
            y = posMiddle.y() - clampUnit;
            level.addAlwaysVisibleParticle(
                    ModParticles.BRIEF_MAGIC_DUST, x, y, z, 0, 0, 0);
            x = posMiddle.x() + modUnit;
            z = posMiddle.z() + modUnit;
            level.addAlwaysVisibleParticle(
                    ModParticles.BRIEF_MAGIC_DUST, x, y, z, 0, 0, 0);
        }
    }
    @Override
    public void render(InvisiBlockEntity blockEntity, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        if (ClientUtil.checkIfClientCanSeeInvisAchtung()) {
            Level level = blockEntity.getLevel();
            if (level.isClientSide()) {
                int step = ClientUtil.getClientTicker();
                if (blockEntity.lastRenderTick != step && step%5==0) {
                    blockEntity.lastRenderTick = step;
                    Vec3 pos = blockEntity.getBlockPos().getCenter();
                    level.addAlwaysVisibleParticle(
                            ModParticles.BRIEF_MAGIC_DUST, pos.x, pos.y, pos.z, 0, 0, 0);
                }
            }
            /**
            if (ClientUtil.isFabulous()){
            } else {

                poseStack.pushPose();

                // Move to block center
                poseStack.translate(0.0, 0.0, 0.0);

                // Get a translucent buffer (cutout or translucent depending on goal)
                VertexConsumer buffer = bufferSource.getBuffer(RenderType.translucent());

                // Define cube coordinates (from 0 to 1, full block)
                AABB cube = new AABB(0.2, 0.2, 0.2, 0.8, 0.8, 0.8);

                renderCube(poseStack, buffer, cube, new Color(200, 200, 200, ClientConfig.getLocalInstance().invisibleBlockDepth), packedLight); // light gray with alpha

                poseStack.popPose();
            }
             **/
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
