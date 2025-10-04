package net.hydra.jojomod.client.models.paintings;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.paintings.RoundaboutPainting;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class CustomPaintingRenderer extends EntityRenderer<RoundaboutPainting> {
    public CustomPaintingRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }

    public void render(RoundaboutPainting painting, float yaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource source, int light) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        poseStack.scale(0.0625F, 0.0625F, 0.0625F);

        // pass both buffers down
        renderPainting(poseStack, getTexture(), getTextureBack(), painting, getWidth(), getHeight(),source);

        poseStack.popPose();
        super.render(painting, yaw, partialTicks, poseStack, source, light);
    }
    public int getWidth(){
        return 16;
    }
    public int getHeight(){
        return 32;
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Roundabout.MOD_ID,"textures/entity/painting/van_gogh.png");
    }
    public ResourceLocation getTextureBack() {
        return new ResourceLocation(Roundabout.MOD_ID,"textures/entity/painting/back.png");
    }

    public ResourceLocation getTextureLocation(RoundaboutPainting $$0) {
        return getTextureBack();
    }

    private static final ResourceLocation PAINTING_BACK = new ResourceLocation("minecraft", "textures/entity/painting/back.png");

    private void renderPainting(PoseStack stack,
                                ResourceLocation frontBuffer,
                                ResourceLocation backBuffer,
                                RoundaboutPainting painting,
                                int width,
                                int height,
                                MultiBufferSource source) {
        PoseStack.Pose last = stack.last();
        Matrix4f pose = last.pose();
        Matrix3f normal = last.normal();
        float x0 = -width / 2.0F;
        float y0 = -height / 2.0F;

        int tilesX = width / 16;
        int tilesY = height / 16;

        double uStep = 1.0 / tilesX;
        double vStep = 1.0 / tilesY;

        for (int tx = 0; tx < tilesX; ++tx) {
            for (int ty = 0; ty < tilesY; ++ty) {
                float x1 = x0 + (tx + 1) * 16;
                float x2 = x0 + tx * 16;
                float y1 = y0 + (ty + 1) * 16;
                float y2 = y0 + ty * 16;

                // compute world position for lighting (same as vanilla)
                int blockX = painting.getBlockX();
                int blockY = Mth.floor(painting.getY() + ((y1 + y2) / 2.0F / 16.0F));
                int blockZ = painting.getBlockZ();
                Direction dir = painting.getDirection();

                if (dir == Direction.NORTH) {
                    blockX = Mth.floor(painting.getX() + ((x1 + x2) / 2.0F / 16.0F));
                }
                if (dir == Direction.WEST) {
                    blockZ = Mth.floor(painting.getZ() - ((x1 + x2) / 2.0F / 16.0F));
                }
                if (dir == Direction.SOUTH) {
                    blockX = Mth.floor(painting.getX() - ((x1 + x2) / 2.0F / 16.0F));
                }
                if (dir == Direction.EAST) {
                    blockZ = Mth.floor(painting.getZ() + ((x1 + x2) / 2.0F / 16.0F));
                }

                int light = LevelRenderer.getLightColor(painting.level(), new BlockPos(blockX, blockY, blockZ));

                // ---- FRONT FACE ----
                VertexConsumer frontBuffer2 = source.getBuffer(RenderType.entitySolid(frontBuffer));
                float u0 = (float) (uStep * (tilesX - tx));
                float u1 = (float) (uStep * (tilesX - (tx + 1)));
                float v0 = (float) (vStep * (tilesY - ty));
                float v1 = (float) (vStep * (tilesY - (ty + 1)));

                vertex(pose, normal, frontBuffer2, x1, y2, u1, v0, -0.5F, 0, 0, -1, light);
                vertex(pose, normal, frontBuffer2, x2, y2, u0, v0, -0.5F, 0, 0, -1, light);
                vertex(pose, normal, frontBuffer2, x2, y1, u0, v1, -0.5F, 0, 0, -1, light);
                vertex(pose, normal, frontBuffer2, x1, y1, u1, v1, -0.5F, 0, 0, -1, light);

                // ---- BACK FACE ----
                VertexConsumer backBuffer2 = source.getBuffer(RenderType.entitySolid(backBuffer));
                u0 = 0.0F; u1 = 1.0F; v0 = 0.0F; v1 = 1.0F;

                vertex(pose, normal, backBuffer2, x1, y1, u1, v0, 0.5F, 0, 0, 1, light);
                vertex(pose, normal, backBuffer2, x2, y1, u0, v0, 0.5F, 0, 0, 1, light);
                vertex(pose, normal, backBuffer2, x2, y2, u0, v1, 0.5F, 0, 0, 1, light);
                vertex(pose, normal, backBuffer2, x1, y2, u1, v1, 0.5F, 0, 0, 1, light);

                // ---- edges (optional, like vanilla) ----
                // You can repeat with backTexture and fixed UVs (0..1) for top/bottom/side strips
                // ---- EDGES ----
                // ---- EDGES ----
                VertexConsumer edgeBuffer = source.getBuffer(RenderType.entitySolid(backBuffer));float topY = y1;
                float zFront = -0.5f;
                float zBack  = 0.5f;

// top edge spans x2 → x1 (full width) but only 1px tall
                vertex(pose, normal, edgeBuffer,
                        x1, topY, 1f, 0f, zFront, 0, 1, 0, light);
                vertex(pose, normal, edgeBuffer,
                        x2, topY, 0f, 0f, zFront, 0, 1, 0, light);
                vertex(pose, normal, edgeBuffer,
                        x2, topY, 0f, 1f/16f, zBack, 0, 1, 0, light);
                vertex(pose, normal, edgeBuffer,
                        x1, topY, 1f, 1f/16f, zBack, 0, 1, 0, light);

                float bottomY = y2;
                vertex(pose, normal, edgeBuffer,
                        x1, bottomY, 1f, 15f/16f, zBack, 0, -1, 0, light);
                vertex(pose, normal, edgeBuffer,
                        x2, bottomY, 0f, 15f/16f, zBack, 0, -1, 0, light);
                vertex(pose, normal, edgeBuffer,
                        x2, bottomY, 0f, 1f, zFront, 0, -1, 0, light);
                vertex(pose, normal, edgeBuffer,
                        x1, bottomY, 1f, 1f, zFront, 0, -1, 0, light);

                float leftX = x2;
                vertex(pose, normal, edgeBuffer,
                        leftX, y1, 15f/16f, 0f, zFront, -1, 0, 0, light);
                vertex(pose, normal, edgeBuffer,
                        leftX, y2, 15f/16f, 1f, zFront, -1, 0, 0, light);
                vertex(pose, normal, edgeBuffer,
                        leftX, y2, 1f, 1f, zBack, -1, 0, 0, light);
                vertex(pose, normal, edgeBuffer,
                        leftX, y1, 1f, 0f, zBack, -1, 0, 0, light);

                float rightX = x1;
                vertex(pose, normal, edgeBuffer,
                        rightX, y1, 0f, 0f, zBack, 1, 0, 0, light);
                vertex(pose, normal, edgeBuffer,
                        rightX, y2, 0f, 1f, zBack, 1, 0, 0, light);
                vertex(pose, normal, edgeBuffer,
                        rightX, y2, 1f/16f, 1f, zFront, 1, 0, 0, light);
                vertex(pose, normal, edgeBuffer,
                        rightX, y1, 1f/16f, 0f, zFront, 1, 0, 0, light);
            }
        }
    }

    private void vertex(Matrix4f pose, Matrix3f normal, VertexConsumer consumer,
                        float x, float y, float u, float v,
                        float zOffset, int nx, int ny, int nz, int light) {
        consumer.vertex(pose, x, y, zOffset)
                .color(255, 255, 255, 255)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, nx, ny, nz)
                .endVertex();
    }
}
