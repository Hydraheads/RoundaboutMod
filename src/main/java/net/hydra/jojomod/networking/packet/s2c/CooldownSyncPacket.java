package net.hydra.jojomod.networking.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class CooldownSyncPacket {
    public static void updateAttackCooldowns(MinecraftClient client, ClientPlayNetworkHandler handler,
                                   PacketByteBuf buf, PacketSender responseSender) {
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

    public static void updateGuard(MinecraftClient client, ClientPlayNetworkHandler handler,
                                   PacketByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            float guardPoints = buf.readFloat();
            boolean guardBroken = buf.readBoolean();
            ((StandUser) client.player).setGuardPoints(guardPoints);
            ((StandUser) client.player).setGuardBroken(guardBroken);
        }
    }

    public static void updateDaze(MinecraftClient client, ClientPlayNetworkHandler handler,
                                   PacketByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            byte dazeTime = buf.readByte();
            ((StandUser) client.player).setDazeTime(dazeTime);
        }
    }
}
