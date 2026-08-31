package net.hydra.jojomod.mixin.whitesnake.control;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.client.WhitesnakeControlClient;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Minecraft.class, priority = 2000)
public abstract class WhitesnakeControlMinecraftMixin {
    @Inject(method = "handleKeybinds", at = @At("HEAD"))
    private void roundaboutWhitesnake$disableOffhandSwap(CallbackInfo ci) {
        Minecraft minecraft = (Minecraft) (Object) this;
        if (minecraft.player != null
                && ((StandUser) minecraft.player).roundabout$getStandPowers() instanceof PowersWhitesnake powers
                && powers.isPiloting()) {
            while (minecraft.options.keySwapOffhand.consumeClick()) {
            }
        }
    }

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$startMining(CallbackInfoReturnable<Boolean> cir) {
        if (WhitesnakeControlClient.tryMining((Minecraft) (Object) this)) cir.setReturnValue(false);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void roundaboutWhitesnake$tickMining(CallbackInfo ci) {
        Minecraft minecraft = (Minecraft) (Object) this;
        boolean attackHeld = minecraft.screen == null && minecraft.options.keyAttack.isDown()
                && minecraft.mouseHandler.isMouseGrabbed();
        WhitesnakeControlClient.handleMining(minecraft, attackHeld);
    }

    @Inject(method = "handleKeybinds", at = @At("TAIL"))
    private void roundaboutWhitesnake$forceThirdPerson(CallbackInfo ci) {
        Minecraft minecraft = (Minecraft) (Object) this;
        if (minecraft.player != null
                && ((StandUser) minecraft.player).roundabout$getStandPowers() instanceof PowersWhitesnake powers
                && powers.isPiloting()) {
            WhitesnakeControlClient.enforceCamera(powers.getPilotingStand());
        }
    }

    @Inject(method = "clearLevel()V", at = @At("HEAD"))
    private void roundaboutWhitesnake$clearLevel(CallbackInfo ci) {
        WhitesnakeControlClient.clear();
    }

    @Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("HEAD"))
    private void roundaboutWhitesnake$clearLevel(Screen screen, CallbackInfo ci) {
        WhitesnakeControlClient.clear();
    }
}
