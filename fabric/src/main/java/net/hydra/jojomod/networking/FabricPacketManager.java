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
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.TIME_STOP_ENTITY_PACKET, TimeEventPackets::updateTSList);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.TIME_STOP_ENTITY_REMOVAL_PACKET, TimeEventPackets::updateTSRemovalList);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.PERMA_CASTING_ENTITY_PACKET, TimeEventPackets::updatePermaCastingList);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.PERMA_CASTING_ENTITY_REMOVAL_PACKET, TimeEventPackets::updatePermaCastingRemovalList);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.RESUME_TILE_ENTITY_TS_PACKET, TimeEventPackets::updateTileEntityTS);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.SEND_FLOAT_POWER_DATA_PACKET, CooldownSyncPacket::sendFloatPower);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.SEND_INT_DATA_PACKET, GenericS2CPacket::sendInt);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.SEND_INT_POWER_DATA_PACKET, CooldownSyncPacket::sendIntPower);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.SEND_SIMPLE_BYTE_PACKET, GenericS2CPacket::sendSimpleByte);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.SEND_BUNDLE_PACKET, GenericS2CPacket::sendBundle);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.SEND_S2C_POWER_INVENTORY_OPTIONS, GenericS2CPacket::sendPowerInventorySettings);

        ClientPlayNetworking.registerGlobalReceiver(ModMessages.DYNAMIC_WORLD_SYNC, DynamicWorldSync::updateWorlds);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.DYNAMIC_WORLD_DEREGISTER, DynamicWorldDeregister::updateWorlds);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.EJECT_PARALLEL_RUNNING, EjectPRunningS2CPacket::eject);
    }
}
