package net.hydra.jojomod.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.networking.packet.StandSyncS2CPacket;
import net.hydra.jojomod.networking.packet.SummonC2SPacket;
import net.minecraft.util.Identifier;

public class ModMessages {
    //packets!
    public static final Identifier SUMMON_ID = new Identifier(RoundaboutMod.MOD_ID,"summon");
    public static final Identifier STAND_SYNC_ID = new Identifier(RoundaboutMod.MOD_ID,"stand_sync");
    public static final Identifier RIDE_SYNC_ID = new Identifier(RoundaboutMod.MOD_ID,"ride_sync");

    //Client To Server
    public static void registerC2SPackets(){
        ServerPlayNetworking.registerGlobalReceiver(SUMMON_ID, SummonC2SPacket::receive);

    }
    //Server to Client
    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(STAND_SYNC_ID, StandSyncS2CPacket::receive);
    }
}
