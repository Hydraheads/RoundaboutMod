package net.hydra.jojomod.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.networking.packet.c2s.MoveSyncPacket;
import net.hydra.jojomod.networking.packet.c2s.StandAbilityPacket;
import net.hydra.jojomod.networking.packet.s2c.NbtSyncPacket;
import net.minecraft.util.Identifier;

public class ModMessages {

    /** Register packets for client and server to communicate. */
    public static final Identifier STAND_SUMMON_PACKET = new Identifier(RoundaboutMod.MOD_ID,"summon_packet");
    public static final Identifier STAND_ATTACK_PACKET = new Identifier(RoundaboutMod.MOD_ID,"attack_packet");
    public static final Identifier STAND_ATTACK_CANCEL_PACKET = new Identifier(RoundaboutMod.MOD_ID,"attack_cancel_packet");
    public static final Identifier NBT_SYNC_ID = new Identifier(RoundaboutMod.MOD_ID,"nbt_sync");
    public static final Identifier MOVE_SYNC_ID = new Identifier(RoundaboutMod.MOD_ID,"move_sync");

    //Client To Server
    public static void registerC2SPackets(){
        ServerPlayNetworking.registerGlobalReceiver(STAND_SUMMON_PACKET, StandAbilityPacket::summon);
        ServerPlayNetworking.registerGlobalReceiver(STAND_ATTACK_PACKET, StandAbilityPacket::attack);
        ServerPlayNetworking.registerGlobalReceiver(STAND_ATTACK_CANCEL_PACKET, StandAbilityPacket::attackCancel);
        ServerPlayNetworking.registerGlobalReceiver(MOVE_SYNC_ID, MoveSyncPacket::receive);

    }
    //Server to Client
    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(NBT_SYNC_ID, NbtSyncPacket::receive);
    }
}
