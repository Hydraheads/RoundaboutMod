package net.hydra.jojomod.networking.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;

public class StandS2CPacket {
    public static void clashUpdate(Minecraft client, ClientPacketListener handler,
                                 FriendlyByteBuf buf, PacketSender responseSender) {
        //Everything here is server only!
        if (client.player != null) {
            ((StandUser) client.player).roundabout$getStandPowers().setClashOp((LivingEntity) client.player.level().getEntity(buf.readInt()));
            ((StandUser) client.player).roundabout$getStandPowers().setClashOpProgress(buf.readFloat());
        }
    }
}
