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
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MouseHandler.class)
public abstract class MemoryMouseHandler {
    @Inject(method = "turnPlayer()V", at = @At("HEAD"), cancellable = true)
    private void roundabout$memoryControlsView(CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && !DiscItemData.hasPlayerControl(minecraft.player)) {
            ci.cancel();
        }
    }

    @ModifyArgs(method = "turnPlayer()V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"))
    private void roundaboutWhitesnake$turnCamera(Args args) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null
                || !(((StandUser) player).roundabout$getStandPowers() instanceof PowersWhitesnake powers)
                || !powers.isPiloting()) return;
        Entity stand = powers.getPilotingStand();
        if (stand == null) return;

        WhitesnakeControlClient.turnCamera(stand, args.get(0), args.get(1));
        args.set(0, 0.0D);
        args.set(1, 0.0D);
    }
}
