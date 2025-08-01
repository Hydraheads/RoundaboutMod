package net.hydra.jojomod.networking;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.networking.c2s.*;
import net.hydra.jojomod.networking.s2c.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ForgePacketHandler {
    public static SimpleChannel INSTANCE;

    private static int packetId = 0;
    public static int id(){
        return packetId++;
    }

    public static void register(){
        INSTANCE = NetworkRegistry.ChannelBuilder.named(
                new ResourceLocation(Roundabout.MOD_ID, "main"))
            .networkProtocolVersion(()->"1.0")
            .serverAcceptedVersions(s-> true)
            .clientAcceptedVersions(s-> true)
            .simpleChannel();

        /**Client to Server Packets*/

        /**Server to Client Packets*/
        INSTANCE.messageBuilder(ForgeGenericIntPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeGenericIntPacket::new)
                .encoder(ForgeGenericIntPacket::toBytes)
                .consumerMainThread(ForgeGenericIntPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeTimeStoppingEntityPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeTimeStoppingEntityPacket::new)
                .encoder(ForgeTimeStoppingEntityPacket::toBytes)
                .consumerMainThread(ForgeTimeStoppingEntityPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeTimeStoppingEntityRemovalPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeTimeStoppingEntityRemovalPacket::new)
                .encoder(ForgeTimeStoppingEntityRemovalPacket::toBytes)
                .consumerMainThread(ForgeTimeStoppingEntityRemovalPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeBlockEntityResumeTSPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeBlockEntityResumeTSPacket::new)
                .encoder(ForgeBlockEntityResumeTSPacket::toBytes)
                .consumerMainThread(ForgeBlockEntityResumeTSPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgePowerFloatPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgePowerFloatPacket::new)
                .encoder(ForgePowerFloatPacket::toBytes)
                .consumerMainThread(ForgePowerFloatPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeSimpleBytePacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeSimpleBytePacket::new)
                .encoder(ForgeSimpleBytePacket::toBytes)
                .consumerMainThread(ForgeSimpleBytePacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeBundlePacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeBundlePacket::new)
                .encoder(ForgeBundlePacket::toBytes)
                .consumerMainThread(ForgeBundlePacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgePermaCastingEntityPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgePermaCastingEntityPacket::new)
                .encoder(ForgePermaCastingEntityPacket::toBytes)
                .consumerMainThread(ForgePermaCastingEntityPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgePermaCastingEntityRemovalPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgePermaCastingEntityRemovalPacket::new)
                .encoder(ForgePermaCastingEntityRemovalPacket::toBytes)
                .consumerMainThread(ForgePermaCastingEntityRemovalPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeDynamicWorldSync.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeDynamicWorldSync::new)
                .encoder(ForgeDynamicWorldSync::toBytes)
                .consumerMainThread(ForgeDynamicWorldSync::handle)
                .add();
        INSTANCE.messageBuilder(ForgeEjectPRunning.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeEjectPRunning::new)
                .encoder(ForgeEjectPRunning::toBytes)
                .consumerMainThread(ForgeEjectPRunning::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message){
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToClient(MSG message, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
