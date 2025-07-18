package net.zetalasis.networking.packet.api.args.c2s;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.Nullable;

/** Structure class for cross-loader C2S packets (as each loader passes diff. arguments to its packets) */
public class PacketArgsC2S {
    public MinecraftServer server;
    public ServerPlayer player;
    public ServerGamePacketListenerImpl handler;
    public Object[] vargs;

    public PacketArgsC2S(MinecraftServer server, ServerPlayer player,
                               @Nullable ServerGamePacketListenerImpl handler,
                               Object... vargs)
    {
        this.server = server;
        this.player = player;
        this.handler = handler;
        this.vargs = vargs;
    }
}