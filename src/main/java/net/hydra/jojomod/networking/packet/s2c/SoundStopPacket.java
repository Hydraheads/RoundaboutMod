package net.hydra.jojomod.networking.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;

public class SoundStopPacket {


    public static void stopSound(MinecraftClient client, ClientPlayNetworkHandler handler,
                                  PacketByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            Entity User = client.player.getWorld().getEntityById(buf.readInt());
            if (User instanceof LivingEntity){
                ((StandUser)User).stopSounds(buf.readByte());
            }
        }
    }
}
