package net.hydra.jojomod.networking.packet.api.args.s2c;

import net.minecraft.network.FriendlyByteBuf;

public abstract class AbstractBaseS2CPacket {
    /** Forge compatibility */
    public abstract void deserialize(Object... vargs);
    /** Forge compatibility */
    public abstract void serialize(FriendlyByteBuf buf);
    /** Forge compatibility */
    public abstract void toBytes(FriendlyByteBuf buf);

    /** Main method of the packet (Forge & Fabric) */
    public abstract void handle(PacketArgsS2C args);
}