package net.hydra.jojomod.client.models.visages.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.entity.BlockD4CEntity;
import net.hydra.jojomod.entity.BlockWallEntity;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class BlockD4CEntityRenderer extends EntityRenderer<BlockD4CEntity> {
    private final BlockRenderDispatcher dispatcher;

    public BlockD4CEntityRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.shadowRadius = 0;
        this.shadowStrength = 0;
        this.dispatcher = $$0.getBlockRenderDispatcher();
    }


    public void render(
            BlockD4CEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        BlockState state = entity.getBlockState();

        if (state.getRenderShape() == RenderShape.MODEL) {
            Level level = entity.level();

            if (state != level.getBlockState(entity.blockPosition())
                    && state.getRenderShape() != RenderShape.INVISIBLE) {

                poseStack.pushPose();

                float scale = Math.min(
                        (((float) entity.existTime) / 12)
                                + (partialTick * 0.05F)
                                + 0.01F,
                        1F
                );

                poseStack.scale(scale, scale, scale);

                // Center the block
                poseStack.translate(0.0D, 0.5D, 0.0D);

                float time = entity.existTime + partialTick;

                // Several different frequencies create chaotic motion
                float rotX =
                        (float) Math.sin(time * 0.17F) * 35.0F
                                + (float) Math.sin(time * 0.071F) * 20.0F
                                + (float) Math.sin(time * 0.031F) * 15.0F;

                float rotY =
                        (float) Math.sin(time * 0.113F) * 70.0F
                                + (float) Math.sin(time * 0.043F) * 40.0F
                                + (float) Math.sin(time * 0.019F) * 25.0F;

                float rotZ =
                        (float) Math.sin(time * 0.151F) * 45.0F
                                + (float) Math.sin(time * 0.059F) * 25.0F
                                + (float) Math.sin(time * 0.023F) * 20.0F;

                poseStack.mulPose(Axis.XP.rotationDegrees(rotX));
                poseStack.mulPose(Axis.YP.rotationDegrees(rotY));
                poseStack.mulPose(Axis.ZP.rotationDegrees(rotZ));

                // Move back down after rotating around center
                poseStack.translate(0.0D, -0.5D, 0.0D);

                BlockPos pos = BlockPos.containing(
                        entity.getX(),
                        entity.getBoundingBox().maxY,
                        entity.getZ()
                );

                poseStack.translate(-0.5D, 0.0D, -0.5D);
                poseStack.translate(0.01F, -0.01F, 0.01F);

                this.dispatcher.getModelRenderer().tesselateBlock(
                        level,
                        this.dispatcher.getBlockModel(state),
                        state,
                        pos,
                        poseStack,
                        buffer.getBuffer(
                                ItemBlockRenderTypes.getMovingBlockRenderType(state)
                        ),
                        false,
                        RandomSource.create(),
                        state.getSeed(entity.getStartPos()),
                        OverlayTexture.NO_OVERLAY
                );

                poseStack.popPose();

                super.render(
                        entity,
                        entityYaw,
                        partialTick,
                        poseStack,
                        buffer,
                        packedLight
                );
            }
        }
    }

    public ResourceLocation getTextureLocation(BlockD4CEntity $$0) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
