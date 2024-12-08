package net.hydra.jojomod.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hydra.jojomod.networking.packet.c2s.ConfigC2S;
import net.hydra.jojomod.networking.packet.c2s.MoveSyncPacket;
import net.hydra.jojomod.networking.packet.c2s.StandAbilityPacket;
import net.hydra.jojomod.networking.packet.c2s.UtilC2S;
import net.hydra.jojomod.networking.packet.s2c.*;

public class FabricPacketManager {


    //Client To Server
    public static void registerC2SPackets(){
        ServerPlayNetworking.registerGlobalReceiver(ModMessages.STAND_SUMMON_PACKET, StandAbilityPacket::summon);
        ServerPlayNetworking.registerGlobalReceiver(ModMessages.STAND_POWER_PACKET, StandAbilityPacket::switchPower);
        ServerPlayNetworking.registerGlobalReceiver(ModMessages.STAND_POS_POWER_PACKET, StandAbilityPacket::switchPosPower);
        ServerPlayNetworking.registerGlobalReceiver(ModMessages.STAND_CHARGED_POWER_PACKET, StandAbilityPacket::switchChargedPower);
        ServerPlayNetworking.registerGlobalReceiver(ModMessages.STAND_PUNCH_PACKET, StandAbilityPacket::punch);
        ServerPlayNetworking.registerGlobalReceiver(ModMessages.STAND_BARRAGE_HIT_PACKET, StandAbilityPacket::barrageHit);
        ServerPlayNetworking.registerGlobalReceiver(ModMessages.MOVE_SYNC_ID, MoveSyncPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(ModMessages.TIME_STOP_JUMP_ID, MoveSyncPacket::updateTSJump);
        ServerPlayNetworking.registerGlobalReceiver(ModMessages.STAND_PILOT_ID, MoveSyncPacket::updateStandLocation);
        ServerPlayNetworking.registerGlobalReceiver(ModMessages.STAND_GUARD_CANCEL_PACKET, StandAbilityPacket::guardCancel);
        ServerPlayNetworking.registerGlobalReceiver(ModMessages.BARRAGE_CLASH_UPDATE_PACKET, StandAbilityPacket::clashUpdate);
        ServerPlayNetworking.registerGlobalReceiver(ModMessages.BYTE_C2S_PACKET, UtilC2S::UpdateByte);
        ServerPlayNetworking.registerGlobalReceiver(ModMessages.SINGLE_BYTE_C2S_PACKET, UtilC2S::UpdateSingleByte);
        ServerPlayNetworking.registerGlobalReceiver(ModMessages.FLOAT_C2S_PACKET, UtilC2S::UpdateFloat);
        ServerPlayNetworking.registerGlobalReceiver(ModMessages.INT_C2S_PACKET, UtilC2S::UpdateInt);
        ServerPlayNetworking.registerGlobalReceiver(ModMessages.GLAIVE_C2S_PACKET, UtilC2S::glaiveAttack);
        ServerPlayNetworking.registerGlobalReceiver(ModMessages.INVENTORY_C2S_PACKET, UtilC2S::inventoryChange);
        ServerPlayNetworking.registerGlobalReceiver(ModMessages.HANDSHAKE, ConfigC2S::Handshake);
    }
    //Server to Client
    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.NBT_SYNC_ID, NbtSyncPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.POWER_COOLDOWN_SYNC_ID, CooldownSyncPacket::updateAttackCooldowns);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.SKILL_COOLDOWN_SYNC_ID, CooldownSyncPacket::updateSkillCooldowns);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.STAND_GUARD_POINT_ID, CooldownSyncPacket::updateGuard);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.DAZE_ID, CooldownSyncPacket::updateDaze);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.SOUND_CANCEL_ID, SoundStopPacket::stopSound);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.SOUND_PLAY_ID, SoundStopPacket::playSound);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.BARRAGE_CLASH_UPDATE_S2C_PACKET, StandS2CPacket::clashUpdate);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.TIME_STOP_ENTITY_PACKET, TimeEventPackets::updateTSList);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.TIME_STOP_ENTITY_REMOVAL_PACKET, TimeEventPackets::updateTSRemovalList);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.PERMA_CASTING_ENTITY_PACKET, TimeEventPackets::updatePermaCastingList);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.PERMA_CASTING_ENTITY_REMOVAL_PACKET, TimeEventPackets::updatePermaCastingRemovalList);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.RESUME_TILE_ENTITY_TS_PACKET, TimeEventPackets::updateTileEntityTS);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.SEND_FLOAT_POWER_DATA_PACKET, CooldownSyncPacket::sendFloatPower);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.SEND_INT_DATA_PACKET, GenericS2CPacket::sendInt);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.SEND_INT_POWER_DATA_PACKET, CooldownSyncPacket::sendIntPower);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.BLIP_PACKET, GenericS2CPacket::blip);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.SEND_SIMPLE_BYTE_PACKET, GenericS2CPacket::sendSimpleByte);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.SEND_BUNDLE_PACKET, GenericS2CPacket::sendBundle);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.SEND_S2C_POWER_INVENTORY_OPTIONS, GenericS2CPacket::sendPowerInventorySettings);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.CONFIG_SYNC, ConfigSyncPacket::readConfig);
    }

}
