package net.hydra.jojomod.client.models.visages.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
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

public class BlockWallEntityRenderer extends EntityRenderer<BlockWallEntity> {
    private final BlockRenderDispatcher dispatcher;

    public BlockWallEntityRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.shadowRadius = 0;
        this.shadowStrength = 0;
        this.dispatcher = $$0.getBlockRenderDispatcher();
    }


    public void render(BlockWallEntity $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
        BlockState $$6 = $$0.getBlockState();
        if ($$6.getRenderShape() == RenderShape.MODEL) {
            Level $$7 = $$0.level();
            if ($$6 != $$7.getBlockState($$0.blockPosition()) && $$6.getRenderShape() != RenderShape.INVISIBLE) {
                $$3.pushPose();
                BlockPos $$8 = BlockPos.containing($$0.getX(), $$0.getBoundingBox().maxY, $$0.getZ());
                $$3.translate((double)-0.5F, (double)0.0F, (double)-0.5F);

                //$$3.scale(0.98F,0.98F,0.98F);
                this.dispatcher.getModelRenderer().tesselateBlock($$7,
                        this.dispatcher.getBlockModel($$6),
                        $$6, $$8, $$3,
                        $$4.getBuffer(ItemBlockRenderTypes.getMovingBlockRenderType($$6)),
                        false, RandomSource.create(), $$6.getSeed($$0.getStartPos()), OverlayTexture.NO_OVERLAY);
                $$3.popPose();
                super.render($$0, $$1, $$2, $$3, $$4, $$5);
            }
        }
    }

    public ResourceLocation getTextureLocation(BlockWallEntity $$0) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
