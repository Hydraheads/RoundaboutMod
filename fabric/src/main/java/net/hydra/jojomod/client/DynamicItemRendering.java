package net.hydra.jojomod.client;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.block.handBlock.HandBlock;
import net.hydra.jojomod.block.handBlock.HandBlockEntity;
import net.hydra.jojomod.client.models.HandRenderer;
import net.hydra.jojomod.registry.FabricBlocks;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;

public class DynamicItemRendering implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    public static final DynamicItemRendering INSTANCE = new DynamicItemRendering();

    private final Minecraft client = Minecraft.getInstance();

    private final HandBlockEntity HAND_BLOCK_ENTITY =
            new HandBlockEntity(BlockPos.ZERO, FabricBlocks.HAND.defaultBlockState());

    @Override
    public void render(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        Item item = stack.getItem();
        if (item instanceof BlockItem BItem) {
            Block block = BItem.getBlock();
            BlockEntity blockEntity;

            /// If someone is willing to do, maybe we could turn this into a Map? so we don't end up with a giant if statement.

            if (block instanceof HandBlock) {
                //blockEntity = HAND_BLOCK_ENTITY;
                /* in my situation is better to call a method in HandRender because of skins,
                 but in most cases using the block entity with the 'renderItem' is good enough
                 */
                GameProfile gameProfile2 = null;
                if (stack.hasTag()) {
                    CompoundTag compoundTag = stack.getTag();
                    if (compoundTag.contains("HandProfile")) {
                        gameProfile2 = NbtUtils.readGameProfile(compoundTag.getCompound("HandProfile"));
                    }
                }
                //HandRenderer.renderHand(gameProfile2, FabricBlocks.HAND.defaultBlockState(), matrices, vertexConsumers, light, overlay);
                return;
            }else {
                return;
            }
            /// uncomment it for other cases of blocks.

            //client.getBlockEntityRenderDispatcher().renderItem(blockEntity, matrices, vertexConsumers, light, overlay);
        }

    }
}
