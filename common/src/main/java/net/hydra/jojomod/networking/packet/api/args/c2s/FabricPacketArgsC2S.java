package net.hydra.jojomod.networking.packet.api.args.c2s;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class FabricPacketArgsC2S {
    public MinecraftServer server;
    public ServerPlayer player;
    public ServerGamePacketListenerImpl handler;
    public FriendlyByteBuf buf;
    public Object responseSender;

    public FabricPacketArgsC2S(MinecraftServer server, ServerPlayer player,
                               ServerGamePacketListenerImpl handler,
                               FriendlyByteBuf buf, Object responseSender)
    {
        this.server = server;
        this.player = player;
        this.handler = handler;
        this.buf = buf;
        this.responseSender = responseSender;
    }
}
