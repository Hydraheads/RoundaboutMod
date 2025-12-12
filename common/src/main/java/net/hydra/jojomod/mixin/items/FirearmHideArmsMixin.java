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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class FirearmHideArmsMixin {

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    private void hideArmWithItem(AbstractClientPlayer player, float p_109347_, float p_109348_, InteractionHand hand, float p_109350_, ItemStack stack, float p_109352_, PoseStack poseStack, MultiBufferSource buffer, int light, CallbackInfo ci) {
        if (player.isUsingItem() && player.getUseItem().getItem() instanceof FirearmItem) {
            ci.cancel();
        }
    }

    @Inject(method = "renderPlayerArm", at = @At("HEAD"), cancellable = true)
    private void hideOtherArm(PoseStack $$0, MultiBufferSource $$1, int $$2, float $$3, float $$4, HumanoidArm $$5, CallbackInfo ci) {
        ItemStack itemStack = net.minecraft.client.Minecraft.getInstance().player.getUseItem();
        if (itemStack.getItem() instanceof FirearmItem) {
            ci.cancel();
        }
    }
}
