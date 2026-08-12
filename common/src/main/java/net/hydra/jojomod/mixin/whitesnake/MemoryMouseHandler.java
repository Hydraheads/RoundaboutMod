package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.event.powers.disc.DiscItemData;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.client.WhitesnakeControlClient;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
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

    @ModifyArg(method = "turnPlayer()V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"), index = 0)
    private double roundaboutWhitesnake$turnCameraYaw(double yawDelta) {
        Entity stand = roundaboutWhitesnake$getControlledStand();
        if (stand == null) return yawDelta;
        WhitesnakeControlClient.turnCamera(stand, yawDelta, 0.0D);
        return 0.0D;
    }

    @ModifyArg(method = "turnPlayer()V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"), index = 1)
    private double roundaboutWhitesnake$turnCameraPitch(double pitchDelta) {
        Entity stand = roundaboutWhitesnake$getControlledStand();
        if (stand == null) return pitchDelta;
        WhitesnakeControlClient.turnCamera(stand, 0.0D, pitchDelta);
        return 0.0D;
    }

    private Entity roundaboutWhitesnake$getControlledStand() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null
                || !(((StandUser) player).roundabout$getStandPowers() instanceof PowersWhitesnake powers)
                || !powers.isPiloting()) return null;
        return powers.getPilotingStand();
    }
}
