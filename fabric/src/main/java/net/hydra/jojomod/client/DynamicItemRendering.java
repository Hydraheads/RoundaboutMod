package net.hydra.jojomod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.hydra.jojomod.block.handBlock.HandBlock;
import net.hydra.jojomod.block.handBlock.HandBlockEntity;
import net.hydra.jojomod.client.models.CoffinModel;
import net.hydra.jojomod.client.models.CoffinRenderer;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.registry.FabricBlocks;
import net.hydra.jojomod.registry.FabricItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DynamicItemRendering implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    public static final DynamicItemRendering INSTANCE = new DynamicItemRendering();

    private final Minecraft client = Minecraft.getInstance();

    private final HandBlockEntity HAND_BLOCK_ENTITY =
            new HandBlockEntity(BlockPos.ZERO, FabricBlocks.HAND.defaultBlockState());

    private CoffinModel coffinModel;

    @Override
    public void render(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        Item item = stack.getItem();
        if (item instanceof BlockItem BItem) {
            Block block = BItem.getBlock();
            BlockEntity blockEntity;
            /// If someone is willing to do, maybe we could turn this into a Map? so we don't end up with a giant if statement.

            if (block instanceof HandBlock) {
                blockEntity = HAND_BLOCK_ENTITY;
                ((HandBlockEntity) blockEntity).setStoredStack(stack);
            } else if (stack.is(FabricItems.COFFIN_BLOCK_ITEM)) {
                if(coffinModel == null) {
                    coffinModel = new CoffinModel(client.getEntityModels().bakeLayer(ModEntityRendererClient.COFFIN_FULL_LAYER));
                }

                matrices.pushPose();
                matrices.scale(1.0F, -1.0F, -1.0F);
                matrices.translate(0.85, -1, -1.0);
                VertexConsumer vertexconsumer1 = ItemRenderer.getFoilBufferDirect(vertexConsumers, this.coffinModel.renderType(CoffinRenderer.COFFIN), false, stack.hasFoil());
                this.coffinModel.renderToBuffer(matrices, vertexconsumer1, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
                matrices.popPose();
                return;
            }else {
                return;
            }

            client.getBlockEntityRenderDispatcher().renderItem(blockEntity, matrices, vertexConsumers, light, overlay);
        }

    }
}
