package net.hydra.jojomod.networking;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hydra.jojomod.event.powers.jumpingJackFlash.LauncherAmmoSync;
import net.hydra.jojomod.event.powers.jumpingJackFlash.ZeroGravitySync;
import net.minecraft.network.FriendlyByteBuf;

public final class FabricJJFNetworking {
    private FabricJJFNetworking() {
    }

    public static void register() {
        ZeroGravitySync.registerSender((player, snapshot) -> {
            FriendlyByteBuf buffer = PacketByteBufs.create();
            ZeroGravitySync.write(buffer, snapshot);
            ServerPlayNetworking.send(player, ZeroGravitySync.CHANNEL, buffer);
        });
        LauncherAmmoSync.registerSender((player, snapshot) -> {
            FriendlyByteBuf buffer = PacketByteBufs.create();
            LauncherAmmoSync.write(buffer, snapshot);
            ServerPlayNetworking.send(player, LauncherAmmoSync.CHANNEL, buffer);
        });
    }
}
