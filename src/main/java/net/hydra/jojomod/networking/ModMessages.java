package net.hydra.jojomod.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.networking.packet.SummonC2SPacket;
import net.minecraft.util.Identifier;

public class ModMessages {
    //packets!
    public static final Identifier SUMMON_ID = new Identifier(RoundaboutMod.MOD_ID,"summon");

    //Client To Server
    public static void registerC2SPackets(){
        ServerPlayNetworking.registerGlobalReceiver(SUMMON_ID, SummonC2SPacket::receive);

    }
    //Server to Client
    public static void registerS2CPackets(){

    }
}
