package net.hydra.jojomod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.hydra.jojomod.block.CoffinBlockEntity;
import net.hydra.jojomod.block.handBlock.HandBlock;
import net.hydra.jojomod.block.handBlock.HandBlockEntity;
import net.hydra.jojomod.registry.FabricBlocks;
import net.hydra.jojomod.registry.FabricItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DynamicItemRendering implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    public static final DynamicItemRendering INSTANCE = new DynamicItemRendering();

    private final Minecraft client = Minecraft.getInstance();

    private final HandBlockEntity HAND_BLOCK_ENTITY =
            new HandBlockEntity(BlockPos.ZERO, FabricBlocks.HAND.defaultBlockState());
    private final CoffinBlockEntity COFFIN_BLOCK_ENTITY =
            new CoffinBlockEntity(BlockPos.ZERO, FabricBlocks.COFFIN_BLOCK.defaultBlockState());

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
                blockEntity = COFFIN_BLOCK_ENTITY;
            }else {
                return;
            }

            client.getBlockEntityRenderDispatcher().renderItem(blockEntity, matrices, vertexConsumers, light, overlay);
        }

    }
}
