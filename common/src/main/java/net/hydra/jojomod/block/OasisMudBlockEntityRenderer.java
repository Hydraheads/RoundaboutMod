package net.hydra.jojomod.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public class OasisMudBlockEntityRenderer implements BlockEntityRenderer<OasisMudBlockEntity> {

    private final BlockRenderDispatcher renderBlock;

    public OasisMudBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super();
        this.renderBlock = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(OasisMudBlockEntity blockEntity, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        BlockState copiedBlockState = blockEntity.getCopiedState();
        if (copiedBlockState != null) {
            this.renderBlock.renderSingleBlock(copiedBlockState, poseStack, bufferSource, packedLight, packedOverlay);
        }
    }

    public void renderEachFace(BlockAndTintGetter btg) {

    }
}