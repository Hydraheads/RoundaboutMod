package net.hydra.jojomod.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.HallucinatoryAcidColors;
import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.whitesnake.HallucinationEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class HallucinatoryAcidBlockEntityRenderer
        implements BlockEntityRenderer<HallucinatoryAcidBlockEntity> {
    private static final ResourceLocation ACID_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,
            "textures/block/hallucinatory_acid_inner.png");
    private static final ResourceLocation SAND_TEXTURE = new ResourceLocation("minecraft",
            "textures/block/sand.png");

    public HallucinatoryAcidBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(HallucinatoryAcidBlockEntity acid, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffers, int packedLight, int packedOverlay) {
        BlockState state = acid.getBlockState();
        if (!(state.getBlock() instanceof HallucinatoryAcidBlock) || acidIsHidden()) return;
        int layers = state.getValue(HallucinatoryAcidBlock.LAYERS);
        int skin = state.getValue(HallucinatoryAcidBlock.SKIN);
        float height = switch (layers) {
            case 1 -> 3.0F;
            case 2 -> 7.0F;
            case 3 -> 11.0F;
            default -> 15.0F;
        } / 16.0F;
        ResourceLocation innerTexture = skin == WhitesnakeEntity.SANDSNAKE_SKIN
                ? SAND_TEXTURE : ACID_TEXTURE;
        VertexConsumer vertices = buffers.getBuffer(RenderType.entityCutoutNoCull(innerTexture));
        PoseStack.Pose pose = poseStack.last();
        int color = skin == WhitesnakeEntity.SANDSNAKE_SKIN
                ? 0xFFFFFF : HallucinatoryAcidColors.tint(skin);
        renderCube(vertices, pose, 0.125F, 0.0F, 0.125F, 0.875F, height, 0.875F,
                packedLight, packedOverlay, color);

        Level level = acid.getLevel();
        if (level == null) return;
        BlockPos pos = acid.getBlockPos();
        renderConnection(level, pos, state, Direction.NORTH, vertices, pose, height,
                packedLight, packedOverlay, color);
        renderConnection(level, pos, state, Direction.SOUTH, vertices, pose, height,
                packedLight, packedOverlay, color);
        renderConnection(level, pos, state, Direction.WEST, vertices, pose, height,
                packedLight, packedOverlay, color);
        renderConnection(level, pos, state, Direction.EAST, vertices, pose, height,
                packedLight, packedOverlay, color);
        renderCorner(level, pos, state, Direction.NORTH, Direction.WEST, vertices, pose, height,
                packedLight, packedOverlay, color);
        renderCorner(level, pos, state, Direction.NORTH, Direction.EAST, vertices, pose, height,
                packedLight, packedOverlay, color);
        renderCorner(level, pos, state, Direction.SOUTH, Direction.WEST, vertices, pose, height,
                packedLight, packedOverlay, color);
        renderCorner(level, pos, state, Direction.SOUTH, Direction.EAST, vertices, pose, height,
                packedLight, packedOverlay, color);
    }

    private static void renderConnection(Level level, BlockPos pos, BlockState state, Direction direction,
                                         VertexConsumer vertices, PoseStack.Pose pose, float height,
                                         int light, int overlay, int color) {
        BlockState neighbor = level.getBlockState(pos.relative(direction));
        if (!isMatchingAcid(neighbor, state)) return;

        float connectionHeight = Math.min(height, coreHeight(neighbor));
        switch (direction) {
            case NORTH -> renderCube(vertices, pose, 0.125F, 0.0F, 0.0F,
                    0.875F, connectionHeight, 0.125F, light, overlay, color);
            case SOUTH -> renderCube(vertices, pose, 0.125F, 0.0F, 0.875F,
                    0.875F, connectionHeight, 1.0F, light, overlay, color);
            case WEST -> renderCube(vertices, pose, 0.0F, 0.0F, 0.125F,
                    0.125F, connectionHeight, 0.875F, light, overlay, color);
            case EAST -> renderCube(vertices, pose, 0.875F, 0.0F, 0.125F,
                    1.0F, connectionHeight, 0.875F, light, overlay, color);
            default -> {
            }
        }
    }

    private static void renderCorner(Level level, BlockPos pos, BlockState state,
                                     Direction first, Direction second,
                                     VertexConsumer vertices, PoseStack.Pose pose, float height,
                                     int light, int overlay, int color) {
        BlockState firstNeighbor = level.getBlockState(pos.relative(first));
        BlockState secondNeighbor = level.getBlockState(pos.relative(second));
        BlockState diagonalNeighbor = level.getBlockState(pos.relative(first).relative(second));
        if (!isMatchingAcid(firstNeighbor, state) || !isMatchingAcid(secondNeighbor, state)
                || !isMatchingAcid(diagonalNeighbor, state)) return;

        float connectionHeight = Math.min(height, Math.min(coreHeight(firstNeighbor),
                Math.min(coreHeight(secondNeighbor), coreHeight(diagonalNeighbor))));
        float minX = second == Direction.WEST ? 0.0F : 0.875F;
        float maxX = second == Direction.WEST ? 0.125F : 1.0F;
        float minZ = first == Direction.NORTH ? 0.0F : 0.875F;
        float maxZ = first == Direction.NORTH ? 0.125F : 1.0F;
        renderCube(vertices, pose, minX, 0.0F, minZ, maxX, connectionHeight, maxZ,
                light, overlay, color);
    }

    private static boolean isMatchingAcid(BlockState candidate, BlockState source) {
        return candidate.getBlock() instanceof HallucinatoryAcidBlock
                && candidate.getValue(HallucinatoryAcidBlock.SKIN)
                == source.getValue(HallucinatoryAcidBlock.SKIN);
    }

    private static float coreHeight(BlockState state) {
        return switch (state.getValue(HallucinatoryAcidBlock.LAYERS)) {
            case 1 -> 3.0F;
            case 2 -> 7.0F;
            case 3 -> 11.0F;
            default -> 15.0F;
        } / 16.0F;
    }

    private static boolean acidIsHidden() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return false;
        MobEffectInstance effect = minecraft.player.getEffect(ModEffects.HALLUCINATION);
        return HallucinationEffect.hasDistortion(effect);
    }

    private static void renderCube(VertexConsumer vertices, PoseStack.Pose pose,
                                   float minX, float minY, float minZ,
                                   float maxX, float maxY, float maxZ,
                                   int light, int overlay, int color) {
        quad(vertices, pose, light, overlay, 0, -1, 0,
                maxX, minY, minZ, minX, minY, minZ, minX, minY, maxZ, maxX, minY, maxZ, color);
        quad(vertices, pose, light, overlay, 0, 1, 0,
                minX, maxY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, minX, maxY, maxZ, color);
        quad(vertices, pose, light, overlay, 0, 0, -1,
                minX, minY, minZ, minX, maxY, minZ, maxX, maxY, minZ, maxX, minY, minZ, color);
        quad(vertices, pose, light, overlay, 0, 0, 1,
                maxX, minY, maxZ, maxX, maxY, maxZ, minX, maxY, maxZ, minX, minY, maxZ, color);
        quad(vertices, pose, light, overlay, -1, 0, 0,
                minX, minY, maxZ, minX, maxY, maxZ, minX, maxY, minZ, minX, minY, minZ, color);
        quad(vertices, pose, light, overlay, 1, 0, 0,
                maxX, minY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, maxX, minY, maxZ, color);
    }

    private static void quad(VertexConsumer vertices, PoseStack.Pose pose, int light, int overlay,
                             float normalX, float normalY, float normalZ,
                             float x1, float y1, float z1, float x2, float y2, float z2,
                             float x3, float y3, float z3, float x4, float y4, float z4,
                             int color) {
        vertex(vertices, pose, light, overlay, normalX, normalY, normalZ, x1, y1, z1, 0, 1, color);
        vertex(vertices, pose, light, overlay, normalX, normalY, normalZ, x2, y2, z2, 0, 0, color);
        vertex(vertices, pose, light, overlay, normalX, normalY, normalZ, x3, y3, z3, 1, 0, color);
        vertex(vertices, pose, light, overlay, normalX, normalY, normalZ, x4, y4, z4, 1, 1, color);
    }

    private static void vertex(VertexConsumer vertices, PoseStack.Pose pose, int light, int overlay,
                               float normalX, float normalY, float normalZ,
                               float x, float y, float z, float u, float v, int color) {
        vertices.vertex(pose.pose(), x, y, z)
                .color((color >> 16) & 255, (color >> 8) & 255, color & 255, 255).uv(u, v)
                .overlayCoords(overlay).uv2(light).normal(pose.normal(), normalX, normalY, normalZ).endVertex();
    }
}
