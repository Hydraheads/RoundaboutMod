package com.example.jjf.forge.network;

import com.example.jjf.zero_gravity.ZeroGravitySync;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public final class ForgeJJFNetwork {

    private static final String PROTOCOL_VERSION = "1";

    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("jjf", "network"),
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
                ForgeJJFNetwork::handle
        );

        ZeroGravitySync.registerSender((player, snapshot) ->
                CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), snapshot));
    }

    private static void handle(ZeroGravitySync.Snapshot snapshot,
                               Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
                ForgeJJFClientPackets.apply(snapshot);
            }
        });
        context.setPacketHandled(true);
    }
}
