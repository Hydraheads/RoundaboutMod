package net.hydra.jojomod.networking;

import net.hydra.jojomod.event.powers.jumpingJackFlash.LauncherAmmoSync;
import net.hydra.jojomod.event.powers.jumpingJackFlash.ZeroGravitySync;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public final class ForgeJJFNetwork {

    private static final String PROTOCOL_VERSION = "2";

    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("roundabout", "jjf_network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private ForgeJJFNetwork() {
    }

    public static void register() {
        CHANNEL.registerMessage(
                0,
                ZeroGravitySync.Snapshot.class,
                (snapshot, buffer) -> ZeroGravitySync.write(buffer, snapshot),
                ZeroGravitySync::read,
                ForgeJJFNetwork::handleGravity
        );

        CHANNEL.registerMessage(
                1,
                LauncherAmmoSync.Snapshot.class,
                (snapshot, buffer) -> LauncherAmmoSync.write(buffer, snapshot),
                LauncherAmmoSync::read,
                ForgeJJFNetwork::handleAmmo
        );

        ZeroGravitySync.registerSender((player, snapshot) ->
                CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), snapshot));
        LauncherAmmoSync.registerSender((player, snapshot) ->
                CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), snapshot));
    }

    private static void handleGravity(ZeroGravitySync.Snapshot snapshot,
                                      Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            context.enqueueWork(() -> ForgeJJFClientPackets.apply(snapshot));
        }
        context.setPacketHandled(true);
    }

    private static void handleAmmo(LauncherAmmoSync.Snapshot snapshot,
                                   Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            context.enqueueWork(() -> ForgeJJFClientPackets.apply(snapshot));
        }
        context.setPacketHandled(true);
    }
}
