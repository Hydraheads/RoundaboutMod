package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.stand.powers.WhitesnakeControlInventory;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public abstract class WhitesnakePlayerArmPoseMixin {
    @Inject(method = "getArmPose(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;",
            at = @At("HEAD"), cancellable = true)
    private static void roundaboutWhitesnake$emptyControlHand(AbstractClientPlayer player, InteractionHand hand,
                                                              CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
        if (hand == InteractionHand.MAIN_HAND && WhitesnakeControlInventory.isActive(player)) {
            cir.setReturnValue(HumanoidModel.ArmPose.EMPTY);
        }
    }
}
