package net.hydra.jojomod.networking.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.event.powers.StandUserClient;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.mixin.TimeStopWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class TimeEventPackets {
    public static void updateTSList(Minecraft client, ClientPacketListener handler,
                                 FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            Entity timeStoppingEntity = client.player.level().getEntity(buf.readInt());
            boolean removal = buf.readBoolean();
            if (timeStoppingEntity instanceof LivingEntity){
                ((TimeStop)client.player.level()).processTSPacket((LivingEntity) timeStoppingEntity,removal);
            }
        }
    }
}
