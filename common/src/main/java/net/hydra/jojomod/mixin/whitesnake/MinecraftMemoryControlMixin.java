package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.event.powers.disc.DiscItemData;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMemoryControlMixin {
    @Shadow public LocalPlayer player;

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$blockAttack(CallbackInfoReturnable<Boolean> cir) {
        if (player != null && !DiscItemData.hasPlayerControl(player)) cir.setReturnValue(false);
    }

    @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$blockBreaking(boolean attacking, CallbackInfo ci) {
        if (player != null && !DiscItemData.hasPlayerControl(player)) ci.cancel();
    }

    @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$blockUse(CallbackInfo ci) {
        if (player != null
                && ((StandUser) player).roundabout$getStandPowers() instanceof PowersWhitesnake powers
                && powers.isAutoMode()
                && (player.getMainHandItem().isEdible() || player.getOffhandItem().isEdible())) {
            ci.cancel();
            return;
        }
        if (player != null && !DiscItemData.hasPlayerControl(player)
                && !DiscItemData.canSelfImplantHeldMemory(player)) ci.cancel();
    }

    @Inject(method = "pickBlock", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$blockPick(CallbackInfo ci) {
        if (player != null && !DiscItemData.hasPlayerControl(player)) ci.cancel();
    }
}
