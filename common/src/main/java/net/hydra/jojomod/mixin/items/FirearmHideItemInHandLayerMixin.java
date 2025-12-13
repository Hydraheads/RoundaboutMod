package net.hydra.jojomod.mixin.items;

import com.mojang.blaze3d.vertex.PoseStack;
import jdk.jfr.Frequency;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.item.BowlerHatItem;
import net.hydra.jojomod.item.FirearmItem;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class FirearmHideItemInHandLayerMixin {

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)

    private void hideItemsWhenAiming(AbstractClientPlayer $$0, float $$1, float $$2, InteractionHand $$3, float $$4, ItemStack $$5, float $$6, PoseStack $$7, MultiBufferSource $$8, int $$9, CallbackInfo ci) {
        if ($$0.isUsingItem() && $$0.getUseItem().getItem() instanceof FirearmItem) {
            ci.cancel();
        }
    }
}