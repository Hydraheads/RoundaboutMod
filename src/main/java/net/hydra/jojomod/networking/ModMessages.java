package net.hydra.jojomod.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.networking.packet.c2s.MoveSyncPacket;
import net.hydra.jojomod.networking.packet.s2c.StandSyncPacket;
import net.hydra.jojomod.networking.packet.c2s.SummonPacket;
import net.minecraft.util.Identifier;

public class ModMessages {
    //packets!
    public static final Identifier SUMMON_ID = new Identifier(RoundaboutMod.MOD_ID,"summon");
    public static final Identifier STAND_SYNC_ID = new Identifier(RoundaboutMod.MOD_ID,"stand_sync");
    public static final Identifier MOVE_SYNC_ID = new Identifier(RoundaboutMod.MOD_ID,"move_sync");

    //Client To Server
    public static void registerC2SPackets(){
        ServerPlayNetworking.registerGlobalReceiver(SUMMON_ID, SummonPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(MOVE_SYNC_ID, MoveSyncPacket::receive);

    }
    //Server to Client
    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(STAND_SYNC_ID, StandSyncPacket::receive);
    }
}
