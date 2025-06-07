package net.hydra.jojomod.networking.packet.api.args.c2s;

import net.minecraft.network.FriendlyByteBuf;

public abstract class AbstractBaseC2SPacket {
    /** Forge compatibility */
    public abstract void deserialize(Object... vargs);
    /** Forge compatibility */
    public abstract void serialize(FriendlyByteBuf buf);
    /** Forge compatibility */
    public abstract void toBytes(FriendlyByteBuf buf);

    /** Main method of the packet (Forge + Fabric) */
    public abstract void handle(PacketArgsC2S args);
}
