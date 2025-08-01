package net.hydra.jojomod.networking.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

public class CooldownSyncPacket {
    public static void updateAttackCooldowns(Minecraft client, ClientPacketListener handler,
                                   FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            int attackTime = buf.readInt();
            int attackTimeMax = buf.readInt();
            int attackTimeDuring = buf.readInt();
            byte activePower = buf.readByte();
            byte activePowerPhase = buf.readByte();
            MainUtil.syncCooldownsForAttacks(attackTime,attackTimeMax,attackTimeDuring,
                    activePower,activePowerPhase,client.player);
        }
    }



    public static void sendFloatPower(Minecraft client, ClientPacketListener handler,
                                  FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            byte activePower = buf.readByte();
            float data = buf.readFloat();
            ((StandUser) client.player).roundabout$getStandPowers().updatePowerFloat(activePower,data);
        }
    }
    public static void sendIntPower(Minecraft client, ClientPacketListener handler,
                                      FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            byte activePower = buf.readByte();
            int data = buf.readInt();
            ((StandUser) client.player).roundabout$getStandPowers().updatePowerInt(activePower,data);
        }
    }
}
