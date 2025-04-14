package net.hydra.jojomod.networking.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.client.ClientUtil;
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
            ClientUtil.handleSimpleBytePacketS2C(client.player,activePower);
        }
    }

    public static void sendBundle(Minecraft client, ClientPacketListener handler,
                                      FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            byte context = buf.readByte();
            byte firstByte = buf.readByte();
            byte secondByte = buf.readByte();
            byte thirdByte = buf.readByte();
            ClientUtil.handleBundlePacketS2C(client.player,context,firstByte,secondByte,thirdByte);
        }
    }

    public static void sendInt(Minecraft client, ClientPacketListener handler,
                                    FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            byte activePower = buf.readByte();
            int data = buf.readInt();
            ClientUtil.handleIntPacketS2C(client.player,data,activePower);
        }
    }

    public static void blip(Minecraft client, ClientPacketListener handler,
                               FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            byte activePower = buf.readByte();
            int data = buf.readInt();
            Vector3f location = buf.readVector3f();
            ClientUtil.handleBlipPacketS2C(client.player, data, activePower, location);
        }
    }


    public static void sendPowerInventorySettings(Minecraft client, ClientPacketListener handler,
                                  FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            int anchorPlace = buf.readInt();
            float distanceOut = buf.readFloat();
            float idleOpacity = buf.readFloat();
            float combatOpacity = buf.readFloat();
            float enemyOpacity = buf.readFloat();
            int anchorPlaceAttack = buf.readInt();
            ClientUtil.handlePowerInventoryOptionsPacketS2C(client.player,anchorPlace,distanceOut,idleOpacity,combatOpacity,
                    enemyOpacity,anchorPlaceAttack);
        }
    }
}
