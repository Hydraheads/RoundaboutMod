package net.hydra.jojomod.networking.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class StandS2CPacket {
    public static void clashUpdate(MinecraftClient client, ClientPlayNetworkHandler handler,
                                 PacketByteBuf buf, PacketSender responseSender) {
        //Everything here is server only!
        if (client.player != null) {
            if (((StandUser) client.player).isClashing()) {
                ((StandUser) client.player).getStandPowers().setClashOp((LivingEntity) client.player.getWorld().getEntityById(buf.readInt()));
                ((StandUser) client.player).getStandPowers().setClashOpProgress(buf.readFloat());
            }
        }
    }
}
