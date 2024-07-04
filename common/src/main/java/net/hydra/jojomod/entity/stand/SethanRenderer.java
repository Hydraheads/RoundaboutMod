package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;


public abstract class SethanRenderer {

    public static ResourceLocation SETHAN_TEXTURE = new ResourceLocation(Roundabout.MOD_ID + ":textures/stand/sethan.png");
    private static final RenderType SHADOW_RENDER_TYPE = RenderType.entityShadow(SETHAN_TEXTURE);


    public static void renderEntity(EntityRenderDispatcher entityRenderDispatcher, Entity castingEntity, double x, double y, double z, float delta, PoseStack poseStack, MultiBufferSource multiBufferSource) {
        double deltaX = Mth.lerp((double)delta, castingEntity.xOld, castingEntity.getX());
        double deltaY = Mth.lerp((double)delta, castingEntity.yOld, castingEntity.getY());
        double deltaZ = Mth.lerp((double)delta, castingEntity.zOld, castingEntity.getZ());
        float yRotation = Mth.lerp(delta, castingEntity.yRotO, castingEntity.getYRot());
        render(entityRenderDispatcher, castingEntity, deltaX - x, deltaY - y, deltaZ - z, yRotation, delta, poseStack, multiBufferSource, entityRenderDispatcher.getPackedLightCoords(castingEntity, delta));
    }

    public static <E extends Entity> void render(EntityRenderDispatcher entityRenderDispatcher, E castingEntity, double deltaX, double deltaY, double deltaZ, float yRotation, float delta, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLightCoords) {
        EntityRenderer<? super E> entityrenderer = entityRenderDispatcher.getRenderer(castingEntity);

            Vec3 vec3 = entityrenderer.getRenderOffset(castingEntity, delta);
            double deltaXOffset = deltaX + vec3.x();
            double deltaYOffset = deltaY + vec3.y();
            double deltaZOffset = deltaZ + vec3.z();
            poseStack.pushPose();
            poseStack.translate(deltaXOffset, deltaYOffset, deltaZOffset);

            poseStack.translate(-vec3.x(), -vec3.y(), -vec3.z());
                    float shadowStrength = 5.0F;
                    float scale = 3F;
                    float alpha = 1F;
                    renderSethan(poseStack, multiBufferSource, castingEntity, shadowStrength, delta, castingEntity.level(), scale, alpha);

            poseStack.popPose();
    }
    private static void renderSethan(PoseStack poseStack, MultiBufferSource multiBufferSource, Entity castingEntity, float shadowStrength, float delta, LevelReader levelReader, float scale, float alpha) {
        float f = scale;
        if (castingEntity instanceof Mob) {
            Mob mobentity = (Mob)castingEntity;
            if (mobentity.isBaby()) {
                f = scale * 0.5F;
            }
        }

        double deltaX = Mth.lerp((double)delta, castingEntity.xOld, castingEntity.getX());
        double deltaY = Mth.lerp((double)delta, castingEntity.yOld, castingEntity.getY());
        double deltaZ = Mth.lerp((double)delta, castingEntity.zOld, castingEntity.getZ());
        int i = Mth.floor(deltaX - (double)f);
        int j = Mth.floor(deltaX + (double)f);
        /*Change this value to make it render down further when high up*/
        int distanceRenderedDown = Mth.floor(deltaY - (double)100);
        int l = Mth.floor(deltaY);
        int i1 = Mth.floor(deltaZ - (double)f);
        int j1 = Mth.floor(deltaZ + (double)f);
        PoseStack.Pose matrixstack$entry = poseStack.last();
        VertexConsumer ivertexbuilder = multiBufferSource.getBuffer(SHADOW_RENDER_TYPE);



        for(BlockPos blockpos : BlockPos.betweenClosed(new BlockPos(i, distanceRenderedDown, i1), new BlockPos(j, l+1, j1))) {
            renderBlockShadow(matrixstack$entry, ivertexbuilder, levelReader, blockpos, deltaX, deltaY, deltaZ, f, scale, alpha);
        }

    }

    private static void renderBlood(PoseStack poseStack, MultiBufferSource multiBufferSource, Entity castingEntity, float shadowStrength, float delta, LevelReader levelReader, float scale, float alpha) {
        float f = scale;
        if (castingEntity instanceof Mob) {
            Mob mobentity = (Mob)castingEntity;
            if (mobentity.isBaby()) {
                f = scale * 0.5F;
            }
        }

        double deltaX = Mth.lerp((double)delta, castingEntity.xOld, castingEntity.getX());
        double deltaY = Mth.lerp((double)delta, castingEntity.yOld, castingEntity.getY());
        double deltaZ = Mth.lerp((double)delta, castingEntity.zOld, castingEntity.getZ());
        int i = Mth.floor(deltaX - (double)f);
        int j = Mth.floor(deltaX + (double)f);
        /*Change this value to make it render down further when high up*/
        int distanceRenderedDown = Mth.floor(deltaY - (double)100);
        int l = Mth.floor(deltaY);
        int i1 = Mth.floor(deltaZ - (double)f);
        int j1 = Mth.floor(deltaZ + (double)f);
        PoseStack.Pose matrixstack$entry = poseStack.last();
        VertexConsumer ivertexbuilder = multiBufferSource.getBuffer(SHADOW_RENDER_TYPE);


        for(BlockPos blockpos : BlockPos.betweenClosed(new BlockPos(i, distanceRenderedDown, i1), new BlockPos(j, l+1, j1))) {
            renderBlockShadow(matrixstack$entry, ivertexbuilder, levelReader, blockpos, deltaX, deltaY, deltaZ, f, scale, alpha);
        }

    }

    private static void renderBlockShadow(PoseStack.Pose pose, VertexConsumer textureCoords, LevelReader levelReader, BlockPos blockPos, double deltaX, double deltaY, double deltaZ, float moddedScale, float scale, float alpha) {
        BlockPos blockpos = blockPos.below();
        BlockState blockstate = levelReader.getBlockState(blockpos);
        if (blockstate.isCollisionShapeFullBlock(levelReader, blockpos)) {
            VoxelShape voxelshape = blockstate.getShape(levelReader, new BlockPos(blockPos.getX(),blockPos.getY()-200,blockPos.getZ()));
            if (!voxelshape.isEmpty()) {
                float f = alpha;
                if (f >= 0.0F) {
                    if (f > 1.0F) {
                        f = 1.0F;
                    }

                    AABB axisalignedbb = voxelshape.bounds();
                    double minX = (double)blockPos.getX() + axisalignedbb.minX;
                    double maxX = (double)blockPos.getX() + axisalignedbb.maxX;
                    double minY = (double)blockPos.getY() + axisalignedbb.minY;
                    double minZ = (double)blockPos.getZ() + axisalignedbb.minZ;
                    double maxZ = (double)blockPos.getZ() + axisalignedbb.maxZ;
                    float f1 = (float)(minX - deltaX);
                    float f2 = (float)(maxX - deltaX);
                    float f3 = (float)(minY - deltaY);
                    float f4 = (float)(minZ - deltaZ);
                    float f5 = (float)(maxZ - deltaZ);
                    float f6 = -f1 / 2.0F / moddedScale + 0.5F;
                    float f7 = -f2 / 2.0F / moddedScale + 0.5F;
                    float f8 = -f4 / 2.0F / moddedScale + 0.5F;
                    float f9 = -f5 / 2.0F / moddedScale + 0.5F;
                    shadowVertex(pose, textureCoords, f, f1, f3, f4, f6, f8);
                    shadowVertex(pose, textureCoords, f, f1, f3, f5, f6, f9);
                    shadowVertex(pose, textureCoords, f, f2, f3, f5, f7, f9);
                    shadowVertex(pose, textureCoords, f, f2, f3, f4, f7, f8);
                }

            }
        }
        //}
    }

    private static void shadowVertex(PoseStack.Pose p_229091_0_, VertexConsumer p_229091_1_, float p_229091_2_, float p_229091_3_, float p_229091_4_, float p_229091_5_, float p_229091_6_, float p_229091_7_) {
        p_229091_1_.vertex(p_229091_0_.pose(), p_229091_3_, p_229091_4_, p_229091_5_).color(1.0F, 1.0F, 1.0F, p_229091_2_).uv(p_229091_6_, p_229091_7_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(p_229091_0_.normal(), 0.0F, 1.0F, 0.0F).endVertex();
    }
}
