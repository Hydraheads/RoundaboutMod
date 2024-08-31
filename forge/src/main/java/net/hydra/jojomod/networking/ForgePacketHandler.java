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
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id(){
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
        INSTANCE.messageBuilder(ForgeMoveSyncPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ForgeMoveSyncPacket::new)
                .encoder(ForgeMoveSyncPacket::toBytes)
                .consumerMainThread(ForgeMoveSyncPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeTSJumpPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ForgeTSJumpPacket::new)
                .encoder(ForgeTSJumpPacket::toBytes)
                .consumerMainThread(ForgeTSJumpPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeSummonPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ForgeSummonPacket::new)
                .encoder(ForgeSummonPacket::toBytes)
                .consumerMainThread(ForgeSummonPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeSwitchPowerPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ForgeSwitchPowerPacket::new)
                .encoder(ForgeSwitchPowerPacket::toBytes)
                .consumerMainThread(ForgeSwitchPowerPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgePosPowerPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ForgePosPowerPacket::new)
                .encoder(ForgePosPowerPacket::toBytes)
                .consumerMainThread(ForgePosPowerPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeChargedPowerPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ForgeChargedPowerPacket::new)
                .encoder(ForgeChargedPowerPacket::toBytes)
                .consumerMainThread(ForgeChargedPowerPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgePunchPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ForgePunchPacket::new)
                .encoder(ForgePunchPacket::toBytes)
                .consumerMainThread(ForgePunchPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeBarrageHitPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ForgeBarrageHitPacket::new)
                .encoder(ForgeBarrageHitPacket::toBytes)
                .consumerMainThread(ForgeBarrageHitPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeGuardCancelPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ForgeGuardCancelPacket::new)
                .encoder(ForgeGuardCancelPacket::toBytes)
                .consumerMainThread(ForgeGuardCancelPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeClashUpdatePacketC2S.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ForgeClashUpdatePacketC2S::new)
                .encoder(ForgeClashUpdatePacketC2S::toBytes)
                .consumerMainThread(ForgeClashUpdatePacketC2S::handle)
                .add();
        INSTANCE.messageBuilder(ForgeByteC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ForgeByteC2SPacket::new)
                .encoder(ForgeByteC2SPacket::toBytes)
                .consumerMainThread(ForgeByteC2SPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeFloatC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ForgeFloatC2SPacket::new)
                .encoder(ForgeFloatC2SPacket::toBytes)
                .consumerMainThread(ForgeFloatC2SPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeIntC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ForgeIntC2SPacket::new)
                .encoder(ForgeIntC2SPacket::toBytes)
                .consumerMainThread(ForgeIntC2SPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeGlaivePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ForgeGlaivePacket::new)
                .encoder(ForgeGlaivePacket::toBytes)
                .consumerMainThread(ForgeGlaivePacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeSingleByteC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ForgeSingleByteC2SPacket::new)
                .encoder(ForgeSingleByteC2SPacket::toBytes)
                .consumerMainThread(ForgeSingleByteC2SPacket::handle)
                .add();

        /**Server to Client Packets*/
        INSTANCE.messageBuilder(ForgeGenericIntPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeGenericIntPacket::new)
                .encoder(ForgeGenericIntPacket::toBytes)
                .consumerMainThread(ForgeGenericIntPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeClashUpdatePacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeClashUpdatePacket::new)
                .encoder(ForgeClashUpdatePacket::toBytes)
                .consumerMainThread(ForgeClashUpdatePacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeNBTPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeNBTPacket::new)
                .encoder(ForgeNBTPacket::toBytes)
                .consumerMainThread(ForgeNBTPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgePlaySoundPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgePlaySoundPacket::new)
                .encoder(ForgePlaySoundPacket::toBytes)
                .consumerMainThread(ForgePlaySoundPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeStopSoundPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeStopSoundPacket::new)
                .encoder(ForgeStopSoundPacket::toBytes)
                .consumerMainThread(ForgeStopSoundPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeGuardUpdatePacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeGuardUpdatePacket::new)
                .encoder(ForgeGuardUpdatePacket::toBytes)
                .consumerMainThread(ForgeGuardUpdatePacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeUpdateDazePacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeUpdateDazePacket::new)
                .encoder(ForgeUpdateDazePacket::toBytes)
                .consumerMainThread(ForgeUpdateDazePacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeCDSyncPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeCDSyncPacket::new)
                .encoder(ForgeCDSyncPacket::toBytes)
                .consumerMainThread(ForgeCDSyncPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeSkillCDSyncPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeSkillCDSyncPacket::new)
                .encoder(ForgeSkillCDSyncPacket::toBytes)
                .consumerMainThread(ForgeSkillCDSyncPacket::handle)
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
        INSTANCE.messageBuilder(ForgePowerIntPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgePowerIntPacket::new)
                .encoder(ForgePowerIntPacket::toBytes)
                .consumerMainThread(ForgePowerIntPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message){
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToClient(MSG message, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
