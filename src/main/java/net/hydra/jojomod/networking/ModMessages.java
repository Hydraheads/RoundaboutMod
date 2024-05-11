package net.hydra.jojomod.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.networking.packet.c2s.MoveSyncPacket;
import net.hydra.jojomod.networking.packet.c2s.StandAbilityPacket;
import net.hydra.jojomod.networking.packet.s2c.NbtSyncPacket;
import net.minecraft.network.PacketBundleHandler;
import net.minecraft.util.Identifier;

public class ModMessages {

    /** Register packets for client and server to communicate. */
    public static final Identifier STAND_SUMMON_PACKET = new Identifier(RoundaboutMod.MOD_ID,"summon_packet");
    public static final Identifier STAND_ATTACK_PACKET = new Identifier(RoundaboutMod.MOD_ID,"attack_packet");
    public static final Identifier STAND_PUNCH_PACKET = new Identifier(RoundaboutMod.MOD_ID,"punch_packet");
    public static final Identifier STAND_BARRAGE_PACKET = new Identifier(RoundaboutMod.MOD_ID,"barrage_packet");
    public static final Identifier STAND_BARRAGE_HIT_PACKET = new Identifier(RoundaboutMod.MOD_ID,"barrage_hit_packet");
    public static final Identifier NBT_SYNC_ID = new Identifier(RoundaboutMod.MOD_ID,"nbt_sync");
    public static final Identifier MOVE_SYNC_ID = new Identifier(RoundaboutMod.MOD_ID,"move_sync");
    public static final Identifier STAND_GUARD_PACKET = new Identifier(RoundaboutMod.MOD_ID,"guard_packet");
    public static final Identifier STAND_GUARD_CANCEL_PACKET = new Identifier(RoundaboutMod.MOD_ID,"guard_cancel_packet");

    //Client To Server
    public static void registerC2SPackets(){
        ServerPlayNetworking.registerGlobalReceiver(STAND_SUMMON_PACKET, StandAbilityPacket::summon);
        ServerPlayNetworking.registerGlobalReceiver(STAND_ATTACK_PACKET, StandAbilityPacket::attack);
        ServerPlayNetworking.registerGlobalReceiver(STAND_PUNCH_PACKET, StandAbilityPacket::punch);
        ServerPlayNetworking.registerGlobalReceiver(STAND_BARRAGE_PACKET, StandAbilityPacket::barrage);
        ServerPlayNetworking.registerGlobalReceiver(STAND_BARRAGE_HIT_PACKET, StandAbilityPacket::barrageHit);
        ServerPlayNetworking.registerGlobalReceiver(MOVE_SYNC_ID, MoveSyncPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(STAND_GUARD_PACKET, StandAbilityPacket::guard);
        ServerPlayNetworking.registerGlobalReceiver(STAND_GUARD_CANCEL_PACKET, StandAbilityPacket::guardCancel);

    }
    //Server to Client
    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(NBT_SYNC_ID, NbtSyncPacket::receive);
    }
}
