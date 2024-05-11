package net.hydra.jojomod.networking.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.networking.component.StandUserComponent;
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
            StandUserComponent standUserData = MyComponents.STAND_USER.get(client.player);
            StandPowers powers = standUserData.getStandPowers();
            powers.setAttackTime(attackTime);
            powers.setAttackTimeMax(attackTimeMax);
            powers.setAttackTimeDuring(attackTimeDuring);
            powers.setActivePower(activePower);
            powers.setActivePowerPhase(activePowerPhase);
        }
    }
}
