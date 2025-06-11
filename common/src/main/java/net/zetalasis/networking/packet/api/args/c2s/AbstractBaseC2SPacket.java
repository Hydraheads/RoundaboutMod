package net.zetalasis.networking.packet.api.args.c2s;

import net.minecraft.network.FriendlyByteBuf;

public abstract class AbstractBaseC2SPacket {
    /** Main method of the packet (Forge + Fabric)
     * @param args The main arguments of the packet
     * @param buf Compatibility for the NetworkMessages API. You generally don't need it. */
    public abstract void handle(PacketArgsC2S args, FriendlyByteBuf buf);
}