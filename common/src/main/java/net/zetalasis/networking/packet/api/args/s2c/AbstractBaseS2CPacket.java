package net.zetalasis.networking.packet.api.args.s2c;

import net.minecraft.network.FriendlyByteBuf;

public abstract class AbstractBaseS2CPacket {
    /** Main method of the packet (Forge + Fabric)
     * @param args The main arguments of the packet
     * @param buf Compatibility for the NetworkMessages API. You generally don't need it. */
    public abstract void handle(PacketArgsS2C args, FriendlyByteBuf buf);
}