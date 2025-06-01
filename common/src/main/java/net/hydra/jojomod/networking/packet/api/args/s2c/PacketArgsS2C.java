package net.hydra.jojomod.networking.packet.api.args.s2c;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

/** Structure class for cross-loader S2C packets (as each loader passes diff. arguments to its packets) */
public class PacketArgsS2C {
    public Minecraft client;
    public ClientPacketListener handler;
    public FriendlyByteBuf buf;
    public Object responseSender;

    public PacketArgsS2C(Minecraft client, ClientPacketListener handler,
                               FriendlyByteBuf buf, Object responseSender)
    {
        this.client = client;
        this.handler = handler;
        this.buf = buf;
        this.responseSender = responseSender;
    }
}