package net.hydra.jojomod.networking.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
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
            StandPowers powers = ((StandUser) client.player).getStandPowers();
            powers.setAttackTime(attackTime);
            powers.setAttackTimeMax(attackTimeMax);
            powers.setAttackTimeDuring(attackTimeDuring);
            powers.setActivePower(activePower);
            powers.setActivePowerPhase(activePowerPhase);
            powers.kickStartClient();
        }
    }

    public static void updateGuard(Minecraft client, ClientPacketListener handler,
                                   FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            float guardPoints = buf.readFloat();
            boolean guardBroken = buf.readBoolean();
            ((StandUser) client.player).setGuardPoints(guardPoints);
            ((StandUser) client.player).setGuardBroken(guardBroken);
        }
    }

    public static void updateDaze(Minecraft client, ClientPacketListener handler,
                                   FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            byte dazeTime = buf.readByte();
            ((StandUser) client.player).setDazeTime(dazeTime);
        }
    }

    public static void sendFloatPower(Minecraft client, ClientPacketListener handler,
                                  FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            byte activePower = buf.readByte();
            float data = buf.readFloat();
            ((StandUser) client.player).getStandPowers().updatePowerFloat(activePower,data);
        }
    }
    public static void sendIntPower(Minecraft client, ClientPacketListener handler,
                                      FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            byte activePower = buf.readByte();
            int data = buf.readInt();
            ((StandUser) client.player).getStandPowers().updatePowerInt(activePower,data);
        }
    }
}
