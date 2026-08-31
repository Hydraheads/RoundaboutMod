package net.hydra.jojomod.mixin.whitesnake.inventory;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.event.powers.whitesnake.WhitesnakeControlInventory;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public abstract class WhitesnakePlayerHeldItemMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$hideControlItems(PoseStack poseStack, MultiBufferSource buffers,
                                                       int light, LivingEntity entity, float limbSwing,
                                                       float limbSwingAmount, float partialTick,
                                                       float ageInTicks, float netHeadYaw, float headPitch,
                                                       CallbackInfo ci) {
        if (entity instanceof Player player && WhitesnakeControlInventory.isActive(player)) {
            ci.cancel();
        }
    }
}
