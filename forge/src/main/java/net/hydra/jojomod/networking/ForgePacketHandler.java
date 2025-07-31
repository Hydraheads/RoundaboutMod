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
        INSTANCE.messageBuilder(ForgeCreativeModeSlotPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ForgeCreativeModeSlotPacket::new)
                .encoder(ForgeCreativeModeSlotPacket::toBytes)
                .consumerMainThread(ForgeCreativeModeSlotPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeItemChangePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ForgeItemChangePacket::new)
                .encoder(ForgeItemChangePacket::toBytes)
                .consumerMainThread(ForgeItemChangePacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeHandshakePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ForgeHandshakePacket::new)
                .encoder(ForgeHandshakePacket::toBytes)
                .consumerMainThread(ForgeHandshakePacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeAckDynamicWorld.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ForgeAckDynamicWorld::new)
                .encoder(ForgeAckDynamicWorld::toBytes)
                .consumerMainThread(ForgeAckDynamicWorld::handle)
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
        INSTANCE.messageBuilder(ForgeSkillCDSyncMaxPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeSkillCDSyncMaxPacket::new)
                .encoder(ForgeSkillCDSyncMaxPacket::toBytes)
                .consumerMainThread(ForgeSkillCDSyncMaxPacket::handle)
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
        INSTANCE.messageBuilder(ForgeSimpleBytePacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeSimpleBytePacket::new)
                .encoder(ForgeSimpleBytePacket::toBytes)
                .consumerMainThread(ForgeSimpleBytePacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeBlipPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeBlipPacket::new)
                .encoder(ForgeBlipPacket::toBytes)
                .consumerMainThread(ForgeBlipPacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeBundlePacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeBundlePacket::new)
                .encoder(ForgeBundlePacket::toBytes)
                .consumerMainThread(ForgeBundlePacket::handle)
                .add();
        INSTANCE.messageBuilder(ForgeS2CPowerInventorySettingsPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeS2CPowerInventorySettingsPacket::new)
                .encoder(ForgeS2CPowerInventorySettingsPacket::toBytes)
                .consumerMainThread(ForgeS2CPowerInventorySettingsPacket::handle)
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
        INSTANCE.messageBuilder(ForgeSendConfigPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeSendConfigPacket::new)
                .encoder(ForgeSendConfigPacket::toBytes)
                .consumerMainThread(ForgeSendConfigPacket::handle)
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
