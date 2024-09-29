package net.hydra.jojomod.networking.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import org.joml.Vector3f;

public class GenericS2CPacket {

    public static void sendSimpleByte(Minecraft client, ClientPacketListener handler,
                               FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            byte activePower = buf.readByte();
            MainUtil.handleSimpleBytePacketS2C(client.player,activePower);
        }
    }

    public static void sendInt(Minecraft client, ClientPacketListener handler,
                                    FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            byte activePower = buf.readByte();
            int data = buf.readInt();
            MainUtil.handleIntPacketS2C(client.player,data,activePower);
        }
    }

    public static void blip(Minecraft client, ClientPacketListener handler,
                               FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            byte activePower = buf.readByte();
            int data = buf.readInt();
            Vector3f location = buf.readVector3f();
            MainUtil.handleBlipPacketS2C(client.player,data,activePower,location);
        }
    }
}
