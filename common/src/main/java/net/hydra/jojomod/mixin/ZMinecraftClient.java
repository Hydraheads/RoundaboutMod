package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.zetalasis.networking.packet.api.IClientNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class ZMinecraftClient implements IClientNetworking {
    @Shadow @Nullable public LocalPlayer player;

    @Shadow @Nullable private Connection pendingConnection;

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void roundabout$startAttack(CallbackInfoReturnable<Boolean> cir)
    {
        if (player == null)
            return;

        if (((StandUser)player).roundabout$isParallelRunning())
        {
            player.swing(InteractionHand.MAIN_HAND);
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Override
    public @Nullable Connection roundabout$getServer() {
        // note: pendingConnection is actually just the connection to the integrated server **only**
        return this.pendingConnection;
    }
}