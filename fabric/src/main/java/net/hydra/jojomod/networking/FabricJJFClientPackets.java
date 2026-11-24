package net.hydra.jojomod.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.hydra.jojomod.event.powers.jumpingJackFlash.LauncherAmmoSync;
import net.hydra.jojomod.event.powers.jumpingJackFlash.ZeroGravitySync;

public final class FabricJJFClientPackets {
    private FabricJJFClientPackets() {
    }

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ZeroGravitySync.CHANNEL,
                (client, handler, buffer, responseSender) -> {
                    ZeroGravitySync.Snapshot snapshot = ZeroGravitySync.read(buffer);
                    client.execute(() -> {
                        if (client.player != null) {
                            ZeroGravitySync.applyClient(client.player, snapshot);
                        }
                    });
                });
        ClientPlayNetworking.registerGlobalReceiver(LauncherAmmoSync.CHANNEL,
                (client, handler, buffer, responseSender) -> {
                    LauncherAmmoSync.Snapshot snapshot = LauncherAmmoSync.read(buffer);
                    client.execute(() -> {
                        if (client.player != null) {
                            LauncherAmmoSync.applyClient(client.player, snapshot);
                        }
                    });
                });
    }
}
