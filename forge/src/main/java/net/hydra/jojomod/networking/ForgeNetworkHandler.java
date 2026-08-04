package net.hydra.jojomod.networking;

import net.hydra.jojomod.Roundabout;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ForgeNetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Roundabout.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int index;

    public static synchronized void register() {
        INSTANCE.messageBuilder(ClientboundBlackSabbathOpenPacket.class, index++)
                .encoder(ClientboundBlackSabbathOpenPacket::encode)
                .decoder(ClientboundBlackSabbathOpenPacket::decode)
                .consumerMainThread(ClientboundBlackSabbathOpenPacket::handle).add();
    }

    public static <MSG> void sendToClient(MSG message, ServerPlayer serverPlayer) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), message);
    }
}
