package net.hydra.jojomod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.CoffinBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.block.handBlock.HandBlock;
import net.hydra.jojomod.block.handBlock.HandBlockEntity;
import net.hydra.jojomod.client.models.CoffinModel;
import net.hydra.jojomod.client.models.CoffinRenderer;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.registry.ForgeBlocks;
import net.hydra.jojomod.registry.ForgeItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DynamicItemRendering extends BlockEntityWithoutLevelRenderer {

    public static DynamicItemRendering INSTANCE = null;

    public static DynamicItemRendering getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DynamicItemRendering(
                    Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                    Minecraft.getInstance().getEntityModels()
            );
        }
        return INSTANCE;
    }

    private CoffinModel coffinModel;

    private final Minecraft client = Minecraft.getInstance();
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    private final EntityModelSet entityModelSet;


    private final HandBlockEntity HAND_BLOCK_ENTITY =
            new HandBlockEntity(BlockPos.ZERO, ForgeBlocks.HAND_BLOCK.get().defaultBlockState());

    public DynamicItemRendering(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_) {
        super(p_172550_, p_172551_);
        blockEntityRenderDispatcher = p_172550_;
        entityModelSet = p_172551_;
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext ctx, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        Item item = itemStack.getItem();
        if (item instanceof BlockItem BItem) {
            Block block = BItem.getBlock();
            BlockEntity blockEntity;

            /// If someone is willing to do, maybe we could turn this into a Map? so we don't end up with a giant if statement.

            if (block instanceof HandBlock) {
                blockEntity = HAND_BLOCK_ENTITY;
                ((HandBlockEntity)blockEntity).setStoredStack(itemStack);
            } else if (block instanceof CoffinBlock) {
                if(coffinModel == null) {
                    coffinModel = new CoffinModel(entityModelSet.bakeLayer(ModEntityRendererClient.COFFIN_FULL_LAYER));
                }

                poseStack.pushPose();
                poseStack.scale(1.0F, -1.0F, -1.0F);
                poseStack.translate(0.85, -1, -1.0);
                VertexConsumer vertexconsumer1 = ItemRenderer.getFoilBufferDirect(bufferSource, this.coffinModel.renderType(CoffinRenderer.COFFIN), false, itemStack.hasFoil());
                this.coffinModel.renderToBuffer(poseStack, vertexconsumer1, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                poseStack.popPose();
                return;
            }else {
                return;
            }

            blockEntityRenderDispatcher.renderItem(blockEntity, poseStack, bufferSource, combinedLight, combinedOverlay);
        }

    }

}
