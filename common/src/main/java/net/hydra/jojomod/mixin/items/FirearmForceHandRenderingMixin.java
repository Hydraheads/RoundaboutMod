package net.hydra.jojomod.mixin.items;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.item.FirearmItem;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class FirearmForceHandRenderingMixin {

    @Shadow
    protected abstract void renderPlayerArm(PoseStack $$0, MultiBufferSource $$1, int $$2, float $$3, float $$4, HumanoidArm $$5);

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    private void forceEmptyHandWhileAiming(AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equippedProgress, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, CallbackInfo ci) {
        if (player.isUsingItem() && player.getUseItem().getItem() instanceof FirearmItem) {
            ci.cancel();
            HumanoidArm mainArm = player.getMainArm();
            HumanoidArm offArm = mainArm.getOpposite();

            this.renderPlayerArm(poseStack, buffer, combinedLight, equippedProgress, swingProgress, mainArm);

            this.renderPlayerArm(poseStack, buffer, combinedLight, equippedProgress, swingProgress, offArm);
        }
    }
}
