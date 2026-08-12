package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.event.powers.disc.DiscItemData;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.client.WhitesnakeControlClient;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MemoryMouseHandler {
    @Inject(method = "turnPlayer()V", at = @At("HEAD"), cancellable = true)
    private void roundabout$memoryControlsView(CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && !DiscItemData.hasPlayerControl(minecraft.player)) {
            ci.cancel();
        }
    }

    //Redirect(method = "turnPlayer()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"))
    private void roundaboutWhitesnake$turnCamera(LocalPlayer player, double yawDelta, double pitchDelta) {
        if (((StandUser) player).roundabout$getStandPowers() instanceof PowersWhitesnake powers
                && powers.isPiloting()) {
            WhitesnakeControlClient.turnCamera(powers.getPilotingStand(), yawDelta, pitchDelta);
        } else {
            player.turn(yawDelta, pitchDelta);
        }
    }
}
