package net.hydra.jojomod.mixin.silver_chariot;

import net.hydra.jojomod.client.SilverChariotClient;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersSilverChariot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Minecraft.class, priority = 2000)
public abstract class SilverChariotControlMinecraftMixin {
    @Inject(method = "handleKeybinds", at = @At("HEAD"))
    private void roundaboutSilverChariot$disableOffhandSwap(CallbackInfo ci) {
        Minecraft minecraft = (Minecraft) (Object) this;
        if (minecraft.player != null
                && ((StandUser) minecraft.player).roundabout$getStandPowers() instanceof PowersSilverChariot powers
                && powers.isPiloting()) {
            while (minecraft.options.keySwapOffhand.consumeClick()) {
            }
        }
    }

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void roundaboutSilverChariot$startMining(CallbackInfoReturnable<Boolean> cir) {
        if (SilverChariotClient.tryMining((Minecraft) (Object) this)) cir.setReturnValue(false);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void roundaboutSilverChariot$tickMining(CallbackInfo ci) {
        Minecraft minecraft = (Minecraft) (Object) this;
        boolean attackHeld = minecraft.screen == null && minecraft.options.keyAttack.isDown()
                && minecraft.mouseHandler.isMouseGrabbed();
        SilverChariotClient.handleMining(minecraft, attackHeld);
    }

    @Inject(method = "handleKeybinds", at = @At("TAIL"))
    private void roundaboutSilverChariot$forceThirdPerson(CallbackInfo ci) {
        Minecraft minecraft = (Minecraft) (Object) this;
        if (minecraft.player != null
                && ((StandUser) minecraft.player).roundabout$getStandPowers() instanceof PowersSilverChariot powers
                && powers.isPiloting()) {
            SilverChariotClient.enforceCamera(powers.getPilotingStand());
        }
    }

    @Inject(method = "clearLevel()V", at = @At("HEAD"))
    private void roundaboutSilverChariot$clearLevel(CallbackInfo ci) {
        SilverChariotClient.clear();
    }

    @Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("HEAD"))
    private void roundaboutSilverChariot$clearLevel(Screen screen, CallbackInfo ci) {
        SilverChariotClient.clear();
    }
}
