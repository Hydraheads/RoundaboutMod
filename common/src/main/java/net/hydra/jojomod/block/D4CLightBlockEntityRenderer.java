package net.hydra.jojomod.block;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.hydra.jojomod.access.IGameRenderer;
import net.hydra.jojomod.client.shader.RCoreShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class D4CLightBlockEntityRenderer implements BlockEntityRenderer<D4CLightBlockEntity> {
    public D4CLightBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(D4CLightBlockEntity blockEntity, float v, PoseStack matrices, MultiBufferSource bufferSource, int light, int overlay) {
        matrices.pushPose();
        matrices.translate(0.5, 0.5, 0.5);

        Matrix4f poseMatrix = matrices.last().pose();
        Matrix3f normalMatrix = matrices.last().normal();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(() -> RCoreShader.roundabout$loveTrainProgram);
        RenderSystem.setShaderTexture(0, 0);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        RCoreShader.roundabout$loveTrainProgram.getUniform("FrameCount").set(((IGameRenderer)Minecraft.getInstance().gameRenderer).roundabout$getFrameCount());

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);

        drawCoreCube(buffer, poseMatrix, normalMatrix, blockEntity);

        tesselator.end();

        matrices.popPose();
    }

    private void drawCoreCube(BufferBuilder buffer, Matrix4f pose, Matrix3f normal, D4CLightBlockEntity blockEntity) {
        BlockPos origin = blockEntity.getBlockPos();
        Level level = blockEntity.getLevel();
        if (level == null) return;

        Direction[] directions = {
                Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP
        };

        Vector3f[] normals = {
                new Vector3f(0, 0, -1), new Vector3f(0, 0, 1),
                new Vector3f(-1, 0, 0), new Vector3f(1, 0, 0),
                new Vector3f(0, -1, 0), new Vector3f(0, 1, 0)
        };

        float[][] vertices = {
                {-0.5f, -0.5f, -0.5f}, {0.5f, -0.5f, -0.5f}, {0.5f, 0.5f, -0.5f}, {-0.5f, 0.5f, -0.5f}, // front
                {-0.5f, -0.5f,  0.5f}, {0.5f, -0.5f,  0.5f}, {0.5f, 0.5f,  0.5f}, {-0.5f, 0.5f,  0.5f}  // back
        };

        int[][] faces = {
                {3, 2, 1, 0}, // front (north)
                {6, 7, 4, 5}, // back (south)
                {7, 3, 0, 4}, // left (west)
                {2, 6, 5, 1}, // right (east)
                {0, 1, 5, 4}, // bottom (down)
                {7, 6, 2, 3}  // top (up)
        };

        float[][] uvs = {
                {0, 0}, {1, 0}, {1, 1}, {0, 1}
        };

        for (int i = 0; i < faces.length; i++) {
            Direction dir = directions[i];
            BlockState neighborState = level.getBlockState(origin.relative(dir));
            if (neighborState.getBlock().defaultBlockState().isSolid() || neighborState.getBlock() instanceof D4CLightBlock) continue;

            Vector3f normalVec = normals[i];

            for (int j = 0; j < 4; j++) {
                int idx = faces[i][j];
                float[] v = vertices[idx];
                float[] uv = uvs[j];

                buffer.vertex(pose, v[0], v[1], v[2])
                        .color(255, 255, 255, 255)
                        .uv(uv[0], uv[1])
                        .normal(normal, normalVec.x(), normalVec.y(), normalVec.z())
                        .endVertex();
            }
        }
    }
}