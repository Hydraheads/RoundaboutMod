package net.hydra.jojomod.mixin.items;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.item.FirearmItem;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class FirearmForceHandRenderingMixin {

    @Shadow
    public abstract void renderHandsWithItems(float partialTicks, PoseStack poseStack, MultiBufferSource.BufferSource buffer, LocalPlayer player, int light);

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    private void forceBothArmsWhileAiming(AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equippedProgress, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, CallbackInfo ci) {

        if (player.isUsingItem() && player.getUseItem().getItem() instanceof FirearmItem) {
            ci.cancel();

            this.renderHandsWithItems(partialTicks, poseStack, (MultiBufferSource.BufferSource) buffer, (LocalPlayer) player, combinedLight);
        }
    }
}
