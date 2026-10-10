package net.hydra.jojomod.mixin.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.block.handBlock.HandBlock;
import net.hydra.jojomod.block.handBlock.HandBlockEntity;
import net.hydra.jojomod.registry.ForgeBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


//@Mixin(BlockEntityWithoutLevelRenderer.class)
public class DynamicItemRenderingMixin  {

    //public static final DynamicItemRendering INSTANCE = new DynamicItemRendering();

    private final Minecraft client = Minecraft.getInstance();
    /*private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    private final EntityModelSet entityModelSet;*/

    private final HandBlockEntity HAND_BLOCK_ENTITY =
            new HandBlockEntity(BlockPos.ZERO, ForgeBlocks.HAND_BLOCK.get().defaultBlockState());
    /*
    public DynamicItemRenderingMixin(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_) {
        blockEntityRenderDispatcher = p_172550_;
        entityModelSet = p_172551_;
    }
    */

    /*
   @Override
   public void renderByItem(ItemStack itemStack, ItemDisplayContext ctx, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {

   */
   // @Inject(method = "renderByItem", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$renderByItem(ItemStack itemStack, ItemDisplayContext ctx, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, CallbackInfo ci) {
        Item item = itemStack.getItem();
        if (item instanceof BlockItem BItem) {
            Block block = BItem.getBlock();
            BlockEntity blockEntity;

            /// If someone is willing to do, maybe we could turn this into a Map? so we don't end up with a giant if statement.

            if (block instanceof HandBlock) {
                blockEntity = HAND_BLOCK_ENTITY;
                ((HandBlockEntity)blockEntity).setStoredStack(itemStack);
            }else {
                return;
            }

            Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(blockEntity, poseStack, bufferSource, combinedLight, combinedOverlay);
        }

    }

}
