package net.hydra.jojomod.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hydra.jojomod.networking.packet.c2s.*;
import net.hydra.jojomod.networking.packet.s2c.*;

public class FabricPacketManager {


    //Client To Server
    public static void registerC2SPackets(){

    }
    //Server to Client
    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.RESUME_TILE_ENTITY_TS_PACKET, TimeEventPackets::updateTileEntityTS);

        ClientPlayNetworking.registerGlobalReceiver(ModMessages.DYNAMIC_WORLD_SYNC, DynamicWorldSync::updateWorlds);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.DYNAMIC_WORLD_DEREGISTER, DynamicWorldDeregister::updateWorlds);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.EJECT_PARALLEL_RUNNING, EjectPRunningS2CPacket::eject);
    }
}
